package com.gustavoeliseu.commonui.utils.extensions

import androidx.core.graphics.ColorUtils
import androidx.compose.ui.graphics.Color as ComposeColor


fun Int.isDarkColor(): Boolean {
    return ColorUtils.calculateLuminance(this) < 0.5
}

fun FloatArray.notTooDarkNorTooBright(): Boolean {
    val lightness = this[2]
    val saturation = this[1]
    return !(saturation > 0.95 || saturation < 0.05) && !(lightness > 0.95 || lightness < 0.05)
}

fun ComposeColor.colorDistance(color2: ComposeColor): Double {
    val deltaRed = (color2.red - this.red).toDouble()
    val deltaGreen = (color2.green - this.green).toDouble()
    val deltaBlue = (color2.blue - this.blue).toDouble()

    return Math.sqrt(deltaRed * deltaRed + deltaGreen * deltaGreen + deltaBlue * deltaBlue)
}