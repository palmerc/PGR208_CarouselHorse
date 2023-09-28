package no.kristiania.carouselhorse

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okio.ByteString
import java.io.ByteArrayOutputStream


class CarouselViewModel(application: Application): AndroidViewModel(application) {
    private val TAG = "CarouselViewModel"
    private val _carouselState = MutableStateFlow(CarouselState())
    val carouselState: StateFlow<CarouselState> = _carouselState.asStateFlow()

    private var currentHorseIndex = 0
    private var automaticHorse = false
    private val delayInMillis = 50L
    private var direction = Directions.Unknown
    private var listener: EchoWebSocketListener? = null
    private var ws: WebSocket? = null

    init {
        reset()
        startWebSocket()
    }

    private fun triggerHorseUpdate() {
        currentHorseIndex %= (carouselOfHorses.size - 1)
        Log.d(TAG, "Horse $currentHorseIndex")

        val horseResource = carouselOfHorses[currentHorseIndex]
        Log.d(TAG, "Horse Resource: $horseResource")

        _carouselState.update { currentState ->
            currentState.copy(currentHorse = horseResource)
        }

        sendBytes(horseResource)
    }

    fun nextHorse(repeat: Boolean = false) {
        if (!repeat) {
            if (direction == Directions.Backward || direction == Directions.Unknown) {
                direction = Directions.Forward
            } else if (automaticHorse) {
                return
            }
        }

        if (direction == Directions.Forward) {
            currentHorseIndex += 1
            triggerHorseUpdate()
            if (automaticHorse) {
                GlobalScope.launch(Dispatchers.Main) {
                    delay(delayInMillis)
                    nextHorse(repeat = true)
                }
            }
        }
    }

    fun previousHorse(repeat: Boolean = false) {
        if (!repeat) {
            if (direction == Directions.Forward || direction == Directions.Unknown) {
                direction = Directions.Backward
            } else if (automaticHorse) {
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
                    previousHorse(repeat = true)
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

    private fun startWebSocket() {
        // TODO Change the IP Address
        val wsURL = "ws://192.168.7.79:8765"
        val wsRequest: Request = Request.Builder().url(wsURL).build()
        listener = EchoWebSocketListener()
//        listener?.delegate = this
        listener?.also {
            ws = OkHttpClient().newWebSocket(wsRequest, it)
        }
    }

    private fun stopWebSocket() {
        ws?.close(code = 1000, reason = "Client is exiting.")
        ws = null
    }

    fun sendBytes(resourceId: Int) {
        val resources = getApplication<Application>().resources
        val bm = BitmapFactory.decodeResource(resources, resourceId)
        val stream = ByteArrayOutputStream()
        GlobalScope.launch {
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream)
            ws?.send(bytes = ByteString.of(*stream.toByteArray()))
        }
    }
}
