package com.gustavoeliseu.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gustavoeliseu.domain.models.PokemonDetails
import com.gustavoeliseu.pokedexdata.models.GenericPokemonData
import com.gustavoeliseu.pokedexdata.repository.GenericPokemonListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val genericPokemonListRepository: GenericPokemonListRepository,
) : ViewModel() {

    private val _details: MutableStateFlow<PokemonDetails?> = MutableStateFlow(null)
    val details = _details.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )
    suspend fun searchPokemonWithId(mId: Int) {
        if(mId!=0){
            _details.value= genericPokemonListRepository.getPokemonDetails(mId) as? PokemonDetails
        }
    }
}