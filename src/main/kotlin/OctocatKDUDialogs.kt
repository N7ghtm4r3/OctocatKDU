
import FrequencyVisibility.ALWAYS
import KDUWorker.logError
import KDUWorker.logMessage
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.m3.Markdown
import com.tecknobit.apimanager.annotations.Wrapper
import com.tecknobit.apimanager.apis.ConsolePainter.ANSIColor.GREEN
import com.tecknobit.apimanager.apis.ConsolePainter.ANSIColor.YELLOW
import com.tecknobit.apimanager.formatters.TimeFormatter
import com.tecknobit.apimanager.formatters.TimeFormatter.DEFAULT_PATTERN
import com.tecknobit.octocatkdu.generated.resources.*
import com.tecknobit.octocatkdu.generated.resources.Res.string
import org.jetbrains.compose.resources.stringResource
import java.util.*
import java.util.concurrent.TimeUnit.DAYS
import kotlin.system.exitProcess

/**
 * **last_version** -> last version text
 */
@Deprecated(
    message = "This will be removed in the next version",
    level = DeprecationLevel.WARNING
)
private const val last_depr_version = "last_version"

/**
 * **kduExhibitor** -> the helper to manage the dialog whether it can be shown or not shown
 */
@Deprecated(
    message = "This will be removed in the next version",
    level = DeprecationLevel.WARNING
)
private val kduExhibitor = KDUExhibitor()

/**
 * **no_update_log_key** -> log text used in the [FakeUpdaterDialog] when the no update button is clicked
 */
private const val no_update_log_key = "Simulation not updating simulated correctly"

/**
 * **installation_message_key** -> log text used in the [FakeUpdaterDialog] when the new version has been installed
 */
private const val installation_message_key = "Installation simulated successfully!"

/**
 * **dismiss_update_log_key** -> log text used in the [FakeUpdaterDialog] when the dismiss button is clicked
 */
private const val dismiss_update_log_key = "Simulated update canceled successfully..."

/**
 * **last_version** -> last version text
 */
const val last_version = "last_version"

/**
 * The temporal frequency when display the dialog when there is an update available
 *
 * @param gap: the gap value used to check if the dialog can be displayed due to exceed of
 * the frequency visibility chosen
 *
 * @since 1.0.4
 */
enum class FrequencyVisibility(
    val gap: Long
) {

    /**
     * **ALWAYS** -> the dialog will always be displayed at every launch
     */
    ALWAYS(
        gap = 0
    ),

    /**
     * **ONCE_PER_DAY** -> the dialog will be displayed only once per day
     */
    ONCE_PER_DAY(
        gap = DAYS.toMillis(1)
    ),

    /**
     * **ONCE_PER_WEEK** -> the dialog will be displayed only once per week
     */
    ONCE_PER_WEEK(
        gap = DAYS.toMillis(7)
    ),

    /**
     * **ONCE_PER_MONTH** -> the dialog will be displayed only once per month (considering 30 days value)
     */
    ONCE_PER_MONTH(
        gap = DAYS.toMillis(30)
    )

}

/**
 * Set of configurations for the [FakeUpdaterDialog]
 *
 * @param locale: the locale language to use
 * @param notShowAtNextLaunchOptionEnabled: whether enable the option for the user to not display, so not be warned about a new
 * update, at the next launches
 * @param appName: the name of the application where the dialog will be shown
 * @param onUpdateAvailable: the action to execute if there is an update available and the dialog is shown
 * @param releaseNotes: the notes of the release
 * @param dismissAction: the action to execute when the dismiss button has been clicked
 * @param confirmAction: the action to execute when the confirm button has been clicked
 *
 * @since 1.0.4
 */
open class OctocatKDUFakeConfig(
    val locale: Locale = Locale.getDefault(),
    val notShowAtNextLaunchOptionEnabled: Boolean = false,
    val appName: String,
    var newVersion: String? = last_version,
    val onUpdateAvailable: () -> Unit,
    var releaseNotes: String? = null,
    var dismissAction: () -> Unit = {},
    var confirmAction: (Boolean) -> Unit = {},
)

