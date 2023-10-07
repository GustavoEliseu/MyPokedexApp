package com.gustavoeliseu.domain.repository

import androidx.paging.PagingData
import com.gustavoeliseu.domain.entity.PokemonSimpleListItem
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun queryPokemonList(
        searchTyped: String
    ): Flow<PagingData<PokemonSimpleListItem>>

//    fun getPokemonDetails(pokeId: Int): Flow<Response<PokemonDetails>>
}