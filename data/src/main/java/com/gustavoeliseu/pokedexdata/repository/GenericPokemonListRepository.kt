package com.gustavoeliseu.pokedexdata.repository

import androidx.paging.PagingData
import com.gustavoeliseu.pokedexdata.models.GenericPokemonData
import kotlinx.coroutines.flow.Flow

interface GenericPokemonListRepository {
    fun queryPokemonList(
        searchTyped: String
    ): Flow<PagingData<GenericPokemonData>>

    suspend fun getPokemonDetails(
        pokeId:Int
    ):GenericPokemonData?
}