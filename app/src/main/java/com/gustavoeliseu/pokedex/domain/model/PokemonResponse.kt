package com.gustavoeliseu.pokedex.domain.model

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @field:SerializedName("next")
    val next: String? = null,

    @field:SerializedName("previous")
    val previous: String? = null,

    @field:SerializedName("count")
    val count: Int,

    @field:SerializedName("results")
    val results: List<SimplePokemon> = listOf(),
)