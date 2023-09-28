package com.gustavoeliseu.pokedex.network

import com.gustavoeliseu.pokedex.domain.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") page:Int,
        @Query("limit") pageSize: Int
    ) : PokemonResponse
}