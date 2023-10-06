package com.gustavoeliseu.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import com.gustavoeliseu.pokedex.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    val pokemonRepository: PokemonRepository,
) : ViewModel() {



}