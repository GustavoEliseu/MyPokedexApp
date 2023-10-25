package com.gustavoeliseu.domain.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gustavoeliseu.domain.dao.PokemonDao
import com.gustavoeliseu.domain.models.PokemonDetails
import com.gustavoeliseu.domain.models.PokemonSimpleListItem
import com.gustavoeliseu.domain.utils.Const.PokemonDatabaseConsts.DB_VERSION
import com.gustavoeliseu.domain.utils.Const.PokemonDatabaseConsts.POKEMON_DB_VERSION
import com.gustavoeliseu.domain.utils.converters.PokemonTypeConverters
import javax.inject.Inject

@Database(
    entities = [PokemonDetails::class, PokemonSimpleListItem::class],
    version = DB_VERSION,
    exportSchema = false
)
@TypeConverters(PokemonTypeConverters::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    companion object{

    @Volatile
    private var INSTANCE: PokemonDatabase? = null
    fun getInstance(context: Context): PokemonDatabase = INSTANCE ?: synchronized(this) {
        INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
    }

    private fun buildDatabase(context: Context) = Room.databaseBuilder(
        context.applicationContext,
        PokemonDatabase::class.java,
        POKEMON_DB_VERSION
    ).build()
    }
    fun destroyInstance() {
        INSTANCE = null
    }
}