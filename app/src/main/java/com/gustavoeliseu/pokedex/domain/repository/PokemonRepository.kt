package com.gustavoeliseu.pokedex.domain.repository

import androidx.paging.PagingData
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun queryPokemonList(
        searchTyped: String
    ): Flow<PagingData<PokemonListGraphQlQuery.PokemonItem>>

//    fun getPokemonDetails(pokeId: Int): Flow<Response<PokemonDetails>>
}