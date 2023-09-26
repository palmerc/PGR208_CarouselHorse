package no.kristiania.carouselhorse

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class CarouselViewModel: ViewModel() {
    private val TAG = "CarouselViewModel"
    private val _carouselState = MutableStateFlow(CarouselState())
    val carouselState: StateFlow<CarouselState> = _carouselState.asStateFlow()
    private var currentHorseIndex = 0
    private var automaticHorse = false

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

    fun nextHorse() {
        currentHorseIndex += 1
        triggerHorseUpdate()
    }

    fun previousHorse() {
        currentHorseIndex -= 1
        if (currentHorseIndex < 0) currentHorseIndex = carouselOfHorses.size - 2
        triggerHorseUpdate()
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
