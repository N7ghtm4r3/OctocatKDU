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
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("com.github.N7ghtm4r3:GitHubManager:1.0.0")
    implementation("com.github.N7ghtm4r3:APIManager:2.2.1")
    implementation("com.github.N7ghtm4r3:Mantis:1.0.0")
}