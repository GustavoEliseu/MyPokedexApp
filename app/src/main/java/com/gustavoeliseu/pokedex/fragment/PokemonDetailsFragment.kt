package com.gustavoeliseu.pokedex.fragment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.gustavoeliseu.pokedex.domain.model.PokemonDetails
import com.gustavoeliseu.pokedex.domain.model.PokemonSpecieDetails
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
        pokemonDetailsViewModel.getPokemonSpeciesDetails(pokeId)
    }
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val details: MutableState<PokemonDetails?> = remember { mutableStateOf(null) }
        val species: MutableState<PokemonSpecieDetails?> = remember {
            mutableStateOf(null)
        }

        if (details.value == null) {
            when (val pokemonResponse = pokemonDetailsViewModel.pokemonDetailsState.value) {
                is Response.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                is Response.Success -> {
                    details.value = pokemonResponse.data
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

        if (species.value == null) {
            when (val pokemonSpeciesResponse = pokemonDetailsViewModel.pokemonSpeciesState.value) {
                is Response.Loading -> {
                    if (details.value != null) {
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                is Response.Success -> {
                    species.value = pokemonSpeciesResponse.data
                }

                is Response.Failure -> {
                    pokemonSpeciesResponse.e?.let { error ->
                        SafeCrashlyticsUtil.logException(error)
                    }
                    onError()
                    //TODO ADD TREATMENT FOR FAILURE
                }
            }
        }


        if (details.value != null && species.value != null) {
            PokemonDetailsScreen(
                modifier = modifier,
                details = details.value,
                speciesData = species.value,
                onError
            )
        }
    }

}

@Composable
fun PokemonDetailsScreen(
    modifier: Modifier = Modifier,
    details: PokemonDetails?,
    speciesData: PokemonSpecieDetails?,
    onError: () -> Unit
) {
    if (details == null || speciesData == null) {
        onError()
        return
        //todo - add treatment for null details or species
    }

    Text(text = details.name)


}