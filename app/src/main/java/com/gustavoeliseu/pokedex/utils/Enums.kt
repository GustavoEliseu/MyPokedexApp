package com.gustavoeliseu.pokedex.utils

import androidx.compose.ui.graphics.Color
import com.gustavoeliseu.pokedex.R


interface DefaultEnum {
    val nameId: Int
}

interface IValueEnum : DefaultEnum {
    val value: Int
}

interface IRefValueEnum : IValueEnum {
    val tintColor: Color
}

enum class ColorEnum(override val value: Int) : IRefValueEnum {
    //TODO - MANUALLY TEST EACH COLOR TO FIND THE BEST RESULT
    INVALID(-1) {
        override val nameId = R.string.dark_gray
        override val tintColor = Color(103, 103, 101)
    },
    BLACK(1) {
        override val tintColor = Color.Black
        override val nameId = R.string.black
    },
    BLUE(2) {
        override val tintColor = Color(46, 75, 190, 255)
        override val nameId = R.string.blue
    },
    BROWN(3) {
        override val tintColor = Color(163, 115, 61, 255)
        override val nameId = R.string.brown
    },
    GRAY(4) {
        override val tintColor = Color(197, 198, 199)
        override val nameId = R.string.app_name
    },
    GREEN(5) {
        override val tintColor = Color(65, 180, 84, 255)
        override val nameId = R.string.green
    },
    PINK(6) {
        override val tintColor = Color(251, 202, 224)
        override val nameId = R.string.pink
    },
    PURPLE(7) {
        override val tintColor = Color(90, 36, 123)
        override val nameId = R.string.purple
    },
    RED(8) {
        override val tintColor = Color(197, 40, 20, 255)
        override val nameId = R.string.red
    },
    WHITE(9) {
        override val tintColor = Color.White
        override val nameId = R.string.white
    },
    YELLOW(10) {
        override val tintColor = Color(229, 183, 35)
        override val nameId = R.string.yellow
    };

    companion object {
        private val map = values().associateBy(ColorEnum::value)
        fun fromInt(value: Int?): ColorEnum {
            return if (value != null) map[value] ?: INVALID else INVALID
        }
    }
}