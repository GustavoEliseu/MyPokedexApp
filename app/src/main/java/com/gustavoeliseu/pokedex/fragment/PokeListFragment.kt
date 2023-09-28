package com.gustavoeliseu.pokedex.fragment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gustavoeliseu.pokedex.ui.pokemon.PokemonCard

@Composable
fun PokeListFragment(onClick: (id: Int) -> Unit, ) {
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        content = {
            items(151) {
                PokemonCard(
                    id = it+1,
                    name = "Crabominable",
                    picture = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${it+1}.png",
                    modifier = Modifier
                        .clickable {
                            onClick(it)
                        })
            }
        })
}