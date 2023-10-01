package com.gustavoeliseu.pokedex.utils.extensions

import com.gustavoeliseu.pokedex.utils.SafeCrashlyticsUtil


fun String.getPokemonIdFromUrl(): Int {
    return try {
        this.substringAfter("pokemon/")
            .substringBefore("/")
            .toInt()
    } catch (e: Exception) {
        SafeCrashlyticsUtil.logException(e)
        return -1
    }
}