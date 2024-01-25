plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("maven-publish")
}

group = "com.tecknobit"
version = "1.0.2"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
    maven("https://repo.clojars.org")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.github.N7ghtm4r3:GitHubManager:1.0.0")
    implementation("com.github.N7ghtm4r3:APIManager:2.2.2")
    implementation("com.github.N7ghtm4r3:Mantis:1.0.0")
    implementation("org.json:json:20230227")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.tecknobit.octocatkdu"
                artifactId = "OctocatKDU"
                version = "1.0.2"
                from(components["java"])
            }
        }
    }
}