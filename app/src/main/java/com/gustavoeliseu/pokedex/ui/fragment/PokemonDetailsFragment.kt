package com.gustavoeliseu.pokedex.ui.fragment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.gustavoeliseu.domain.models.PokemonDetails
import com.gustavoeliseu.domain.models.PokemonSimpleList
import com.gustavoeliseu.domain.models.PokemonSimpleList.Companion.toSimplePokemonList
import com.gustavoeliseu.pokedex.GetPokemonDetailsQuery
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import com.gustavoeliseu.pokedex.viewmodel.PokemonDetailsViewModel
import java.util.Optional

@Composable
fun PokemonDetailsFragment(
    modifier: Modifier = Modifier,
    onClick: (id: Int) -> Unit = {},
    pokeId: Int,
    pokemonDetailsViewModel: PokemonDetailsViewModel = hiltViewModel(),
    onError: () -> Unit
) {
    val pokemonDetails by pokemonDetailsViewModel.details.collectAsState()
    LaunchedEffect(pokeId ){
        pokemonDetailsViewModel.searchPokemonWithId(pokeId)
    }
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if(pokemonDetails!=null){
            Text(text = "entrooou ${pokemonDetails?.name}")
        }
    }
}

@Composable
fun PokemonDetailsScreen(
    modifier: Modifier = Modifier,
    details: PokemonDetails?,
    onError: () -> Unit
) {
    if (details == null) {
        onError()
        return
        //todo - add treatment for null details or species
    }

    Text(text = details.name)


}