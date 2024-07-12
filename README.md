# OctocatKDU

**v1.0.4**

Kotlin Desktop Updater based on GitHub releases. From the GitHub's repository of the application get the release marked as the last-release to warn the user of that application about a new version available

## Implementation

Add the JitPack repository to your build file

### Gradle

- Add it in your root build.gradle at the end of repositories

  #### Gradle (Short)

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

  #### Gradle (Short)

    ```gradle
    dependencies {
        implementation 'com.github.N7ghtm4r3:OctocatKDU:1.0.4'
    }
    ```

  #### Gradle (Kotlin)

    ```gradle
    dependencies {
        implementation("com.github.N7ghtm4r3:OctocatKDU:1.0.4")
    }
    ```

### Maven

- Add it in your root build.gradle at the end of repositories

```xml

<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

- Add the dependency

```xml

<dependency>
  <groupId>com.github.N7ghtm4r3</groupId>
  <artifactId>OctocatKDU</artifactId>
  <version>1.0.4</version>
</dependency>
```
### Latest supported property configuration

``` properties
kotlin.version=1.9.20
compose.version=1.6.0
```

### Usage/Examples

#### Resources file

You must place the resource file, named **octocat_kdu.config**, in the resources folder of your application:

> [!CAUTION]  
> Note: you must keep this file safe and not share in public repositories, but keep in local to be included only in the 
> executable file, because it contains sensitive data like the GitHub's personal access token 

``` bash
src
|-- main
    |-- resources
    |   |-- octocat_kdu.config
```

Content example:

```json
{
  "personal_access_token": "your_personal_access_token_for_github",
  "owner": "owner_of_the_repository_of_the_application",
  "repo": "the_repository_of_the_application"
}
```

#### Testing purposes

Use the fake updater dialog to testing the workflow of your application with the OctocatKDU

```kotlin
FakeUpdaterDialog(
  appName = "MyApplication",
  onUpdateAvailable = {
    // the application flow when there is an update available and the dialog is displayed
  },
  dismissAction = {
      // the rest of the application flow
  }
)
```

#### Real workflow

Use the real updater dialog in the release

```kotlin
UpdaterDialog(
  appName = "MyApplication",
  currentVersion = "current_version_of_the_application",
  onUpdateAvailable = {
    // the application flow when there is an update available and the dialog is displayed
  },
  dismissAction = {
    // the rest of the application flow
  }
)
```

#### Customization

The customization can be both with the faker and with the real dialog

- Application theme

  Customize the dialog for your application theme 
  
  ```kotlin
  MyApplicationTheme {
    FakeUpdaterDialog(
      appName = "MyApplication",
      onUpdateAvailable = {
        // the application flow when there is an update available and the dialog is displayed
      },
      dismissAction = {
          // the rest of the application flow
      }
    )
  }
  ```
  
- Specific for the dialog
  
  Customize only the dialog theme

  ```kotlin
  FakeUpdaterDialog(
    locale = // the locale language to use",
    shape = // the shape for the dialog,
    titleModifier = // the modifier for the title of the dialog, 
    titleColor = // the color for the title of the dialog,
    titleFontSize = // the size for the title of the dialog,
    titleFontStyle = // the style for the title of the dialog,
    titleFontWeight = // the weight for the title of the dialog,
    titleFontFamily = // the font family for the title of the dialog,
    textModifier = // the modifier for the text of the dialog,
    textColor = // the color for the text of the dialog,
    textFontSize = // the size for the text of the dialog,
    textFontStyle = // the style for the text of the dialog,
    textFontWeight = // the weight for the text of the dialog,
    textFontFamily = // the font family for the text of the dialog,
  )
  ```

#### Custom options that can be activated

- `not_show_at_next_launch`
  ```kotlin
  FakeUpdaterDialog(
    ...
    // allow the user to avoid to be warned about new updates available hiding the dialog
    notShowAtNextLaunchOptionEnabled = false / true
    ...
  )
  ```

#### Release distribution

> [!IMPORTANT]  
>  When you need to create the release distribution you must insert in the **compose-desktop.pro** file this proguard setting
>  to correctly run the distribution:
> 
> ```txt 
> -keepclassmembers enum * { 
>    public static **[] values();
>    public static ** valueOf(java.lang.String);
> }
> ```

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

[![](https://jitpack.io/v/N7ghtm4r3/OctocatKDU.svg)](https://jitpack.io/#N7ghtm4r3/OctocatKDU)

## Donations

If you want support project and developer

| Crypto                                                                                              | Address                                        | Network  |
|-----------------------------------------------------------------------------------------------------|------------------------------------------------|----------|
| ![](https://img.shields.io/badge/Bitcoin-000000?style=for-the-badge&logo=bitcoin&logoColor=white)   | **3H3jyCzcRmnxroHthuXh22GXXSmizin2yp**         | Bitcoin  |
| ![](https://img.shields.io/badge/Ethereum-3C3C3D?style=for-the-badge&logo=Ethereum&logoColor=white) | **0x1b45bc41efeb3ed655b078f95086f25fc83345c4** | Ethereum |

If you want support project and developer with <a href="https://www.paypal.com/donate/?hosted_button_id=5QMN5UQH7LDT4">PayPal</a>

Copyright © 2024 Tecknobit
