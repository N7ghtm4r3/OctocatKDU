
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.tecknobit.mantis.Mantis
import java.util.*

@Composable
fun UpdateDialog(
    locale: Locale = Locale.getDefault(),
    appName: String,
    currentVersion: String,
    titleModifier: Modifier = Modifier,
    titleColor: Color = Color.Unspecified,
    titleFontSize: TextUnit = TextUnit.Unspecified,
    titleFontStyle: FontStyle? = null,
    titleFontWeight: FontWeight? = null,
    titleFontFamily: FontFamily? = null,
    textModifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified,
    textFontSize: TextUnit = TextUnit.Unspecified,
    textFontStyle: FontStyle? = null,
    textFontWeight: FontWeight? = null,
    textFontFamily: FontFamily? = null,
    accessToken: String,
    owner: String,
    repo: String
) {
    val mantis = Mantis(locale)
    var show by remember { mutableStateOf(true) }
    val kduWorker = KDUWorker(accessToken, owner, repo, appName)
    var isInstalling by remember { mutableStateOf(false) }
    if(kduWorker.canBeUpdated(currentVersion)) {
        if(show) {
            AlertDialog(
                onDismissRequest = { show = false },
                title = {
                    Text(
                        modifier = titleModifier,
                        text = mantis.getResource("title_key"),
                        color = titleColor,
                        fontSize = titleFontSize,
                        fontStyle = titleFontStyle,
                        fontWeight = titleFontWeight,
                        fontFamily = titleFontFamily
                    )
                },
                text = {
                    if(isInstalling) {
                        Column (
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = mantis.getResource("installing_executable_text_key")
                            )
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
                        Text(
                            modifier = textModifier,
                            text = "${mantis.getResource("text_part_one_key")} $appName" +
                                    "${mantis.getResource("text_part_two_key")}",
                            color = textColor,
                            fontSize = textFontSize,
                            fontStyle = textFontStyle,
                            fontWeight = textFontWeight,
                            fontFamily = textFontFamily
                        )
                    }
                },
                dismissButton = {
                    if(!isInstalling) {
                        TextButton(
                            onClick = { show = false }
                        ) {
                            Text(mantis.getResource("no_update_key"))
                        }
                    }
                },
                confirmButton = {
                    if(!isInstalling) {
                        TextButton(
                            onClick = {
                                isInstalling = true
                                kduWorker.installNewVersion()
                            }
                        ) {
                            Text(mantis.getResource("update_key"))
                        }
                    }
                }
            )
        }
    }
}
