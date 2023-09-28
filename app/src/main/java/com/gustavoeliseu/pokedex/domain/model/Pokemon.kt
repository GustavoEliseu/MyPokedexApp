package com.gustavoeliseu.pokedex.domain.model

import com.google.gson.annotations.SerializedName

data class SimplePokemon(
    @field:SerializedName("name")
    val name:String,
    @field:SerializedName("url")
    val url: String
)

data class PokemonDetails(
    val name:String,
    val url: String
)