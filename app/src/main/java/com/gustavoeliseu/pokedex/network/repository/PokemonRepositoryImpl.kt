package com.gustavoeliseu.pokedex.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gustavoeliseu.pokedex.domain.model.PokemonDetails
import com.gustavoeliseu.pokedex.domain.model.SimpleGenericPokemonData
import com.gustavoeliseu.pokedex.domain.repository.PokemonRepository
import com.gustavoeliseu.pokedex.network.PokemonApi
import com.gustavoeliseu.pokedex.network.pagingsource.PokemonPagingSource
import com.gustavoeliseu.pokedex.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val pokemonApi: PokemonApi,
    private val pageSize: Int
) : PokemonRepository {
    override fun getPokemonList(): Flow<PagingData<SimpleGenericPokemonData>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            PokemonPagingSource(
                response = { nextPage ->
                    pokemonApi.getPokemonList(nextPage, pageSize)
                }
            )
        }
    ).flow

    override fun getPokemonDetails(pokeId: Int): Flow<Response<PokemonDetails>> = flow {
        try {
            emit(Response.Loading)
            val responseApi = pokemonApi.getPokemonDetails(
                id = pokeId
            )
            emit(Response.Success(responseApi))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
}