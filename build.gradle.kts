plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "com.tecknobit"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
    maven("https://repo.clojars.org")
    google()
}

dependencies {
    implementation(compose.desktop.common)
    implementation("com.github.N7ghtm4r3:GitHubManager:1.0.0")
    implementation("com.github.N7ghtm4r3:APIManager:2.2.1")
    implementation("com.github.N7ghtm4r3:Mantis:1.0.0")
    implementation("org.json:json:20230227")
}