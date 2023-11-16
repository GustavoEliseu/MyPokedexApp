@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package com.gustavoeliseu.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gustavoeliseu.domain.usecase.GetSimplePokemonListUseCase
import com.gustavoeliseu.pokedexdata.models.GenericPokemonData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    genericPokemonListUseCase: GetSimplePokemonListUseCase
) : ViewModel() {


    private var offlineFirst = false //todo change to a use save preference when offlineFirst is added

    private val _isSearchShowing = MutableStateFlow(false)
    val isSearchShowing = _isSearchShowing.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false,
        )

    private val _search = MutableStateFlow("")
    val search = _search.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = "",
        )

    private val _offline = MutableStateFlow(false)
    val offline = _offline.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false,
        )

    var pokemonListState: Flow<PagingData<GenericPokemonData>> =
            search.debounce(300).flatMapLatest {query ->
                genericPokemonListUseCase.getList(query, (offline.value || offlineFirst)).cachedIn(viewModelScope)
            }

    fun setSearch(query: String) {
        _search.value = query
    }

    fun setOfflineState(lostConnection: Boolean) {
        _offline.value = lostConnection
    }

    fun toggleIsSearchShowing() {
        _isSearchShowing.value = !_isSearchShowing.value
    }
}