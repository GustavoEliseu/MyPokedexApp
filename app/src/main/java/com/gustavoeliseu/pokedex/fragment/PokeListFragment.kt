package com.gustavoeliseu.pokedex.fragment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.gustavoeliseu.pokedex.BuildConfig
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import com.gustavoeliseu.pokedex.R
import com.gustavoeliseu.pokedex.di.AppModules
import com.gustavoeliseu.pokedex.ui.pokemon.PokemonCard
import com.gustavoeliseu.pokedex.utils.ColorEnum
import com.gustavoeliseu.pokedex.viewmodel.PokemonListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


@Composable
fun PokeListFragment(
    modifier: Modifier = Modifier,
    onClick: (id: Int) -> Unit = {},
    pokemonListViewModel: PokemonListViewModel = hiltViewModel()
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        val listState = remember {
            mutableStateOf(mutableListOf<PokemonListGraphQlQuery.PokemonItem>())
        }
        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
//            val response = apolloClient?.query(PokemonListGraphQlQuery(Optional.present(""),Optional.present(0),Optional.present(20)))?.execute()
//            Log.d("POKEMONS LISTA SUCESSO", "Success ${response?.data}")
            pokemonListViewModel.setSearch("")
        }


        PokeListGrid(
            modifier = modifier,
            pokemonList = pokemonListViewModel.pokemonListState.collectAsLazyPagingItems(),
            onClick = onClick
        ) { value -> pokemonListViewModel.setSearch(value) }


    }
}

fun getApolloClient(): ApolloClient {
    val logging = HttpLoggingInterceptor()
        .apply { level = BuildConfig.INTERCEPTOR_LEVEL }

    val client = OkHttpClient.Builder()
        .addInterceptor(AppModules.AuthInterceptor())
        .addInterceptor(logging)
        .build()


    return ApolloClient.Builder()
        .serverUrl(BuildConfig.GRAPHQLAPI_URL)
        .okHttpClient(client).build()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
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