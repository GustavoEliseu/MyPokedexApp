package com.gustavoeliseu.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gustavoeliseu.pokedex.domain.model.SimpleGenericPokemonData
import com.gustavoeliseu.pokedex.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    pokemonRepository: PokemonRepository
): ViewModel() {

    val pokemonListState: Flow<PagingData<SimpleGenericPokemonData>> = pokemonRepository.getPokemonList().cachedIn(viewModelScope)
}