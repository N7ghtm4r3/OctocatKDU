package com.tecknobit.octocatkdu

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.tecknobit.apimanager.annotations.Wrapper
import com.tecknobit.apimanager.apis.ConsolePainter.ANSIColor.GREEN
import com.tecknobit.apimanager.apis.ConsolePainter.ANSIColor.YELLOW
import com.tecknobit.octocatkdu.KDUWorker.logError
import com.tecknobit.octocatkdu.KDUWorker.logMessage
import com.tecknobit.octocatkdu.generated.resources.*
import com.tecknobit.octocatkdu.generated.resources.Res.string
import org.jetbrains.compose.resources.stringResource
import java.util.*
import kotlin.system.exitProcess

/**
 * `no_update_log_key` log text used in the [FakeUpdaterDialog] when the no update button is clicked
 */
private const val no_update_log_key = "Simulation not updating simulated correctly"

/**
 * `installation_message_key` log text used in the [FakeUpdaterDialog] when the new version has been installed
 */
private const val installation_message_key = "Installation simulated successfully!"

/**
 * `dismiss_update_log_key` log text used in the [FakeUpdaterDialog] when the dismiss button is clicked
 */
private const val dismiss_update_log_key = "Simulated update canceled successfully..."

/**
 * `last_version` last version text
 */
const val last_version = "last_version"

/**
 * Function to create the fake update dialog for testing purposes
 *
 * @param showFakeUpdate: whether show the dialog
 * @param config The configuration to use for the dialog
 * @param style The style configuration to use for the dialog
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
 * @param config The configuration to use for the dialog
 * @param style The style configuration to use for the dialog
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
    }
}

/**
 * Function to create the update dialog to update the application to the latest release
 *
 * @param isReal: whether the dialog is the [FakeUpdaterDialog] or the [UpdaterDialog]
 * @param isInstalling The current status of the dialog
 * @param kduExhibitor The helper to manage the display usage of the dialog
 * @param config The configuration to use for the dialog
 * @param style The style configuration to use for the dialog
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
    var show by remember { mutableStateOf(true) }
    if(show) {
        Locale.setDefault(config.locale)
        config.onUpdateAvailable()
        AlertDialog(
            modifier = style.dialogModifier,
            onDismissRequest = {
                if(!isInstalling.value) {
                    show = false
                    config.dismissAction.invoke()
                }
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
                AnimatedContent(
                    targetState = isInstalling.value
                ) { isInstalling ->
                    if(isInstalling)
                        InstallingContent()
                    else {
                        NotInstallingContent(
                            style = style,
                            config = config,
                            isReal = isReal,
                            kduExhibitor = kduExhibitor
                        )
                    }
                }
            },
            dismissButton = {
                AnimatedVisibility(
                    visible = !isInstalling.value
                ) {
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
                        modifier = Modifier
                            .animateContentSize(),
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

@Composable
private fun InstallingContent() {
    Column {
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
    }
}

@Composable
private fun NotInstallingContent(
    style: OctocatKDUStyle,
    config: OctocatKDUFakeConfig,
    isReal: Boolean,
    kduExhibitor: KDUExhibitor
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
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
        Box (
            modifier = Modifier
                .wrapContentHeight()
        ) {
            config.releaseNotes?.let { releaseNotes ->
                ReleaseNotes(
                    releaseNotes = releaseNotes
                )
            }
            if(config.notShowAtNextLaunchOptionEnabled) {
                NotShowAtNextLaunchCheckBox(
                    kduExhibitor = kduExhibitor,
                    isReal = isReal
                )
            }
        }
    }
}

@Composable
private fun ReleaseNotes(
    releaseNotes: String
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = stringResource(string.release_notes),
            fontSize = 20.sp
        )
        HorizontalDivider()
        val state = rememberRichTextState()
        state.setMarkdown(releaseNotes)
        state.config.linkColor = MaterialTheme.colorScheme.primary
        RichText(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            state = state
        )
    }
}

@Composable
private fun BoxScope.NotShowAtNextLaunchCheckBox(
    kduExhibitor: KDUExhibitor,
    isReal: Boolean
) {
    var hideDialog by remember { mutableStateOf(!kduExhibitor.canDisplayDialog(!isReal)) }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
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