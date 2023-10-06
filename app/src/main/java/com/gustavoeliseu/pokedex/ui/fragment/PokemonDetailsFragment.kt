package com.gustavoeliseu.pokedex.ui.fragment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.gustavoeliseu.pokedex.domain.model.PokemonDetails
import com.gustavoeliseu.pokedex.viewmodel.PokemonDetailsViewModel

@Composable
fun PokemonDetailsFragment(
    modifier: Modifier = Modifier,
    onClick: (id: Int) -> Unit = {},
    pokeId: Int,
    pokemonDetailsViewModel: PokemonDetailsViewModel = hiltViewModel(),
    onError: () -> Unit
) {

    LaunchedEffect(Unit) {

    }
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

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