package com.gustavoeliseu.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import com.gustavoeliseu.pokedex.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    val pokemonRepository: PokemonRepository
) : ViewModel() {
    private val _search = MutableStateFlow("")

    //TODO - EDIT PAGING AND ADD SEARCH LAYOUT

    val search = _search.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = "",
        )

    private val _isSearchShowing = MutableStateFlow(false)

    val isSearchShowing = _isSearchShowing.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false,
        )

    val pokemonListState: Flow<PagingData<PokemonListGraphQlQuery.PokemonItem>> =
        pokemonRepository.queryPokemonList("").cachedIn(viewModelScope)

    fun setSearch(query: String) {
        _search.value = query
    }

    fun toggleIsSearchShowing() {
        _isSearchShowing.value = !_isSearchShowing.value
    }
}