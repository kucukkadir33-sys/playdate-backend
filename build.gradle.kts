plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.7"
    application
}

group = "com.playdate"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

dependencies {

    // Ktor core
    implementation("io.ktor:ktor-server-core-jvm:2.3.7")

    // Netty engine
    implementation("io.ktor:ktor-server-netty-jvm:2.3.7")

    // WebSocket
    implementation("io.ktor:ktor-server-websockets-jvm:2.3.7")

    // YAML config (opsiyonel ama sorun çıkarmaz)
    implementation("io.ktor:ktor-server-config-yaml:2.3.7")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Test
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.7")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.22")
}
