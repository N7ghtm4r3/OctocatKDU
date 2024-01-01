
import KDUWorker.logError
import KDUWorker.logMessage
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
import androidx.compose.ui.unit.sp
import com.tecknobit.apimanager.apis.ConsolePainter.ANSIColor.GREEN
import com.tecknobit.apimanager.apis.ConsolePainter.ANSIColor.YELLOW
import com.tecknobit.mantis.Mantis
import java.util.*
import kotlin.system.exitProcess

@Composable
fun FakeUpdaterDialog(
    locale: Locale = Locale.getDefault(),
    appName: String,
    showFakeUpdate: Boolean = true,
    titleModifier: Modifier = Modifier,
    titleColor: Color = Color.Unspecified,
    titleFontSize: TextUnit = 18.sp,
    titleFontStyle: FontStyle? = null,
    titleFontWeight: FontWeight? = null,
    titleFontFamily: FontFamily? = null,
    textModifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified,
    textFontSize: TextUnit = 16.sp,
    textFontStyle: FontStyle? = null,
    textFontWeight: FontWeight? = null,
    textFontFamily: FontFamily? = null
) {
    val mantis = Mantis(locale)
    var timer = Timer()
    val isInstalling = remember { mutableStateOf(false) }
    if(showFakeUpdate) {
        KDUDialog(
            locale = locale,
            isInstalling = isInstalling,
            title = "${mantis.getResource("title_key")} last_version!",
            appName = appName,
            titleModifier = titleModifier,
            titleColor = titleColor,
            titleFontSize = titleFontSize,
            titleFontStyle = titleFontStyle,
            titleFontWeight = titleFontWeight,
            titleFontFamily = titleFontFamily,
            textModifier = textModifier,
            textColor = textColor,
            textFontSize = textFontSize,
            textFontStyle = textFontStyle,
            textFontWeight = textFontWeight,
            textFontFamily = textFontFamily,
            dismissAction = {
                logMessage(
                    mantis.getResource("no_update_log_key"),
                    YELLOW
                )
            },
            confirmAction = {
                if(!isInstalling.value) {
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            logMessage(
                                mantis.getResource("installation_message_key"),
                                GREEN
                            )
                            exitProcess(0)
                        }
                    }, 5000)
                } else {
                    logError(mantis.getResource("dismiss_update_log_key"))
                    timer.cancel()
                    timer = Timer()
                }
            },
        )
    }
}

@Composable
fun UpdaterDialog(
    locale: Locale = Locale.getDefault(),
    appName: String,
    currentVersion: String,
    titleModifier: Modifier = Modifier,
    titleColor: Color = Color.Unspecified,
    titleFontSize: TextUnit = 18.sp,
    titleFontStyle: FontStyle? = null,
    titleFontWeight: FontWeight? = null,
    titleFontFamily: FontFamily? = null,
    textModifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified,
    textFontSize: TextUnit = 16.sp,
    textFontStyle: FontStyle? = null,
    textFontWeight: FontWeight? = null,
    textFontFamily: FontFamily? = null,
    accessToken: String,
    owner: String,
    repo: String
) {
    val mantis = Mantis(locale)
    val kduWorker = KDUWorker(accessToken, owner, repo, appName)
    val isInstalling = remember { mutableStateOf(false) }
    if(kduWorker.canBeUpdated(currentVersion)) {
        KDUDialog(
            locale = locale,
            isInstalling = isInstalling,
            title = "${mantis.getResource("title_key")} ${kduWorker.lastVersionCode}!",
            appName = appName,
            titleModifier = titleModifier,
            titleColor = titleColor,
            titleFontSize = titleFontSize,
            titleFontStyle = titleFontStyle,
            titleFontWeight = titleFontWeight,
            titleFontFamily = titleFontFamily,
            textModifier = textModifier,
            textColor = textColor,
            textFontSize = textFontSize,
            textFontStyle = textFontStyle,
            textFontWeight = textFontWeight,
            textFontFamily = textFontFamily,
            confirmAction = {
                if(!isInstalling.value)
                    kduWorker.installNewVersion()
                else
                    kduWorker.stopInstallation()
            },
        )
    }
}

@Composable
private fun KDUDialog(
    locale: Locale = Locale.getDefault(),
    isInstalling: MutableState<Boolean>,
    title: String,
    appName: String,
    titleModifier: Modifier = Modifier,
    titleColor: Color = Color.Unspecified,
    titleFontSize: TextUnit = 18.sp,
    titleFontStyle: FontStyle? = null,
    titleFontWeight: FontWeight? = null,
    titleFontFamily: FontFamily? = null,
    textModifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified,
    textFontSize: TextUnit = 16.sp,
    textFontStyle: FontStyle? = null,
    textFontWeight: FontWeight? = null,
    textFontFamily: FontFamily? = null,
    dismissAction: () -> Unit = {},
    confirmAction: () -> Unit = {},
) {
    val mantis = Mantis(locale)
    var show by remember { mutableStateOf(true) }
    if(show) {
        AlertDialog(
            onDismissRequest = { show = false },
            title = {
                Text(
                    modifier = titleModifier,
                    text = title,
                    color = titleColor,
                    fontSize = titleFontSize,
                    fontStyle = titleFontStyle,
                    fontWeight = titleFontWeight,
                    fontFamily = titleFontFamily
                )
            },
            text = {
                if(isInstalling.value) {
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
                if(!isInstalling.value) {
                    TextButton(
                        onClick = {
                            dismissAction.invoke()
                            show = false
                        }
                    ) {
                        Text(mantis.getResource("no_update_key"))
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmAction.invoke()
                        isInstalling.value = !isInstalling.value
                    }
                ) {
                    Text(
                        text = if(!isInstalling.value)
                            mantis.getResource("update_key")
                        else
                            mantis.getResource("dismiss_key")
                    )
                }
            }
        )
    }
}