/**
 * Set of configurations for the [UpdaterDialog]
 *
 * @param frequencyVisibility: the temporal frequency to display or not the dialog (also when there is an update available)
 * @param locale: the locale language to use
 * @param notShowAtNextLaunchOptionEnabled: whether enable the option for the user to not display, so not be warned about a new
 * update, at the next launches
 * @param appName: the name of the application where the dialog will be shown
 * @param currentVersion: the current version of the application, it will be used to check if is currently the latest
 * version available
 * @param onUpdateAvailable: the action to execute if there is an update available and the dialog is shown
 * @param dismissAction: the action to execute when the dismiss button has been clicked
 * @param confirmAction: the action to execute when the confirm button has been clicked
 *
 * @since 1.0.4
 *
 * @see OctocatKDUFakeConfig
 */
class OctocatKDUConfig(
    val frequencyVisibility: FrequencyVisibility = ALWAYS,
    locale: Locale = Locale.getDefault(),
    notShowAtNextLaunchOptionEnabled: Boolean = false,
    appName: String,
    val currentVersion: String,
    onUpdateAvailable: () -> Unit,
    dismissAction: () -> Unit = {},
    confirmAction: (Boolean) -> Unit = {},
) : OctocatKDUFakeConfig(
    locale = locale,
    notShowAtNextLaunchOptionEnabled = notShowAtNextLaunchOptionEnabled,
    appName = appName,
    newVersion = null,
    onUpdateAvailable = onUpdateAvailable,
    releaseNotes = null,
    dismissAction = dismissAction,
    confirmAction = confirmAction
)

/**
 * Set of style configurations for the [KDUDialog]
 *
 * @param dialogModifier: the [Modifier] for the [AlertDialog] shown
 * @param shape: the shape of the [AlertDialog]
 * @param titleModifier: the dialogModifier for the title of the [AlertDialog]
 * @param titleColor: the color of the title of the [AlertDialog]
 * @param titleFontSize: the font size for the title of the [AlertDialog]
 * @param titleFontStyle: the font style for the title of the [AlertDialog]
 * @param titleFontWeight: the font weight for the title of the [AlertDialog]
 * @param titleFontFamily: the font family for the title of the [AlertDialog]
 * @param textModifier: the dialogModifier for the text of the [AlertDialog]
 * @param textColor: the color of the text of the [AlertDialog]
 * @param textFontSize: the font size for the text of the [AlertDialog]
 * @param textFontStyle: the font style for the text of the [AlertDialog]
 * @param textFontWeight: the font weight for the text of the [AlertDialog]
 * @param textFontFamily: the font family for the text of the [AlertDialog]
 *
 * @since 1.0.4
 */
data class OctocatKDUStyle(
    val dialogModifier: Modifier = Modifier
        .heightIn(
            max = 500.dp
        ),
    val shape: Shape = RoundedCornerShape(
        size = 15.dp
    ),
    val titleModifier: Modifier = Modifier,
    val titleColor: Color = Color.Unspecified,
    val titleFontSize: TextUnit = 18.sp,
    val titleFontStyle: FontStyle? = null,
    val titleFontWeight: FontWeight? = null,
    val titleFontFamily: FontFamily? = null,
    val textModifier: Modifier = Modifier,
    val textColor: Color = Color.Unspecified,
    val textFontSize: TextUnit = 16.sp,
    val textFontStyle: FontStyle? = null,
    val textFontWeight: FontWeight? = null,
    val textFontFamily: FontFamily? = null
)

/**
 * Function to create the fake update dialog for testing purposes
 *
 * @param dialogModifier: the [Modifier] for the [AlertDialog] shown
 * @param locale: the locale language to use
 * @param shape: the shape of the [AlertDialog]
 * @param appName: the name of the application where the dialog will be shown
 * @param onUpdateAvailable: the action to execute if there is an update available and the dialog is shown
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
 * @param releaseNotes: the notes of the release
 * @param notShowAtNextLaunchOptionEnabled: whether enable the option for the user to not display, so not be warned about a new
 * update, at the next launches
 * @param dismissAction: the action to execute when the dismiss button has been clicked,
 * note this action will be invoked also if the dialog are not displayed
 */
