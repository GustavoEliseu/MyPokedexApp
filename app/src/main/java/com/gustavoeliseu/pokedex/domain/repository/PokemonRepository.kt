package com.gustavoeliseu.pokedex.domain.repository

import androidx.paging.PagingData
import com.gustavoeliseu.pokedex.domain.model.PokemonDetails
import com.gustavoeliseu.pokedex.domain.model.SimpleGenericPokemonData
import com.gustavoeliseu.pokedex.utils.Response
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(): Flow<PagingData<SimpleGenericPokemonData>>
    fun getPokemonDetails(pokeId: Int): Flow<Response<PokemonDetails>>
}