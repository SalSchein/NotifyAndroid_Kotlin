package com.example.workmanagersample

import android.app.Application
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.Polling
import io.socket.engineio.client.transports.WebSocket
import java.net.URISyntaxException

class WorkManagerApplication : Application() {
    //socket.io connection url
    private var mSocket: Socket? = null

    override fun onCreate() {
        super.onCreate()
        try {
            //creating socket instance
            val options = IO.Options()
            options.reconnection = true //reconnection
            options.forceNew = true
            options.port = 3000
            options.path = "/unresolved-notifications-socket"
            options.secure = false

            //mSocket = IO.socket("http://192.168.0.110:3000/", options)
            mSocket = IO.socket("http://192.168.0.243:3000/", options)
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }

    //return socket instance
    fun getMSocket(): Socket? {
        return mSocket
    }
}