@Wrapper
@Composable
@Deprecated(
    message = "This will be removed in the next version",
    replaceWith = ReplaceWith(
        expression = "[this.FakeUpdaterDialog(showFakeUpdate, config, style)]"
    ),
    level = DeprecationLevel.WARNING
)
fun FakeUpdaterDialog(
    dialogModifier: Modifier = Modifier
        .heightIn(
            max = 500.dp
        ),
    locale: Locale = Locale.getDefault(),
    shape: Shape = RoundedCornerShape(15.dp),
    appName: String,
    onUpdateAvailable: () -> Unit,
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
    textFontFamily: FontFamily? = null,
    releaseNotes: String? = null,
    notShowAtNextLaunchOptionEnabled: Boolean = false,
    dismissAction: () -> Unit
) {
    var timer = Timer()
    val isInstalling = remember { mutableStateOf(false) }
    if(showFakeUpdate) {
        KDUDialog(
            isReal = false,
            dialogModifier = dialogModifier,
            locale = locale,
            shape = shape,
            isInstalling = isInstalling,
            newVersion = last_depr_version,
            appName = appName,
            onUpdateAvailable = onUpdateAvailable,
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
            releaseNotes = releaseNotes,
            notShowAtNextLaunchOptionEnabled = notShowAtNextLaunchOptionEnabled,
            dismissAction = {
                logMessage(
                    no_update_log_key,
                    YELLOW
                )
                dismissAction.invoke()
            },
            confirmAction = {
                if(!isInstalling.value) {
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            logMessage(
                                installation_message_key,
                                GREEN
                            )
                            exitProcess(0)
                        }
                    }, 5000)
                } else {
                    logError(dismiss_update_log_key)
                    timer.cancel()
                    timer = Timer()
                }
            },
        )
    } else
        dismissAction.invoke()
}

/**
 * Function to create the update dialog to update the application to the latest release
 *
 * @param dialogModifier: the [Modifier] for the [AlertDialog] shown
 * @param locale: the locale language to use
 * @param shape: the shape of the [AlertDialog]
 * @param appName: the name of the application where the dialog will be shown
 * @param onUpdateAvailable: the action to execute if there is an update available and the dialog is shown
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
 * @param notShowAtNextLaunchOptionEnabled: whether enable the option for the user to not display, so not be warned about a new
 * update, at the next launches
 * @param dismissAction: the action to execute when the dismiss button has been clicked,
 * note this action will be invoked also if the dialog are not displayed
 */
@Wrapper
@Composable
@Deprecated(
    message = "This will be removed in the next version",
    replaceWith = ReplaceWith(
        expression = "[this.UpdaterDialog(config, style)]"
    ),
    level = DeprecationLevel.WARNING
)
fun UpdaterDialog(
    dialogModifier: Modifier = Modifier
        .heightIn(
            max = 500.dp
        ),
    locale: Locale = Locale.getDefault(),
    shape: Shape = RoundedCornerShape(15.dp),
    appName: String,
    currentVersion: String,
    onUpdateAvailable: () -> Unit,
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
    notShowAtNextLaunchOptionEnabled: Boolean = false,
    dismissAction: () -> Unit = {}
) {
    if(kduExhibitor.canDisplayDialog(false)) {
        val kduWorker = KDUWorker(appName)
        val isInstalling = remember { mutableStateOf(false) }
        if(kduWorker.canBeUpdated(currentVersion)) {
            kduExhibitor.refreshDisplayedTime()
            KDUDialog(
                dialogModifier = dialogModifier,
                locale = locale,
                shape = shape,
                isInstalling = isInstalling,
                newVersion = kduWorker.lastVersionCode,
                appName = appName,
                onUpdateAvailable = onUpdateAvailable,
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
                releaseNotes = kduWorker.releaseNotes,
                notShowAtNextLaunchOptionEnabled = notShowAtNextLaunchOptionEnabled,
                dismissAction = dismissAction,
                confirmAction = {
                    if(!isInstalling.value)
                        kduWorker.installNewVersion()
                    else
                        kduWorker.stopInstallation()
                },
            )
        } else
            dismissAction.invoke()
        TimeFormatter.changeDefaultPattern(DEFAULT_PATTERN)
    }
}

