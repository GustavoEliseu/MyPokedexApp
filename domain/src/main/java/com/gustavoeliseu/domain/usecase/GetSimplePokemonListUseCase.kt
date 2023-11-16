package com.gustavoeliseu.domain.usecase


import androidx.paging.PagingData
import com.gustavoeliseu.pokedexdata.models.GenericPokemonData
import com.gustavoeliseu.pokedexdata.repository.GenericPokemonListRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow


class GetSimplePokemonListUseCase @Inject constructor(private val genericPokemonListRepository: GenericPokemonListRepository) {
    fun getList(
        query:String,
        offline:Boolean
    ): Flow<PagingData<GenericPokemonData>> {
       return genericPokemonListRepository.queryPokemonList(query,
           offline)
    }
}