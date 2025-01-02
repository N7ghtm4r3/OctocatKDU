
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.vanniktech.maven.publish") version "0.30.0"
    id("org.jetbrains.dokka") version ("1.9.20")
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.9.20")
    }
}

group = "com.tecknobit"
version = "1.0.5"

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
    implementation("com.github.N7ghtm4r3:GitHubManager:1.0.1")
    implementation("com.github.N7ghtm4r3:APIManager:2.2.4")
    implementation("com.mikepenz:multiplatform-markdown-renderer-m3:0.14.0")
    implementation("org.json:json:20240303")
}

mavenPublishing {
    configure(
        platform = KotlinJvm(
            javadocJar = JavadocJar.Dokka("dokkaHtml"),
            sourcesJar = true
        )
    )
    coordinates(
        groupId = "io.github.n7ghtm4r3",
        artifactId = "octocatkdu",
        version = "1.0.5"
    )
    pom {
        name.set("OctocatKDU")
        description.set("Kotlin Desktop Updater based on GitHub releases. From the Github's repository of the application get the release marked as the last-release to warn the user of that application about a new version available")
        inceptionYear.set("2025")
        url.set("https://github.com/N7ghtm4r3/OctocatKDU")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/license/mit")
            }
        }
        developers {
            developer {
                id.set("N7ghtm4r3")
                name.set("Manuel Maurizio")
                email.set("maurizio.manuel2003@gmail.com")
                url.set("https://github.com/N7ghtm4r3")
            }
        }
        scm {
            url.set("https://github.com/N7ghtm4r3/OctocatKDU")
        }
    }
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        moduleName.set("OctocatKDU")
        outputDirectory.set(layout.projectDirectory.dir("docs"))
        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "(c) 2025 Tecknobit"
        }
    }
}