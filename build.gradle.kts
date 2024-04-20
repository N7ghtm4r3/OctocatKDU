plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("maven-publish")
}

group = "com.tecknobit"
version = "1.0.3"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.components.resources)
    implementation(compose.material3)
    implementation("com.github.N7ghtm4r3:GitHubManager:1.0.0")
    implementation("com.github.N7ghtm4r3:APIManager:2.2.3")
    implementation("org.json:json:20230227")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.tecknobit.octocatkdu"
                artifactId = "OctocatKDU"
                version = "1.0.3"
                from(components["java"])
            }
        }
    }
}