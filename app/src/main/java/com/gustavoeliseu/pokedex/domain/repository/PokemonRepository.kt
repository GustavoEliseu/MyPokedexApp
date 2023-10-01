package com.gustavoeliseu.pokedex.domain.repository

import androidx.paging.PagingData
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import com.gustavoeliseu.pokedex.domain.model.PokemonDetails
import com.gustavoeliseu.pokedex.domain.model.PokemonSpecieDetails
import com.gustavoeliseu.pokedex.utils.Response
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun queryPokemonList(searchTyped: String): Flow<PagingData<PokemonListGraphQlQuery.PokemonItem>>
    fun getPokemonDetails(pokeId: Int): Flow<Response<PokemonDetails>>

    fun getPokemonSpeciesDetails(pokeId: Int): Flow<Response<PokemonSpecieDetails>>
}