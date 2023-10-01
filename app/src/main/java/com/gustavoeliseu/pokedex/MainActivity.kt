package com.gustavoeliseu.pokedex

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gustavoeliseu.pokedex.fragment.PokeListFragment
import com.gustavoeliseu.pokedex.fragment.PokemonDetailsFragment
import com.gustavoeliseu.pokedex.ui.theme.MyPokedexTheme
import com.gustavoeliseu.pokedex.utils.Const.POKEMON_ID
import com.gustavoeliseu.pokedex.utils.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPokedexTheme {
                PokemonAppScreen()

                //TODO - NEEDED TO FINISH PART 1 (PokeApi, simple pokedex no search)
                //TODO - PART 1 - FINISH the POKEDEX LAYOUT with links to other pokemon
                //TODO - PART 1 - ADD SCREENS FOR ABILITIES AND MOVES DETAILS
                //TODO - PART 1 - ADD EXPRESSO AND JUNIT AND DO 100% TESTING
                //TODO - PART 1 - ADD OFFLINE FIRST AND USECASES

                //TODO - NEEDED FOR PART 2 - (Smogon or Graphql, custom app with search and option to save teams)
                //TODO - PART 2 - add the option to mount your own team and save on local database(room or pure sqlite)
                //TODO - PART 2 - add Move e type coverage
                //TODO - PART 2 - search bar ( check if possible on the api) (if not possible, save it on pure sqlite or room and do local searching)
                //TODO - PART 2 - TRY to add format validation(for pvp teams) (Maybe try the smogon api)
                //TODO - PART 2 - Other option is the api https://graphqlpokemon.favware.tech/v7 using 89 offset to ignore custom monsters
            }
        }
    }

    @Composable
    fun PokemonAppScreen() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Route.PokemonListRoute.route) {
            composable(route = Route.PokemonListRoute.route) {
                PokeListFragment(onClick = { id ->
                    navController.navigate(
                        Route.PokemonDetailRoute.createRoute(id)
                    )
                })
            }

            composable(
                route = Route.PokemonDetailRoute.route,
                arguments = listOf(navArgument(POKEMON_ID) {
                    type = NavType.IntType
                })
            ) { entry ->
                val pokeId = entry.arguments?.getInt(POKEMON_ID)
                if (pokeId == null) {
                    Toast.makeText(
                        applicationContext,
                        "Não foi possível identificar o mostro clicado",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate(Route.PokemonListRoute.route)
                } else {
                    if (navController.currentDestination?.route != Route.PokemonDetailRoute.createRoute(
                            pokeId
                        )
                    ) {
                        PokemonDetailsFragment(pokeId = pokeId) {
                            navController.navigate(Route.PokemonListRoute.route)
                        }
                    }
                }
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MyPokedexTheme {
            PokeListFragment()
        }
    }
}