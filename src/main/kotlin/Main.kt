
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.util.*

@Composable
@Preview
fun App() {
    MaterialTheme {
        UpdateDialog(
            locale = Locale.UK,
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
