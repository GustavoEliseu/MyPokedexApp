package com.gustavoeliseu.pokedex.fragment

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.gustavoeliseu.pokedex.domain.model.PokemonDetails
import com.gustavoeliseu.pokedex.utils.Response
import com.gustavoeliseu.pokedex.utils.SafeCrashlyticsUtil
import com.gustavoeliseu.pokedex.viewmodel.PokemonDetailsViewModel
import kotlin.coroutines.coroutineContext

@Composable
fun PokemonDetailsFragment(
    modifier: Modifier = Modifier,
    onClick: (id: Int) -> Unit = {},
    pokeId: Int,
    pokemonDetailsViewModel: PokemonDetailsViewModel = hiltViewModel()
) {
    fun launch() {
        pokemonDetailsViewModel.getPokemonDetails(pokeId)
    }

    launch()
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when(val pokemonResponse = pokemonDetailsViewModel.pokemonDetailsState.value){
            is Response.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is Response.Success -> {
                PokemonDetailsScreen(
                    modifier = modifier,
                    details = pokemonResponse.data
                )
            }
            is Response.Failure -> {
                pokemonResponse.e?.let {error->
                    SafeCrashlyticsUtil.logException(error)
                }
                //TODO ADD TREATMENT FOR FAILURE
            }
        }
    }

}

@Composable
fun PokemonDetailsScreen(
    modifier: Modifier = Modifier,
    details: PokemonDetails?
){
    val mContext = LocalContext.current
    Toast.makeText(mContext,"Entrou", Toast.LENGTH_SHORT).show()
    details?.let {
        Toast.makeText(mContext,details.name, Toast.LENGTH_SHORT).show()
        Text(text = details.name)
    }

}