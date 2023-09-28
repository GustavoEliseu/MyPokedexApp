package com.gustavoeliseu.pokedex

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gustavoeliseu.pokedex.ui.pokemon.PokemonCard
import com.gustavoeliseu.pokedex.ui.theme.MyPokedexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPokedexTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }
                PokeList()
            }
        }
    }

    @Composable
    fun PokeList() {
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            content = {
                items(100) {
                    PokemonCard(
                        id = 132,
                        name = "ditto",
                        picture = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/132.png",
                        modifier = Modifier
                            .clickable {
                                //todo - add click action
                                Toast.makeText(
                                    applicationContext,
                                    "Clicou no item $it",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                }
            })
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MyPokedexTheme {
            PokeList()
        }
    }
}