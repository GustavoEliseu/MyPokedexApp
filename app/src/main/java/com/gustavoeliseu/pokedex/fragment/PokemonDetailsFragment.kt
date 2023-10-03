package com.gustavoeliseu.pokedex.fragment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.gustavoeliseu.pokedex.domain.model.PokemonDetails
import com.gustavoeliseu.pokedex.utils.Response
import com.gustavoeliseu.pokedex.utils.SafeCrashlyticsUtil
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
        pokemonDetailsViewModel.getPokemonDetails(pokeId)
    }
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (val pokemonResponse = pokemonDetailsViewModel.pokemonDetailsState.value) {
            is Response.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            is Response.Success -> {
                PokemonDetailsScreen(
                    modifier = modifier,
                    details = pokemonResponse.data,
                    onError
                )
            }

            is Response.Failure -> {
                pokemonResponse.e?.let { error ->
                    SafeCrashlyticsUtil.logException(error)
                }
                onError()
                //TODO ADD TREATMENT FOR FAILURE
            }
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