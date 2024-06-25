package com.gustavoeliseu.domain.di

import android.content.Context
import com.gustavoeliseu.domain.dao.PokemonDao
import com.gustavoeliseu.domain.database.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): PokemonDatabase {
        return PokemonDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideCharacterDao(charactersDatabase: PokemonDatabase): PokemonDao {
        return charactersDatabase.pokemonDao()
    }
}