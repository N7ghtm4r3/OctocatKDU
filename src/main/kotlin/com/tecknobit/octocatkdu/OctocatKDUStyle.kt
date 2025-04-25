package com.tecknobit.octocatkdu

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

/**
 * Set of style configurations for the [KDUDialog]
 *
 * @param dialogModifier The [Modifier] for the [AlertDialog] shown
 * @param shape The shape of the [AlertDialog]
 * @param titleModifier The dialogModifier for the title of the [AlertDialog]
 * @param titleColor The color of the title of the [AlertDialog]
 * @param titleFontSize The font size for the title of the [AlertDialog]
 * @param titleFontStyle The font style for the title of the [AlertDialog]
 * @param titleFontWeight The font weight for the title of the [AlertDialog]
 * @param titleFontFamily The font family for the title of the [AlertDialog]
 * @param textModifier The dialogModifier for the text of the [AlertDialog]
 * @param textColor The color of the text of the [AlertDialog]
 * @param textFontSize The font size for the text of the [AlertDialog]
 * @param textFontStyle The font style for the text of the [AlertDialog]
 * @param textFontWeight The font weight for the text of the [AlertDialog]
 * @param textFontFamily The font family for the text of the [AlertDialog]
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