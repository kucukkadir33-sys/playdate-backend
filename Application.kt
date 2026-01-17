package com.playdate

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(
        Netty,
        host = "0.0.0.0",
        port = System.getenv("PORT")?.toInt() ?: 8080
    ) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    configureSockets()
    configureRouting()
}