/**
 * Function to create the update dialog to update the application to the latest release
 *
 * @param isReal: whether the dialog is the [FakeUpdaterDialog] or the [UpdaterDialog]
 * @param dialogModifier: the [Modifier] for the [AlertDialog] shown
 * @param locale: the locale language to use
 * @param shape: the shape of the [AlertDialog]
 * @param isInstalling: the current status of the dialog
 * @param appName: the name of the application where the dialog will be shown
 * @param onUpdateAvailable: the action to execute if there is an update available and the dialog is shown
 * @param titleModifier: the dialogModifier for the title of the [AlertDialog]
 * @param titleColor: the color of the title of the [AlertDialog]
 * @param titleFontSize: the font size for the title of the [AlertDialog]
 * @param titleFontStyle: the font style for the title of the [AlertDialog]
 * @param titleFontWeight: the font weight for the title of the [AlertDialog]
 * @param titleFontFamily: the font family for the title of the [AlertDialog]
 * @param textModifier: the dialogModifier for the text of the [AlertDialog]
 * @param textColor: the color of the text of the [AlertDialog]
 * @param textFontSize: the font size for the text of the [AlertDialog]
 * @param textFontStyle: the font style for the text of the [AlertDialog]
 * @param textFontWeight: the font weight for the text of the [AlertDialog]
 * @param textFontFamily: the font family for the text of the [AlertDialog]
 * @param releaseNotes: the notes of the release
 * @param notShowAtNextLaunchOptionEnabled: whether enable the option for the user to not display, so not be warned about a new
 * update, at the next launches
 * @param dismissAction: the action to execute when the dismiss button has been clicked
 * @param confirmAction: the action to execute when the confirm button has been clicked
 */
@Composable
@Deprecated(
    message = "This will be removed in the next version",
    replaceWith = ReplaceWith(
        expression = "[this.KDUDialog(isReal, isInstalling, kduExhibitor, config, style)]"
    ),
    level = DeprecationLevel.WARNING
)
private fun KDUDialog(
    isReal: Boolean = true,
    dialogModifier: Modifier,
    locale: Locale = Locale.getDefault(),
    shape: Shape,
    isInstalling: MutableState<Boolean>,
    newVersion: String,
    appName: String,
    onUpdateAvailable: () -> Unit,
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
    releaseNotes: String? = null,
    notShowAtNextLaunchOptionEnabled: Boolean = false,
    dismissAction: () -> Unit = {},
    confirmAction: () -> Unit = {},
) {
    val currentDefaultLocale = Locale.getDefault()
    var show by remember { mutableStateOf(true) }
    if(show) {
        Locale.setDefault(locale)
        onUpdateAvailable.invoke()
        AlertDialog(
            modifier = dialogModifier,
            onDismissRequest = {
                if(!isInstalling.value)
                    show = false
            },
            shape = shape,
            title = {
                Text(
                    modifier = titleModifier,
                    text = "${stringResource(string.title_key)} $newVersion!",
                    color = titleColor,
                    fontSize = titleFontSize,
                    fontStyle = titleFontStyle,
                    fontWeight = titleFontWeight,
                    fontFamily = titleFontFamily
                )
            },
            text = {
                var hideDialog by remember { mutableStateOf(!kduExhibitor.canDisplayDialog(!isReal)) }
                Column {
                    if(isInstalling.value) {
                        Text(
                            text = stringResource(string.installing_executable_text_key)
                        )
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 10.dp
                                )
                        )
                    } else {
                        Text(
                            modifier = textModifier,
                            text = "${stringResource(string.text_part_one_key)} $appName " +
                                    stringResource(string.text_part_two_key),
                            color = textColor,
                            fontSize = textFontSize,
                            fontStyle = textFontStyle,
                            fontWeight = textFontWeight,
                            fontFamily = textFontFamily
                        )
                        if(releaseNotes != null) {
                            Text(
                                modifier = Modifier
                                    .padding(
                                        top = 5.dp
                                    ),
                                text = stringResource(string.release_notes),
                                fontSize = 20.sp
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(
                                        top = 5.dp
                                    )
                            )
                            Markdown(
                                modifier = Modifier
                                    .padding(
                                        top = 5.dp,
                                        bottom = 10.dp
                                    )
                                    .fillMaxHeight(
                                        if(notShowAtNextLaunchOptionEnabled)
                                            .9f
                                        else
                                            1f
                                    )
                                    .verticalScroll(rememberScrollState()),
                                content = releaseNotes
                            )
                        }
                        if(notShowAtNextLaunchOptionEnabled) {
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Checkbox(
                                    checked = hideDialog,
                                    onCheckedChange = {
                                        hideDialog = it
                                        if(isReal)
                                            kduExhibitor.notShowAtNextLaunch(it)
                                    }
                                )
                                Text(
                                    text = stringResource(string.not_show_at_next_launch)
                                )
                            }
                        }
                    }
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
                        Text(
                            text = stringResource(string.no_update_key)
                        )
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
                            stringResource(string.update_key)
                        else
                            stringResource(string.dismiss_key)
                    )
                }
            }
        )
    } else
        Locale.setDefault(currentDefaultLocale)
}

