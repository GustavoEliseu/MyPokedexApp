package com.gustavoeliseu.pokedex.domain.repository

import androidx.paging.PagingData
import com.gustavoeliseu.pokedex.domain.model.PokemonSimpleListItem
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun queryPokemonList(
        searchTyped: String
    ): Flow<PagingData<PokemonSimpleListItem>>

//    fun getPokemonDetails(pokeId: Int): Flow<Response<PokemonDetails>>
}