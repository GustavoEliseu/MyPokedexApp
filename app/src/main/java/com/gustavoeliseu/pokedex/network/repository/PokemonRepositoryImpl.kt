package com.gustavoeliseu.pokedex.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import com.gustavoeliseu.pokedex.domain.model.PokemonDetails
import com.gustavoeliseu.pokedex.domain.model.PokemonSpecieDetails
import com.gustavoeliseu.pokedex.domain.repository.PokemonRepository
import com.gustavoeliseu.pokedex.network.pagingsource.PokemonPagingSource
import com.gustavoeliseu.pokedex.utils.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val pokemonApi: ApolloClient,
    private val pageSize: Int
) : PokemonRepository {


//    override fun queryPokemonList(searchTyped: String): Flow<ApolloResponse<PokemonListGraphQlQuery.Data>> {
//        return pokemonApi.query(PokemonListGraphQlQuery(Optional.present(searchTyped))).toFlow()
//    }

    override fun queryPokemonList(searchTyped: String): Flow<PagingData<PokemonListGraphQlQuery.PokemonItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                PokemonPagingSource(response = { offset ->
                    pokemonApi.query(
                        PokemonListGraphQlQuery(
                            searchTerm = Optional.present(searchTyped),
                            offset = Optional.present(offset),
                            pageSize = Optional.present(20)
                        )
                    ).execute().data
                }
                )
            }
        ).flow
    }

    override fun getPokemonDetails(pokeId: Int): Flow<Response<PokemonDetails>> {
        TODO("Not yet implemented")
    }

    override fun getPokemonSpeciesDetails(pokeId: Int): Flow<Response<PokemonSpecieDetails>> {
        TODO("Not yet implemented")
    }

//    override fun queryPokemonList(searchTyped: String): Flow<Response<PokemonListGraphQlQuery.Data>> =
//        flow {
//            try {
//                emit(Response.Loading)
//                val result = pokemonApi.query(PokemonListGraphQlQuery(
//                    searchTerm = Optional.present(searchTyped),
//                    offset = Optional.present(0),
//                    pageSize = Optional.present(20)
//                )).execute().data
//                emit(Response.Success(result))
//            } catch (e: Exception) {
//                emit(Response.Failure(e))
//            }
//        }.flowOn(Dispatchers.IO)
}

//    override fun getPokemonList(searchTyped: String): Flow<PagingData<NewPokemonListData>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = pageSize,
//                enablePlaceholders = true
//            ),
//            pagingSourceFactory = {
//                PokemonPagingSource(
//                    response = { offset ->
//                        val paramObject = JSONObject()
//                        paramObject.put(
//                            "query", "query samplePokeAPIquery {\n" +
//                                    "  pokemonList: pokemon_v2_pokemonspecies(where: {name: {_regex: \"$searchTyped\"}}, offset: $offset, limit: $pageSize, order_by: {id: asc}) {\n" +
//                                    "    name\n" +
//                                    "    id\n" +
//                                    "    pokemon_color_id\n" +
//                                    "  }\n" +
//                                    "}"
//                        )
//                        pokemonApi.getPokemonList(paramObject.toString())
//                    }
//                )
//            }
//        ).flow
//    }


//
//    override fun getPokemonDetails(pokeId: Int): Flow<Response<PokemonDetails>> = flow {
////        try {
////            emit(Response.Loading)
////            val responseApi = pokemonApi.getPokemonDetails(
////                id = pokeId
////            )
////            emit(Response.Success(responseApi))
////        } catch (e: Exception) {
////            emit(Response.Failure(e))
////        }
////    }.flowOn(Dispatchers.IO)
//    }