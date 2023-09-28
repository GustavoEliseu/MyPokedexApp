package com.gustavoeliseu.pokedex.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gustavoeliseu.pokedex.domain.model.PokemonDetails
import com.gustavoeliseu.pokedex.domain.repository.PokemonRepository
import com.gustavoeliseu.pokedex.domain.model.SimplePokemon
import com.gustavoeliseu.pokedex.network.PokemonApi
import com.gustavoeliseu.pokedex.network.pagingsource.PokemonPagingSource
import com.gustavoeliseu.pokedex.utils.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val pokemonApi: PokemonApi,
    private val pageSize: Int
) : PokemonRepository {
    override fun getPokemonList(): Flow<PagingData<SimplePokemon>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            PokemonPagingSource(
                response = { nextPage ->
                    pokemonApi.getPokemonList(nextPage,pageSize)
                }
            )
        }
    ).flow

    override fun getPokemonDetails(): Flow<Response<PokemonDetails>> {
        TODO("Not yet implemented")
    }
}