# OctocatKDU

![Maven Central](https://img.shields.io/maven-central/v/io.github.n7ghtm4r3/octocatkdu.svg?label=Maven%20Central)

![Static Badge](https://img.shields.io/badge/desktop-006874?link=https%3A%2F%2Fimg.shields.io%2Fbadge%2Fandroid-4280511051)

**v1.0.5**

Kotlin Desktop Updater based on GitHub releases. From the GitHub's repository of the application get the release marked as the last-release to warn the user of that application about a new version available

## Implementation

### Version catalog

```gradle
[versions]
octocatkdu = "1.0.5"

[libraries]
octocatkdu = { module = "io.github.n7ghtm4r3:octocatkdu", version.ref = "octocatkdu" }
```

### Gradle

Add the JitPack repository to your build file

- Add it in your root build.gradle at the end of repositories

    ```gradle
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
    ```

  #### Gradle (Kotlin)

    ```gradle
    repositories {
        ...
        maven("https://jitpack.io")
    }
    ```

- Add the dependency

    ```gradle
    dependencies {
        implementation 'io.github.n7ghtm4r3:octocatkdu:1.0.5'
    }
    ```

  #### Gradle (Kotlin)

    ```gradle
    dependencies {
        implementation("io.github.n7ghtm4r3:octocatkdu:1.0.5")
    }
    ```

  #### Gradle (version catalog)

    ```gradle
    dependencies {
        implementation(libs.octocatkdu)
    }
    ```

## Appearance (default application theme)

![UI appearance](https://github.com/N7ghtm4r3/OctocatKDU/blob/main/images/ui.png)

## Authors

- [@N7ghtm4r3](https://www.github.com/N7ghtm4r3)

## Support

If you need help using the library or encounter any problems or bugs, please contact us via the following links:

- Support via <a href="mailto:infotecknobitcompany@gmail.com">email</a>
- Support via <a href="https://github.com/N7ghtm4r3/OctocatKDU/issues/new">GitHub</a>

Thank you for your help!

## Badges

[![](https://img.shields.io/badge/Google_Play-414141?style=for-the-badge&logo=google-play&logoColor=white)](https://play.google.com/store/apps/developer?id=Tecknobit)
[![Twitter](https://img.shields.io/badge/Twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white)](https://twitter.com/tecknobit)

[![](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)

## Donations

If you want support project and developer

| Crypto                                                                                              | Address                                          | Network  |
|-----------------------------------------------------------------------------------------------------|--------------------------------------------------|----------|
| ![](https://img.shields.io/badge/Bitcoin-000000?style=for-the-badge&logo=bitcoin&logoColor=white)   | **3H3jyCzcRmnxroHthuXh22GXXSmizin2yp**           | Bitcoin  |
| ![](https://img.shields.io/badge/Ethereum-3C3C3D?style=for-the-badge&logo=Ethereum&logoColor=white) | **0x1b45bc41efeb3ed655b078f95086f25fc83345c4**   | Ethereum |
| ![](https://img.shields.io/badge/Solana-000?style=for-the-badge&logo=Solana&logoColor=9945FF)       | **AtPjUnxYFHw3a6Si9HinQtyPTqsdbfdKX3dJ1xiDjbrL** | Solana   |

If you want support project and developer with <a href="https://www.paypal.com/donate/?hosted_button_id=5QMN5UQH7LDT4">PayPal</a>

Copyright © 2024 Tecknobit
