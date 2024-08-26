plugins {
    kotlin("jvm") version "2.0.0"
    id("com.google.cloud.tools.jib") version "3.4.3"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.http4k:http4k-bom:5.27.0.0"))
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-server-jetty")
    implementation("org.jetbrains.kotlinx:kotlinx-html:0.11.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "AppKt"
}