package no.kristiania.carouselhorse

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CarouselViewModel: ViewModel() {
    private val TAG = "CarouselViewModel"
    private val _carouselState = MutableStateFlow(CarouselState())
    val carouselState: StateFlow<CarouselState> = _carouselState.asStateFlow()
    private var currentHorseIndex = 0
    private var automaticHorse = false
    private val delayInMillis = 50L
    private var direction = Directions.Unknown

    init {
        reset()
    }

    private fun triggerHorseUpdate() {
        currentHorseIndex %= (carouselOfHorses.size - 1)
        Log.d(TAG, "Horse $currentHorseIndex")

        _carouselState.update { currentState ->
            val horseResource = carouselOfHorses[currentHorseIndex]
            Log.d(TAG, "Horse Resource: $horseResource")
            currentState.copy(currentHorse = horseResource)
        }
    }

    fun nextHorse(click: Boolean = false) {
        if (click) {
            if (direction == Directions.Backward || direction == Directions.Unknown) {
                direction = Directions.Forward
            } else {
                return
            }
        }

        if (direction == Directions.Forward) {
            currentHorseIndex += 1
            triggerHorseUpdate()
            if (automaticHorse) {
                GlobalScope.launch(Dispatchers.Main) {
                    delay(delayInMillis)
                    nextHorse()
                }
            }
        }
    }

    fun previousHorse(click: Boolean = false) {
        if (click) {
            if (direction == Directions.Forward || direction == Directions.Unknown) {
                direction = Directions.Backward
            } else {
                return
            }
        }

        if (direction == Directions.Backward) {
            currentHorseIndex -= 1
            if (currentHorseIndex < 0) currentHorseIndex = carouselOfHorses.size - 2
            triggerHorseUpdate()
            if (automaticHorse) {
                GlobalScope.launch(Dispatchers.Main) {
                    delay(delayInMillis)
                    previousHorse()
                }
            }
        }
    }

    fun toggleAutomatic(): Boolean {
        val state = automaticHorse
        automaticHorse = !state
        Log.d(TAG, "$automaticHorse")
        return automaticHorse
    }

    fun reset() {
        _carouselState.value = CarouselState(currentHorse = standingHorse)
    }
}
