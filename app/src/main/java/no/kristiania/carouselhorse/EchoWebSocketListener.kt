package no.kristiania.carouselhorse

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString


interface EchoWebSocketListenerDelegate {
    fun textReceived(msg: String)
    fun bytesReceived(msg: ByteString)
}



class EchoWebSocketListener : WebSocketListener() {
    val TAG = "EchoWebSocketListener"
    var delegate: EchoWebSocketListenerDelegate? = null

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d(TAG, "WSL onOpen")

        webSocket.send("You have entered the chat.")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d(TAG, "WSL onMessage text: ${text}")

        delegate?.textReceived(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d(TAG, "WSL onMessage bytes: ${bytes.size} bytes")

        delegate?.bytesReceived(bytes)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d(TAG, "WSL onClosing")

        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        Log.d(TAG, "Closing : $code / $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d(TAG, "WSL onClosed")
    }

    companion object {
        private val NORMAL_CLOSURE_STATUS = 1000
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d(TAG, "WSL onFailure")
        Log.e(TAG, "${t.message}")
    }
}