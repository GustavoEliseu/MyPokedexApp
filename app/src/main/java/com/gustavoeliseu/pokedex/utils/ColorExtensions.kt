package com.gustavoeliseu.pokedex.utils

import android.graphics.Color
import androidx.core.graphics.ColorUtils


fun Int.isDarkColor(): Boolean {
    return ColorUtils.calculateLuminance(this) < 0.5
}

fun Int.getContrastColor(): Int {
    val whiteContrast = ColorUtils.calculateContrast(Color.WHITE, this)
    val blackContrast = ColorUtils.calculateContrast(Color.BLACK, this)
    return if (whiteContrast > blackContrast) Color.WHITE else Color.BLACK
}
