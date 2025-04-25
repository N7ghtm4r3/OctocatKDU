
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.dokka)
    alias(libs.plugins.vanniktech.mavenPublish)
}

buildscript {
    dependencies {
        classpath(libs.dokka.base)
    }
}

group = "com.tecknobit"
version = "1.0.6"

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.components.resources)
    implementation(compose.material3)
    implementation(libs.githubManager)
    implementation(libs.apiManager)
    implementation(libs.richeditor)
    implementation(libs.json)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_18)
    }
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
        version = "1.0.6"
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