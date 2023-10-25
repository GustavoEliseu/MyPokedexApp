package com.gustavoeliseu.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.refetchPolicy
import com.apollographql.apollo3.exception.ApolloException
import com.gustavoeliseu.domain.PokemonPagingSource
import com.gustavoeliseu.domain.models.PokemonSimpleList
import com.gustavoeliseu.domain.models.PokemonSimpleList.Companion.toSimplePokemonList
import com.gustavoeliseu.domain.utils.Const
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import com.gustavoeliseu.pokedex.utils.SafeCrashlyticsUtil
import com.gustavoeliseu.pokedexdata.models.GenericPokemonData
import com.gustavoeliseu.pokedexdata.models.GenericPokemonDataList
import com.gustavoeliseu.pokedexdata.repository.GenericPokemonListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GenericPokemonListRepositoryImpl @Inject constructor(
    private val pokemonApi: ApolloClient,
    private val pageSize: Int,
) : GenericPokemonListRepository {

    override fun queryPokemonList(
        searchTyped: String
    ): Flow<PagingData<GenericPokemonData>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { createPagingSource(searchTyped) }
        ).flow
    }

    private fun createPagingSource(
        searchTyped: String,
    ): PokemonPagingSource {
        return PokemonPagingSource(searchTerm = "$searchTyped%") { currentSearchTerm,startsWith,currentNextPage ->
            try {
                // add verification if data exists on database
                pokemonApi.query(
                    PokemonListGraphQlQuery(
                        searchTerm = Optional.present(currentSearchTerm),
                        notStartsWith = Optional.present(startsWith),
                        offset = Optional.present(currentNextPage),
                        pageSize = Optional.present(Const.PAGE_SIZE)
                    )
                ).refetchPolicy(FetchPolicy.CacheFirst).execute().data?.toSimplePokemonList() ?: PokemonSimpleList(
                    listOf()
                )as? GenericPokemonDataList
            } catch (exception: ApolloException) {
                SafeCrashlyticsUtil.logException(exception)
                PokemonSimpleList(listOf())
            }
        }
    }
}