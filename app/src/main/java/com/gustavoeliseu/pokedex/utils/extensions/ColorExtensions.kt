package com.gustavoeliseu.pokedex.utils.extensions

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.Swatch
import java.util.Collections
import androidx.compose.ui.graphics.Color as ComposeColor


fun Int.isDarkColor(): Boolean {
    return ColorUtils.calculateLuminance(this) < 0.5
}

//TODO - CHECK IF STILL NEEDED WHEN POLISHING CARD LIST
fun getDominantSwatch(palette: Palette?): Swatch? {
    // find most-represented swatch based on population
    if (palette == null) return null
    return Collections.max(palette.swatches, object : Comparator<Swatch?> {
        override fun compare(p0: Swatch?, p1: Swatch?): Int {

            return Integer.compare(p0?.population ?: 0, p1?.population ?: 0)
        }
    })
}

fun Int.notBlackNorWhite(): Boolean {
    val luminance = Color.luminance(this)
    return !(luminance < 0.2 || luminance > 0.6)
}

//TODO - CHECK IF STILL NEEDED WHEN POLISHING CARD LIST
fun FloatArray.notTooDarkNorTooBright(brightnessThreshold: Float = 0.7f): Boolean {
    val lightness = this[2]
    val saturation = this[1]

    return !(saturation > 0.8 || saturation < 0.1) && !(lightness > brightnessThreshold || lightness < (1 - brightnessThreshold))
}

fun Int.isCloseTo(compared: Int?): Boolean {
    if (compared == null) return false
    val comparison = this - compared
    return (comparison in 0..30)
}

fun ComposeColor.colorDistance(color2: ComposeColor): Double {
    val deltaRed = (color2.red - this.red).toDouble()
    val deltaGreen = (color2.green - this.green).toDouble()
    val deltaBlue = (color2.blue - this.blue).toDouble()

    return Math.sqrt(deltaRed * deltaRed + deltaGreen * deltaGreen + deltaBlue * deltaBlue)
}