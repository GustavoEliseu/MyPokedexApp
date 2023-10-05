package com.gustavoeliseu.pokedex.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.refetchPolicy
import com.apollographql.apollo3.exception.ApolloException
import com.gustavoeliseu.pokedex.domain.model.PokemonDetails
import com.gustavoeliseu.pokedex.domain.model.PokemonSimpleList
import com.gustavoeliseu.pokedex.domain.model.PokemonSimpleList.Companion.toSimplePokemonList
import com.gustavoeliseu.pokedex.domain.model.PokemonSimpleListItem
import com.gustavoeliseu.pokedex.domain.repository.PokemonRepository
import com.gustavoeliseu.pokedex.network.pagingsource.PokemonPagingSource
import com.gustavoeliseu.pokedex.utils.Response
import com.gustavoeliseu.pokedex.utils.SafeCrashlyticsUtil
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val pokemonApi: ApolloClient,
    private val pageSize: Int,
) : PokemonRepository {

    override fun queryPokemonList(
        searchTyped: String
    ): Flow<PagingData<PokemonSimpleListItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { createPagingSource(searchTyped) }
        ).flow
    }

    private fun createPagingSource(
        searchTyped: String
    ): PokemonPagingSource {
        return PokemonPagingSource(searchTerm = "$searchTyped%") { mQuery ->
            try {
                pokemonApi.query(
                    mQuery
                ).refetchPolicy(FetchPolicy.CacheFirst).execute().data?.toSimplePokemonList()
            } catch (exception: ApolloException) {
                SafeCrashlyticsUtil.logException(exception)
                PokemonSimpleList(listOf())
            }
        }
    }

    override fun getPokemonDetails(pokeId: Int): Flow<Response<PokemonDetails>> {
        TODO("Not yet implemented")
    }
}