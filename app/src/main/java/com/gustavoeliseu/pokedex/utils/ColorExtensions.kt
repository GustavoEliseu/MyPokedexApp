package com.gustavoeliseu.pokedex.utils

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.Swatch
import java.util.Collections


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
    return !(luminance < 0.2 || luminance > 0.8)
}

//TODO - CHECK IF STILL NEEDED WHEN POLISHING CARD LIST
fun FloatArray.notTooDarkNorTooBright(): Boolean {
    val lightness = this[2]
    val saturation = this[1]

    return !(saturation > 0.95 || saturation < 0.05) && !(lightness > 0.95 || lightness < 0.05)
}

fun Int.isCloseTo(compared: Int?): Boolean {
    if (compared == null) return false
    val comparison = this - compared
    return (comparison in 0..30)
}