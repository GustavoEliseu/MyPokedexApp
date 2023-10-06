@file:OptIn(ExperimentalMaterial3Api::class)

package com.gustavoeliseu.pokedex.ui.fragment

import android.widget.Toast
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import com.gustavoeliseu.pokedex.R
import com.gustavoeliseu.pokedex.domain.model.PokemonSimpleListItem
import com.gustavoeliseu.pokedex.network.connection.ConnectivityObserver
import com.gustavoeliseu.pokedex.network.connection.NetworkConnectivityObserver
import com.gustavoeliseu.pokedex.ui.pokemon.PokemonCard
import com.gustavoeliseu.pokedex.utils.ColorEnum
import com.gustavoeliseu.pokedex.viewmodel.PokemonListViewModel
import kotlinx.coroutines.runBlocking


@Composable
fun PokedexListFragment(
    modifier: Modifier = Modifier,
    onClick: (id: Int) -> Unit = {},
    pokemonListViewModel: PokemonListViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isConnectedStatus by NetworkConnectivityObserver(context).observe().collectAsState(
        initial = ConnectivityObserver.Status.Available
    )
    var connectionLost by remember {
        mutableStateOf(true)
    }
    var position : Int? by remember {
        mutableStateOf(null)
    }
    var reloadingImages by remember{ mutableStateOf(false)}
    val isSearching by pokemonListViewModel.isSearchShowing.collectAsState()
    val textSearch by pokemonListViewModel.search.collectAsState()
    val lazyPokemonList = pokemonListViewModel.pokemonListState.collectAsLazyPagingItems()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isConnectedStatus != ConnectivityObserver.Status.Available) {
            connectionLost = true
            reloadingImages = false
        }
        if (isConnectedStatus == ConnectivityObserver.Status.Available && connectionLost) {
            runBlocking {
                lazyPokemonList.refresh()
                reloadingImages = false
            }
            connectionLost = true
        }
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
                                    if (isConnectedStatus == ConnectivityObserver.Status.Available) {
                                        if (isSearching) pokemonListViewModel.setSearch("")
                                        pokemonListViewModel.toggleIsSearchShowing()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.disable_no_connection),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
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
                        pokemonList = lazyPokemonList,
                        reloadingImages = reloadingImages,
                        updatePosition = {updateId->
                            position = updateId
                        },
                        onClick = onClick
                    )
                }
            }
        )
    }
}

@Composable
fun PokeListGrid(
    modifier: Modifier = Modifier,
    reloadingImages:Boolean,
    updatePosition: (id:Int) -> Unit,
    pokemonList: LazyPagingItems<PokemonSimpleListItem>?,
    onClick: (id: Int) -> Unit
) {
    if (pokemonList == null) return
    if (pokemonList.itemCount > 0) {
        updatePosition(pokemonList.itemSnapshotList.items.maxBy { it.id }.id)
    }
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
                        pokemonItemSimple = pk,
                        picture = stringResource(id = R.string.pokemon_sprite_url, pk.id),
                        modifier = modifier
                            .clickable {
                                onClick(pk.id)
                            },
                        colorEnum = ColorEnum.fromInt(pk.pokemonColorId),
                        reloading = reloadingImages
                    )
                }
            }
        })
}