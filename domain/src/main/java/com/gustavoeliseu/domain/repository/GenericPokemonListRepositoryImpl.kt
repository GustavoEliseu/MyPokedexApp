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
import com.gustavoeliseu.domain.dao.PokemonDao
import com.gustavoeliseu.domain.database.PokemonDatabase
import com.gustavoeliseu.domain.models.PokemonSimpleList
import com.gustavoeliseu.domain.models.PokemonSimpleList.Companion.toSimplePokemonList
import com.gustavoeliseu.domain.utils.Const.PAGE_SIZE
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import com.gustavoeliseu.pokedex.utils.SafeCrashlyticsUtil
import com.gustavoeliseu.pokedexdata.models.GenericPokemonData
import com.gustavoeliseu.pokedexdata.repository.GenericPokemonListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GenericPokemonListRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    private val pokemonDatabase: PokemonDatabase,
    private val pageSize: Int,
) : GenericPokemonListRepository {
    private lateinit var pokemonDao: PokemonDao;
    override fun queryPokemonList(
        searchTyped: String
    ): Flow<PagingData<GenericPokemonData>> {
        pokemonDao = pokemonDatabase.pokemonDao()
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
        return PokemonPagingSource(searchTerm = "$searchTyped%") { currentSearchTerm, startsWith, currentNextPage ->
            try {
                if (pokemonDao.getSearchPaginatedPokemonListCount(
                        currentSearchTerm,
                        startsWith,
                        currentNextPage,
                        PAGE_SIZE
                    ) > 0
                ) {
                    val pokeSimpleList = PokemonSimpleList(
                        pokemonDao.getAllDataPokemonPaginatedSearch(
                            currentSearchTerm,
                            startsWith,
                            currentNextPage,
                            PAGE_SIZE
                        )
                    )
                    if(pokeSimpleList.pokemonItems.size < PAGE_SIZE){
                    val pokeList = getQueryFromApollo(currentSearchTerm,startsWith+pokeSimpleList.pokemonItems.size,currentNextPage,
                        PAGE_SIZE-pokeSimpleList.pokemonItems.size)
                        val pokeListResult = pokeSimpleList.pokemonItems.toMutableList()
                        pokeListResult.addAll((pokeList.pokemonItems))
                        val resultPokeList = PokemonSimpleList(pokeListResult)
                        CoroutineScope(Dispatchers.IO).launch {
                            pokemonDao.addAllPokemonSimple(pokeList.pokemonItems.map { it.toPokemonDetails() })
                        }
                        resultPokeList
                    }else{
                        pokeSimpleList
                    }
                } else {
                    // add verification if data exists on database
                    val pokeList = getQueryFromApollo(currentSearchTerm,startsWith,currentNextPage,
                        PAGE_SIZE)
                    CoroutineScope(Dispatchers.IO).launch {
                        pokemonDao.addAllPokemonSimple(pokeList.pokemonItems.map { it.toPokemonDetails() })
                    }
                    pokeList
                }
            } catch (exception: ApolloException) {
                SafeCrashlyticsUtil.logException(exception)
                PokemonSimpleList(listOf())
            }
        }
    }

    suspend fun getQueryFromApollo(currentSearchTerm:String,startsWith:String,currentNextPage:Int, currentPageSize:Int):PokemonSimpleList{
        return apolloClient.query(
            PokemonListGraphQlQuery(
                searchTerm = Optional.present(currentSearchTerm),
                notStartsWith = Optional.present(startsWith),
                offset = Optional.present(currentNextPage),
                pageSize = Optional.present(currentPageSize)
            )
        ).refetchPolicy(FetchPolicy.CacheFirst).execute().data?.toSimplePokemonList()
            ?: PokemonSimpleList(
                listOf()
            )
    }
}