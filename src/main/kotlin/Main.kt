
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.util.*

/**
 * `COLOR_PRIMARY_HEX` the primary color value as hex [String]
 */
const val COLOR_PRIMARY_HEX = "#1E1E8D"

/**
 * `COLOR_RED_HEX` the red color value as hex [String]
 */
const val COLOR_RED_HEX = "#A81515"

/**
 * `BACKGROUND_COLOR_HEX` the background color value as hex [String]
 */
const val BACKGROUND_COLOR_HEX = "#FAEDE1E1"

/**
 * the primary color value
 */
val primaryColor: Color = fromHexToColor(COLOR_PRIMARY_HEX)

/**
 * the background color value
 */
val backgroundColor: Color = fromHexToColor(BACKGROUND_COLOR_HEX)

/**
 * the red color value
 */
val redColor: Color = fromHexToColor(COLOR_RED_HEX)

/**
 * the green color value
 */
val greenColor: Color = fromHexToColor("#128c33")

/**
 * the red color value
 */
val cBackgroundColor: Color = fromHexToColor("#1b1ba6")

/**
 * Method to mainScreen a [Color] from an hex [String]
 * @param hex: hex value to transform
 *
 * @return color as [Color]
 */
fun fromHexToColor(hex: String): Color {
    return Color(("ff" + hex.removePrefix("#").lowercase()).toLong(16))
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        UpdateDialog(
            locale = Locale.UK,
            appName = "Pandoro",
            currentVersion = "1.0.0",
            accessToken = "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",
            owner = "N7ghtm4r3",
            repo = "Pandoro-Desktop"
        )
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
