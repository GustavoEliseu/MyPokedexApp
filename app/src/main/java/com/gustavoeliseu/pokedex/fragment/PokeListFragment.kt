@file:OptIn(ExperimentalMaterial3Api::class)

package com.gustavoeliseu.pokedex.fragment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import com.gustavoeliseu.pokedex.R
import com.gustavoeliseu.pokedex.ui.pokemon.PokemonCard
import com.gustavoeliseu.pokedex.utils.ColorEnum
import com.gustavoeliseu.pokedex.viewmodel.PokemonListViewModel


@Composable
fun PokedexListFragment(
    modifier: Modifier = Modifier,
    onClick: (id: Int) -> Unit = {},
    pokemonListViewModel: PokemonListViewModel = hiltViewModel()
) {
    //TODO - DIVIDE INTO SMALLER COMPOSES
    val isSearching by pokemonListViewModel.isSearchShowing.collectAsState()
    val textSearch by pokemonListViewModel.search.collectAsState()
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSearching) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 25.dp)
                                        .align(Alignment.CenterStart)
                                        .height(56.dp),
                                    value = textSearch,
                                    onValueChange = { query ->
                                        pokemonListViewModel.setSearch(query)
                                    },
                                    placeholder = {
                                        Text(
                                            text = stringResource(id = R.string.search_placeholder),
                                            modifier = Modifier.alpha(.6f),
                                            color = Color.White
                                        )
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                    keyboardActions = KeyboardActions(
                                        onSearch = {
                                            pokemonListViewModel.setSearch(textSearch)
                                        },
                                    ),
                                    shape = RoundedCornerShape(5.dp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        containerColor = Color.Transparent,
                                        textColor = Color.White,
                                        cursorColor = Color.White
                                    )
                                )
                            } else {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    text = stringResource(id = R.string.pokedex)
                                )
                            }
                            IconButton(
                                modifier = Modifier
                                    .size(48.dp)
                                    .align(Alignment.CenterEnd),
                                onClick = {
                                    if (isSearching) pokemonListViewModel.setSearch("")
                                    pokemonListViewModel.toggleIsSearchShowing()
                                }) {
                                Icon(
                                    if (!isSearching) Icons.Filled.Search else Icons.Filled.Close,
                                    if (!isSearching) stringResource(id = R.string.search) else stringResource(
                                        id = R.string.close_search
                                    )
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White,
                    )

                )
            }, content = { padding ->
                Column() {
                    Box(modifier = modifier.padding(top = padding.calculateTopPadding()))
                    PokeListGrid(
                        modifier = modifier,
                        pokemonList = pokemonListViewModel.pokemonListState.collectAsLazyPagingItems(),
                        onClick = onClick
                    ) { value -> pokemonListViewModel.setSearch(value) }
                }
            }
        )
    }
}

@Composable
fun PokeListGrid(
    modifier: Modifier = Modifier,
    pokemonList: LazyPagingItems<PokemonListGraphQlQuery.PokemonItem>,
    onClick: (id: Int) -> Unit,
    updateSearch: (text: String) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        content = {
            items(pokemonList.itemCount) { index ->
                pokemonList[index]?.let { pk ->
                    PokemonCard(
                        id = pk.id,
                        name = pk.name, //Safe since there's no pokemon with null "name" value
                        picture = stringResource(id = R.string.pokemon_sprite_url, pk.id),
                        modifier = modifier
                            .clickable {
                                onClick(pk.id)
                            },
                        colorEnum = ColorEnum.fromInt(pk.pokemon_color_id)
                    )
                }

            }
        })
}