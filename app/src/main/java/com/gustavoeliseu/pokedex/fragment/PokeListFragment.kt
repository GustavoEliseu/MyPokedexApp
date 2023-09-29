package com.gustavoeliseu.pokedex.fragment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.gustavoeliseu.pokedex.R
import com.gustavoeliseu.pokedex.domain.model.SimpleGenericPokemonData
import com.gustavoeliseu.pokedex.ui.pokemon.PokemonCard
import com.gustavoeliseu.pokedex.utils.getPokemonIdFromUrl
import com.gustavoeliseu.pokedex.viewmodel.PokemonListViewModel

@Composable
fun PokeListFragment(
    modifier: Modifier = Modifier,
    onClick: (id: Int) -> Unit ={},
    pokemonListViewModel: PokemonListViewModel = hiltViewModel()) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        PokeListGrid(
            modifier = modifier,
            pokemonList = pokemonListViewModel.pokemonListState.collectAsLazyPagingItems(),
            onClick = onClick
        )
    }
}

@Composable
fun PokeListGrid(modifier: Modifier = Modifier,
                 pokemonList: LazyPagingItems<SimpleGenericPokemonData>? = null,
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
                        picture = stringResource(id = R.string.pokemon_sprite_url,pokeId),
                        modifier = modifier
                            .clickable {
                                onClick(pokeId)
                            })
                }

            }
        })
}