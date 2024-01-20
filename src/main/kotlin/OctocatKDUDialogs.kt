
import KDUWorker.logError
import KDUWorker.logMessage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.apimanager.annotations.Wrapper
import com.tecknobit.apimanager.apis.ConsolePainter.ANSIColor.GREEN
import com.tecknobit.apimanager.apis.ConsolePainter.ANSIColor.YELLOW
import com.tecknobit.mantis.Mantis
import java.util.*
import kotlin.system.exitProcess

/**
 * Function to create the fake update dialog for testing purposes
 *
 * @param locale: the locale language to use
 * @param shape: the shape of the [AlertDialog]
 * @param appName: the name of the application where the dialog will be shown
 * @param showFakeUpdate: whether show the dialog
 * @param titleModifier: the modifier for the title of the [AlertDialog]
 * @param titleColor: the color of the title of the [AlertDialog]
 * @param titleFontSize: the font size for the title of the [AlertDialog]
 * @param titleFontStyle: the font style for the title of the [AlertDialog]
 * @param titleFontWeight: the font weight for the title of the [AlertDialog]
 * @param titleFontFamily: the font family for the title of the [AlertDialog]
 * @param textModifier: the modifier for the text of the [AlertDialog]
 * @param textColor: the color of the text of the [AlertDialog]
 * @param textFontSize: the font size for the text of the [AlertDialog]
 * @param textFontStyle: the font style for the text of the [AlertDialog]
 * @param textFontWeight: the font weight for the text of the [AlertDialog]
 * @param textFontFamily: the font family for the text of the [AlertDialog]
 */
@Wrapper
@Composable
fun FakeUpdaterDialog(
    locale: Locale = Locale.getDefault(),
    shape: Shape = RoundedCornerShape(15.dp),
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
            shape = shape,
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

/**
 * Function to create the update dialog to update the application to the latest release
 *
 * @param locale: the locale language to use
 * @param shape: the shape of the [AlertDialog]
 * @param appName: the name of the application where the dialog will be shown
 * @param currentVersion: the current version of the application, it will be used to check if is currently the latest
 * version available
 * @param titleModifier: the modifier for the title of the [AlertDialog]
 * @param titleColor: the color of the title of the [AlertDialog]
 * @param titleFontSize: the font size for the title of the [AlertDialog]
 * @param titleFontStyle: the font style for the title of the [AlertDialog]
 * @param titleFontWeight: the font weight for the title of the [AlertDialog]
 * @param titleFontFamily: the font family for the title of the [AlertDialog]
 * @param textModifier: the modifier for the text of the [AlertDialog]
 * @param textColor: the color of the text of the [AlertDialog]
 * @param textFontSize: the font size for the text of the [AlertDialog]
 * @param textFontStyle: the font style for the text of the [AlertDialog]
 * @param textFontWeight: the font weight for the text of the [AlertDialog]
 * @param textFontFamily: the font family for the text of the [AlertDialog]
 */
@Wrapper
@Composable
fun UpdaterDialog(
    locale: Locale = Locale.getDefault(),
    shape: Shape = RoundedCornerShape(15.dp),
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
    textFontFamily: FontFamily? = null
) {
    val mantis = Mantis(locale)
    val kduWorker = KDUWorker(appName)
    val isInstalling = remember { mutableStateOf(false) }
    if(kduWorker.canBeUpdated(currentVersion)) {
        KDUDialog(
            locale = locale,
            shape = shape,
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

/**
 * Function to create the update dialog to update the application to the latest release
 *
 * @param locale: the locale language to use
 * @param shape: the shape of the [AlertDialog]
 * @param isInstalling: the current status of the dialog
 * @param title: the title of the [AlertDialog]
 * @param appName: the name of the application where the dialog will be shown
 * @param titleModifier: the modifier for the title of the [AlertDialog]
 * @param titleColor: the color of the title of the [AlertDialog]
 * @param titleFontSize: the font size for the title of the [AlertDialog]
 * @param titleFontStyle: the font style for the title of the [AlertDialog]
 * @param titleFontWeight: the font weight for the title of the [AlertDialog]
 * @param titleFontFamily: the font family for the title of the [AlertDialog]
 * @param textModifier: the modifier for the text of the [AlertDialog]
 * @param textColor: the color of the text of the [AlertDialog]
 * @param textFontSize: the font size for the text of the [AlertDialog]
 * @param textFontStyle: the font style for the text of the [AlertDialog]
 * @param textFontWeight: the font weight for the text of the [AlertDialog]
 * @param textFontFamily: the font family for the text of the [AlertDialog]
 * @param dismissAction: the action to execute when the dismiss button has been clicked
 * @param confirmAction: the action to execute when the confirm button has been clicked
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun KDUDialog(
    locale: Locale = Locale.getDefault(),
    shape: Shape,
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
            shape = shape,
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