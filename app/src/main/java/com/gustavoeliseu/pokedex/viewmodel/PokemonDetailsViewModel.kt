package com.gustavoeliseu.pokedex.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gustavoeliseu.pokedex.domain.model.PokemonDetails
import com.gustavoeliseu.pokedex.domain.model.PokemonSpecieDetails
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


    private val _pokemonSpeciesState =
        mutableStateOf<Response<PokemonSpecieDetails>>(Response.Loading)
    val pokemonSpeciesState: State<Response<PokemonSpecieDetails>> = _pokemonSpeciesState


    fun getPokemonSpeciesDetails(pokeId: Int) {
        viewModelScope.launch {
            pokemonRepository.getPokemonSpeciesDetails(pokeId).collect { response ->
                _pokemonSpeciesState.value = response
            }
        }
    }

    fun getPokemonDetails(pokeId: Int) {
        viewModelScope.launch {
            pokemonRepository.getPokemonDetails(pokeId).collect { response ->
                _pokemonDetailsState.value = response
            }
        }
    }

    fun getPokemonUU(pokeId: Int) {

    }
}