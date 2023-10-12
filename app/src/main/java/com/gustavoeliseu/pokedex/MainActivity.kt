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
import coil.Coil
import coil.ImageLoader
import com.gustavoeliseu.pokedexlist.ui.PokedexListFragment
import com.gustavoeliseu.pokedex.ui.fragment.PokemonDetailsFragment
import com.gustavoeliseu.pokedex.ui.network_compose.NetworkComposableObserver
import com.gustavoeliseu.pokedex.ui.theme.MyPokedexTheme
import com.gustavoeliseu.pokedex.utils.Const.POKEMON_ID
import com.gustavoeliseu.pokedex.utils.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //TODO - NEEDED TO FINISH PART 1 - PokeApi, simple pokedex with search function
    // GOING TO FIX MVVM/CLEAN ON PART 2, since i'm using part 1 as a playground to test new libraries and technologies

    //TODO - PART 1 - FINISH the POKEDEX LAYOUT with links to other pokemon, skills and moves
    //TODO - PART 1 - ADD ESPRESSO AND JUNIT AND DO 100% TESTING
    //TODO - PART 1 - ADD SCREENS FOR ABILITIES AND MOVES DETAILS

    //TODO - NEEDED FOR PART 2 - OFFLINE FIRST

    //TODO - PART 2 - ADD OFFLINE FIRST AND USE-CASES
    //TODO - PART 2 - CHECK GRAPHQL EXTRAS WHEN ADDING ROOM
    //TODO - PART 2 - FIX IMPLEMENTATION ARCHITECTURE AND CLEAN, CHECK IF EVERYTHING IS ACCORDING TO THE SOLID PRINCIPLES
    //TODO - PART 2 - REMOVE UNUSED CODES AND ADD BOTTOM NAVIGATION FOR PART3

    //TODO - NEEDED FOR PART 3 - (Smogon or another api, custom app with search and option to save teams)

    //TODO - PART 2 - add the option to mount your own team and save on local database(room or pure sqlite)
    //TODO - PART 2 - add Move e type coverage
    //TODO - PART 2 - TRY to add format validation(for pvp teams) (Maybe try the smogon api)
    //TODO - PART 2 - Other option is the api https://graphqlpokemon.favware.tech/v7 using 89 offset to ignore custom monsters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageLoader = ImageLoader.Builder(this)
            .respectCacheHeaders(false)
            .build()
        Coil.setImageLoader(imageLoader)
        setContent {
            MyPokedexTheme {
                PokemonAppScreen()
                NetworkComposableObserver()
            }
        }
    }

    @Composable
    fun PokemonAppScreen() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Route.PokemonListRoute.route) {
            composable(route = Route.PokemonListRoute.route) {
                PokedexListFragment(onClick = { id ->
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
                        "It was not possible to detect the selected pokemon",
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
            PokedexListFragment()
        }
    }
}