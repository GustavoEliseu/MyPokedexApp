package com.gustavoeliseu.pokedex.fragment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.gustavoeliseu.pokedex.domain.model.SimplePokemon
import com.gustavoeliseu.pokedex.ui.pokemon.PokemonCard
import com.gustavoeliseu.pokedex.utils.getPokemonIdFromUrl
import com.gustavoeliseu.pokedex.viewmodel.PokemonListViewModel

@Composable
fun PokeListFragment(
    modifier: Modifier = Modifier,
    onClick: (id: Int) -> Unit = {},
pokemonListViewModel: PokemonListViewModel = hiltViewModel()) {
    pokeListGrid(
        modifier = modifier,
        pokemonList = pokemonListViewModel.pokemonListState.collectAsLazyPagingItems(),
        onClick = onClick
    )
}

@Composable
fun pokeListGrid(modifier: Modifier = Modifier,
                 pokemonList: LazyPagingItems<SimplePokemon>? = null,
                 onClick: (id: Int) -> Unit){
    if(pokemonList == null) return
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        content = {
            items(pokemonList.itemCount) { index ->
                pokemonList[index]?.let { pk->
                    val pokeId = pk.url.getPokemonIdFromUrl()
                    PokemonCard(
                        id = pokeId,
                        name = pk.name,
                        picture = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokeId}.png",
                        modifier = modifier
                            .clickable {
                                onClick(pokeId)
                            })
                }

            }
        })
}