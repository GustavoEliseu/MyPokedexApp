package com.gustavoeliseu.pokedexdata.models

import java.io.Serializable

open class GenericPokemonData(open val name: String?,
                                   open val id: Int?):Serializable {
}
open class GenericPokemonDataList(open val pokemonItems: List<GenericPokemonData>):Serializable {
}

//used for ANY data with name and URL only
open class UrlGenericPokemonData(
    open val name: String?,
    open val url: String
):Serializable