package no.kristiania.carouselhorse

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class CarouselViewModel: ViewModel() {
    private val TAG = "CarouselViewModel"
    private val _carouselState = MutableStateFlow(CarouselState())
    val carouselState: StateFlow<CarouselState> = _carouselState.asStateFlow()
    private var currentHorseIndex = 0
    
    init {
        reset()
    }

    private fun currentHorse() {
    }

    fun nextHorse() {
    }

    fun previousHorse() {
    }

    fun toggleAutomatic() {
    }

    fun reset() {
        _carouselState.value = CarouselState(currentHorse = standingHorse)
    }
}