/**
 * Function to create the fake update dialog for testing purposes
 *
 * @param showFakeUpdate: whether show the dialog
 * @param config: the configuration to use for the dialog
 * @param style: the style configuration to use for the dialog
 *
 * @since 1.0.4
 */
@Wrapper
@Composable
fun FakeUpdaterDialog(
    showFakeUpdate: Boolean = true,
    config: OctocatKDUFakeConfig,
    style: OctocatKDUStyle = OctocatKDUStyle()
) {
    var timer = Timer()
    val isInstalling = remember { mutableStateOf(false) }
    if(showFakeUpdate) {
        val originalDismissAction = config.dismissAction
        config.dismissAction = {
            logMessage(
                no_update_log_key,
                YELLOW
            )
            originalDismissAction.invoke()
        }
        val originalConfirmAction = config.confirmAction
        config.confirmAction = {
            if(!isInstalling.value) {
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        logMessage(
                            installation_message_key,
                            GREEN
                        )
                        exitProcess(0)
                    }
                }, 5000)
            } else {
                logError(dismiss_update_log_key)
                timer.cancel()
                timer = Timer()
            }
            originalConfirmAction.invoke(isInstalling.value)
        }
        KDUDialog(
            isReal = false,
            isInstalling = isInstalling,
            config = config,
            style = style
        )
    } else
        config.dismissAction()
}

/**
 * Function to create the update dialog to update the application to the latest release
 *
 * @param config: the configuration to use for the dialog
 * @param style: the style configuration to use for the dialog
 *
 * @since 1.0.4
 */
@Wrapper
@Composable
fun UpdaterDialog(
    config: OctocatKDUConfig,
    style: OctocatKDUStyle = OctocatKDUStyle(),
) {
    val kduExhibitor = KDUExhibitor(
        config.frequencyVisibility
    )
    if(kduExhibitor.canDisplayDialog(false)) {
        val kduWorker = KDUWorker(config.appName)
        val isInstalling = remember { mutableStateOf(false) }
        if(kduWorker.canBeUpdated(config.currentVersion)) {
            kduExhibitor.refreshDisplayedTime()
            config.releaseNotes = kduWorker.releaseNotes
            config.newVersion = kduWorker.lastVersionCode
            val originalConfirmAction = config.confirmAction
            config.confirmAction = {
                if(!isInstalling.value)
                    kduWorker.installNewVersion()
                else
                    kduWorker.stopInstallation()
                originalConfirmAction.invoke(isInstalling.value)
            }
            KDUDialog(
                isInstalling = isInstalling,
                kduExhibitor = kduExhibitor,
                config = config,
                style = style
            )
        } else
            config.dismissAction()
        TimeFormatter.changeDefaultPattern(DEFAULT_PATTERN)
    }
}

