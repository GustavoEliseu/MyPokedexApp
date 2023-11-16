package com.gustavoeliseu.domain.utils

import androidx.room.migration.Migration

object Const {
    const val PAGE_SIZE_TEXT = "PAGE_SIZE"
    const val PAGE_SIZE = 20
    const val WEB_API = "WEB_API"
    const val TIMES_USED_TO_EVOLVE = 20
    const val RECOIL_TAKE_TO_EVOLVE = 294

    const val MAX_STAT = 255 //based on Blissey, the pokemon with the highest single base stat value

    object PokemonData{
        const val POKEMON_TABLE_NAME = "pokemon"
    }

    //Database Const
    object PokemonDatabaseConsts {
        const val DB_VERSION = 1

        const val POKEMON_DB_VERSION = "pokemon_data.db"
        fun getMigrations(): Array<Migration> {
            return arrayOf()
        }
    }
}