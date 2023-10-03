package com.gustavoeliseu.pokedex.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gustavoeliseu.pokedex.domain.model.PokemonDetails
import com.gustavoeliseu.pokedex.domain.repository.PokemonRepository
import com.gustavoeliseu.pokedex.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    val pokemonRepository: PokemonRepository,
) : ViewModel() {

    private val _pokemonDetailsState =
        mutableStateOf<Response<PokemonDetails>>(Response.Loading)
    val pokemonDetailsState: State<Response<PokemonDetails>> = _pokemonDetailsState

    fun getPokemonDetails(pokeId: Int) {
        viewModelScope.launch {
            pokemonRepository.getPokemonDetails(pokeId).collect { response ->
                _pokemonDetailsState.value = response
            }
        }
    }
}