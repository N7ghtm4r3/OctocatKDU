package com.tecknobit.octocatkdu

import com.tecknobit.octocatkdu.FrequencyVisibility.ALWAYS
import java.util.*

/**
 * Set of configurations for the [FakeUpdaterDialog]
 *
 * @param locale The locale language to use
 * @param notShowAtNextLaunchOptionEnabled: whether enable the option for the user to not display, so not be warned about a new
 * update, at the next launches
 * @param appName The name of the application where the dialog will be shown
 * @param onUpdateAvailable The action to execute if there is an update available and the dialog is shown
 * @param releaseNotes The notes of the release
 * @param dismissAction The action to execute when the dismiss button has been clicked
 * @param confirmAction The action to execute when the confirm button has been clicked
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
 * @param frequencyVisibility The temporal frequency to display or not the dialog (also when there is an update available)
 * @param locale The locale language to use
 * @param notShowAtNextLaunchOptionEnabled: whether enable the option for the user to not display, so not be warned about a new
 * update, at the next launches
 * @param appName The name of the application where the dialog will be shown
 * @param currentVersion The current version of the application, it will be used to check if is currently the latest
 * version available
 * @param onUpdateAvailable The action to execute if there is an update available and the dialog is shown
 * @param dismissAction The action to execute when the dismiss button has been clicked
 * @param confirmAction The action to execute when the confirm button has been clicked
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