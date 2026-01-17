package com.playdate

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import java.util.*
import kotlin.time.Duration.Companion.seconds

// ---- IN-MEMORY QUEUE (şimdilik) ----
private val queue = LinkedHashSet<DefaultWebSocketServerSession>()

fun Application.configureSockets() {

    install(WebSockets) {
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        webSocket("/ws") {

            println("WS CONNECTED")

            try {
                for (frame in incoming) {
                    if (frame !is Frame.Text) continue

                    when (frame.readText()) {

                        "JOIN_QUEUE" -> {
                            queue.add(this)
                            send(Frame.Text("""{"type":"QUEUE_JOINED"}"""))

                            // bekleme bildirimi
                            send(
                                Frame.Text(
                                    """{"type":"QUEUE_WAITING","payload":{"count":${queue.size}}}"""
                                )
                            )

                            // eşleşme varsa
                            if (queue.size >= 2) {
                                val players = queue.take(2).toList()
                                queue.remove(players[0])
                                queue.remove(players[1])

                                val matchId = UUID.randomUUID().toString()

                                players[0].send(
                                    Frame.Text(
                                        """{"type":"MATCH_FOUND","payload":{"matchId":"$matchId"}}"""
                                    )
                                )
                                players[1].send(
                                    Frame.Text(
                                        """{"type":"MATCH_FOUND","payload":{"matchId":"$matchId"}}"""
                                    )
                                )
                            }
                        }

                        "LEAVE_QUEUE" -> {
                            queue.remove(this)
                            send(Frame.Text("""{"type":"QUEUE_LEFT"}"""))
                        }
                    }
                }
            } catch (e: Exception) {
                println("WS ERROR: ${e.message}")
            } finally {
                queue.remove(this)
                println("WS DISCONNECTED")
            }
        }
    }
}
