package com.gustavoeliseu.pokedex

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gustavoeliseu.pokedex.fragment.PokeListFragment
import com.gustavoeliseu.pokedex.ui.pokemon.PokemonCard
import com.gustavoeliseu.pokedex.ui.theme.MyPokedexTheme
import com.gustavoeliseu.pokedex.utils.Const
import com.gustavoeliseu.pokedex.utils.Const.POKEMON_DETAIL
import com.gustavoeliseu.pokedex.utils.Const.POKEMON_ID
import com.gustavoeliseu.pokedex.utils.Const.POKEMON_LIST
import com.gustavoeliseu.pokedex.utils.Route

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPokedexTheme {
                PokemonAppScreen()
                //TODO - 2 - ADD RETROFIT and api calls
                //TODO - 3 - Add data for pokemonList and pokemonDetails
                //TODO - 4 - ADD KOIN OR HILT FOR DEPENDENCY INJECTION
                //TODO - 5 - Fill the list data with the data from the requests

            }
        }
    }

    @Composable
    fun PokemonAppScreen(){
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = POKEMON_LIST ){
            composable(route = Route.PokemonList.route) {
                PokeListFragment(){id->
                    navController.navigate(Route.PokemonDetail.createRoute(id)
                    )
                }
            }

            composable(route = Route.PokemonDetail.route,
            arguments = listOf(navArgument(POKEMON_ID){
                type = NavType.IntType
            })
            ) { entry ->
                val id = entry.arguments?.getInt(POKEMON_ID)
                if(id == null){
                    Toast.makeText(applicationContext, "Não foi possível identificar o mostro clicado",Toast.LENGTH_SHORT).show()
                    navController.navigate(Route.PokemonList.route)
                }else {
                    PokemonDetailScreen(id)
                }
            }
        }
    }

    //TODO- 6 -Create details view with animation in another file
    @Composable
    fun PokemonDetailScreen(id:Int) {
        Box(Modifier.background(Color.Black)){
            Text(text = "Entrou $id", color = Color.White)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MyPokedexTheme {
            PokeListFragment(){Toast.makeText(applicationContext,"teste",Toast.LENGTH_SHORT).show()}
        }
    }
}