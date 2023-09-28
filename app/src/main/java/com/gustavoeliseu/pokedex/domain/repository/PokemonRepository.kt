package com.gustavoeliseu.pokedex.domain.repository

import androidx.paging.PagingData
import com.gustavoeliseu.pokedex.domain.model.PokemonDetails
import com.gustavoeliseu.pokedex.domain.model.SimplePokemon
import com.gustavoeliseu.pokedex.utils.Response
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(): Flow<PagingData<SimplePokemon>>
    fun getPokemonDetails(): Flow<Response<PokemonDetails>>
}