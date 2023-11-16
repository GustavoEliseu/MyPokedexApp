package com.gustavoeliseu.commonui.utils.extensions

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import com.gustavoeliseu.myapplication.R
import androidx.compose.ui.graphics.Color as ComposeColor


fun Int.isDarkColor(): Boolean {
    return ColorUtils.calculateLuminance(this) < 0.5
}

fun FloatArray.notTooDarkNorTooBright(): Boolean {
    val lightness = this[2]
    val saturation = this[1]
    R.drawable.caterpie_test
    return !(saturation > 0.95 || saturation < 0.05) && !(lightness > 0.95 || lightness < 0.05)
}

//TODO Test colors with this function from stack users pgras,blueraja and alp
//fun ComposeColor.colorDistance(c1: Color, c2: Color): Double {
//    val rmean: Float = (c1.red + c2.red) / 2
//    val r: Float = c1.red - c2.red
//    val g: Float = c1.green - c2.green
//    val b: Float = c1.blue - c2.blue
//    val weightR = 2 + rmean / 256
//    val weightG = 4.0
//    val weightB = 2 + (255 - rmean) / 256
//    return Math.sqrt(weightR * r * r + weightG * g * g + weightB * b * b)
//}

fun ComposeColor.colorDistance(color2: ComposeColor): Double {
    val deltaRed = (color2.red - this.red).toDouble()
    val deltaGreen = (color2.green - this.green).toDouble()
    val deltaBlue = (color2.blue - this.blue).toDouble()

    return Math.sqrt(deltaRed * deltaRed + deltaGreen * deltaGreen + deltaBlue * deltaBlue)
}

fun FloatArray.getPaletteFilterFromColor(color: Int, baseColor: Color): Boolean {
    val comparison = Color(
        color
    ).colorDistance(baseColor)
    return this.notTooDarkNorTooBright() && (comparison <= .5f)
}

fun Palette.Swatch?.isColorCloseAndPopulousEnough(baseColor: Color): Boolean {
    var vibrantDistance = -1.0
    var vibrantPopulation = -1
    if (this != null) {
        vibrantDistance = Color(this.rgb).colorDistance(baseColor)
        vibrantPopulation = this.population
    }
    return (this != null && vibrantDistance >= 0 && vibrantDistance > .4f && vibrantPopulation > 50)
}

fun Palette.chooseBestPalette(baseColor: Color, updateColors: (Color?, Color?, Boolean) -> Unit) {
    val isVibrantCloseEnough = this.vibrantSwatch.isColorCloseAndPopulousEnough(baseColor)
    val isLightVibrantCloseEnough = this.lightVibrantSwatch.isColorCloseAndPopulousEnough(baseColor)
    val resultSwatch =
        if (this.dominantSwatch != null && !isVibrantCloseEnough && !isLightVibrantCloseEnough) this.dominantSwatch else if (isVibrantCloseEnough) this.vibrantSwatch else this.lightVibrantSwatch
    if (resultSwatch != null) {
        updateColors(
            Color(resultSwatch.rgb),
            Color(resultSwatch.titleTextColor), false
        )
    } else {
        updateColors(
            null,
            null,
            false
        )
    }
}