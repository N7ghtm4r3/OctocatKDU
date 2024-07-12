
import FrequencyVisibility.ALWAYS
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*
import java.util.concurrent.TimeUnit.DAYS

/**
 * **last_version** -> last version text
 */
const val last_version = "last_version"

//TODO: TO COMMENT
enum class FrequencyVisibility(
    val gap: Long
) {

    ALWAYS(
        gap = 0
    ),

    ONCE_PER_DAY(
        gap = DAYS.toMillis(1)
    ),

    ONCE_PER_WEEK(
        gap = DAYS.toMillis(7)
    ),

    ONCE_PER_MONTH(
        gap = DAYS.toMillis(30)
    )

}

/**
 *
 * @param locale: the locale language to use
 * @param notShowAtNextLaunchOptionEnabled: whether enable the option for the user to not display, so not be warned about a new
 * update, at the next launches
 * @param appName: the name of the application where the dialog will be shown
 * @param onUpdateAvailable: the action to execute if there is an update available and the dialog is shown
 * @param releaseNotes: the notes of the release
 * @param dismissAction: the action to execute when the dismiss button has been clicked
 * @param confirmAction: the action to execute when the confirm button has been clicked
 */
//TODO: TO COMMENT
data class OctocatKDUConfig(
    val frequencyVisibility: FrequencyVisibility = ALWAYS,
    val locale: Locale = Locale.getDefault(),
    val notShowAtNextLaunchOptionEnabled: Boolean = false,
    val appName: String,
    val newVersion: String = last_version,
    val onUpdateAvailable: () -> Unit,
    val releaseNotes: String? = null,
    var dismissAction: () -> Unit = {},
    var confirmAction: () -> Unit = {},
)

/**
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
 */
//TODO: TO COMMENT
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