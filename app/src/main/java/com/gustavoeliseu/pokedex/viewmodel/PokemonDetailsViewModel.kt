package com.gustavoeliseu.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import com.gustavoeliseu.pokedexdata.repository.GenericPokemonListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    val genericPokemonListRepository: GenericPokemonListRepository,
) : ViewModel(){}