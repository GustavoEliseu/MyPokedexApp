package com.gustavoeliseu.pokedex.utils

import com.gustavoeliseu.pokedex.utils.Const.POKEMON_DETAIL
import com.gustavoeliseu.pokedex.utils.Const.POKEMON_ID
import com.gustavoeliseu.pokedex.utils.Const.POKEMON_LIST

sealed class Route(val route: String){

    object PokemonListRoute: Route(POKEMON_LIST)
    object PokemonDetailRoute: Route("$POKEMON_DETAIL/{$POKEMON_ID}"){
        fun createRoute(pokemonId: Int) = "$POKEMON_DETAIL/$pokemonId"
    }
}
