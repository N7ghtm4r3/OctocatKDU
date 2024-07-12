plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("maven-publish")
}

group = "com.tecknobit"
version = "1.0.4"

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
    implementation("com.mikepenz:multiplatform-markdown-renderer-m3:0.14.0")
    implementation("org.json:json:20231013")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.tecknobit.octocatkdu"
                artifactId = "OctocatKDU"
                version = "1.0.4"
                from(components["java"])
            }
        }
    }
}