/**
 * Function to create the update dialog to update the application to the latest release
 *
 * @param isReal: whether the dialog is the [FakeUpdaterDialog] or the [UpdaterDialog]
 * @param isInstalling: the current status of the dialog
 * @param kduExhibitor: the helper to manage the display usage of the dialog
 * @param config: the configuration to use for the dialog
 * @param style: the style configuration to use for the dialog
 *
 * @since 1.0.4
 */
@Composable
private fun KDUDialog(
    isReal: Boolean = true,
    isInstalling: MutableState<Boolean>,
    kduExhibitor: KDUExhibitor = KDUExhibitor(),
    config: OctocatKDUFakeConfig,
    style: OctocatKDUStyle
) {
    val currentDefaultLocale = Locale.getDefault()
    val notShowAtNextLaunchOptionEnabled: Boolean = config.notShowAtNextLaunchOptionEnabled
    var show by remember { mutableStateOf(true) }
    if(show) {
        Locale.setDefault(config.locale)
        config.onUpdateAvailable()
        AlertDialog(
            modifier = style.dialogModifier,
            onDismissRequest = {
                if(!isInstalling.value)
                    show = false
            },
            shape = style.shape,
            title = {
                Text(
                    modifier = style.titleModifier,
                    text = "${stringResource(string.title_key)} ${config.newVersion}!",
                    color = style.titleColor,
                    fontSize = style.titleFontSize,
                    fontStyle = style.titleFontStyle,
                    fontWeight = style.titleFontWeight,
                    fontFamily = style.titleFontFamily
                )
            },
            text = {
                var hideDialog by remember {
                    mutableStateOf(!kduExhibitor.canDisplayDialog(!isReal))
                }
                Column {
                    if(isInstalling.value) {
                        Text(
                            text = stringResource(string.installing_executable_text_key)
                        )
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 10.dp
                                )
                        )
                    } else {
                        Text(
                            modifier = style.textModifier,
                            text = "${stringResource(string.text_part_one_key)} ${config.appName} " +
                                    stringResource(string.text_part_two_key),
                            color = style.textColor,
                            fontSize = style.textFontSize,
                            fontStyle = style.textFontStyle,
                            fontWeight = style.textFontWeight,
                            fontFamily = style.textFontFamily
                        )
                        if(config.releaseNotes != null) {
                            Text(
                                modifier = Modifier
                                    .padding(
                                        top = 5.dp
                                    ),
                                text = stringResource(string.release_notes),
                                fontSize = 20.sp
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(
                                        top = 5.dp
                                    )
                            )
                            Markdown(
                                modifier = Modifier
                                    .padding(
                                        top = 5.dp,
                                        bottom = 10.dp
                                    )
                                    .fillMaxHeight(
                                        if(notShowAtNextLaunchOptionEnabled)
                                            .9f
                                        else
                                            1f
                                    )
                                    .verticalScroll(rememberScrollState()),
                                content = config.releaseNotes!!
                            )
                        }
                        if(notShowAtNextLaunchOptionEnabled) {
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Checkbox(
                                    checked = hideDialog,
                                    onCheckedChange = {
                                        hideDialog = it
                                        if(isReal)
                                            kduExhibitor.notShowAtNextLaunch(it)
                                    }
                                )
                                Text(
                                    text = stringResource(string.not_show_at_next_launch)
                                )
                            }
                        }
                    }
                }
            },
            dismissButton = {
                if(!isInstalling.value) {
                    TextButton(
                        onClick = {
                            config.dismissAction()
                            show = false
                        }
                    ) {
                        Text(
                            text = stringResource(string.no_update_key)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        config.confirmAction(!isInstalling.value)
                        isInstalling.value = !isInstalling.value
                    }
                ) {
                    Text(
                        text = if(!isInstalling.value)
                            stringResource(string.update_key)
                        else
                            stringResource(string.dismiss_key)
                    )
                }
            }
        )
    } else
        Locale.setDefault(currentDefaultLocale)
}