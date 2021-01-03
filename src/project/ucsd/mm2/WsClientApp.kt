package project.ucsd.mm2

import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.client.*
import kotlinx.coroutines.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.ktor.client.features.websocket.WebSockets
import io.ktor.http.cio.websocket.Frame
import kotlinx.coroutines.channels.*

object WsClientApp {
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val client = HttpClient(CIO).config { install(WebSockets) }
            client.ws(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/myws/echo") {
                send(Frame.Text("Hello World"))
                incoming.consumeEach { frame ->
                    val message = frame as? Frame.Text
                    message?.let { println("Server said: " + it.readText()) }
                }
            }
        }
    }
}
