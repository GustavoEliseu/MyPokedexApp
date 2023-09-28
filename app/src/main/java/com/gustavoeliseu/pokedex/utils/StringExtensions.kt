package com.gustavoeliseu.pokedex.utils

import java.lang.Exception


fun String.getPokemonIdFromUrl(): Int{
    return try {
        this.substringAfter("pokemon/")
            .substringBefore("/")
            .toInt()
    } catch (e: Exception){
        SafeCrashlyticsUtil.logException(e)
        return -1
    }
}