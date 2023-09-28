# Lecture 5 - Networked Horse

[Lecture 5 Repository](https://github.com/palmerc/PGR208_CarouselHorse)

* [Kotlin Language Reference](https://kotlinlang.org)
* [Jetpack Compose Reference](https://www.jetpackcompose.net)

We have a series of commits that progressively build up the Alternative assignment of just animating the horse. We achieve this by using a [State Flow](https://developer.android.com/jetpack/compose/state). Why?

As we have repeatedly stated, updating the screen is an expensive operation so we try to clue the Android frameworks into when we need to have the screen redrawn. We achieve this by creating an object to hold our state, and connecting it to the View via a [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel).

The state in the Alternative assignment was to pass the Resource id of a carousel of horses. The only complicated logic is really the play between automatic and manual updating of the horse image.

Extending this to the Network leverages our previous websocket code and instead of sending strings, we send bytes. The hard part of this is Decoding image assets into bytes that are suitable for transmitting over the websocket and general code organization.

## Key elements

### The decoding of a resource and transmitting it

    fun sendBytes(resourceId: Int) {
        val resources = getApplication<Application>().resources
        val bm = BitmapFactory.decodeResource(resources, resourceId)
        val stream = ByteArrayOutputStream()
        GlobalScope.launch {
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream)
            ws?.send(bytes = ByteString.of(*stream.toByteArray()))
        }
    }

### Receiving the bytes [ByteString](https://square.github.io/okio/2.x/okio/okio/-byte-string/index.html) and turning it back into a [Bitmap](https://developer.android.com/reference/android/graphics/Bitmap)

    override fun bytesReceived(msg: ByteString) {
        val data = msg.toByteArray()
        triggerHorseUpdate(BitmapFactory.decodeByteArray(data, 0, data.size))
    }

### Triggering the state change

    private fun triggerHorseUpdate(bitmap: Bitmap) {
        _carouselState.update { currentState ->
            currentState.copy(currentHorse = bitmap)
        }
    }

