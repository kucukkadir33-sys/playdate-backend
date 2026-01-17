package com.playdate

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


fun Application.module() {
    configureSockets()
    configureRouting()
}
