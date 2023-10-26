package com.gustavoeliseu.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gustavoeliseu.domain.utils.Const.PAGE_SIZE
import com.gustavoeliseu.pokedexdata.models.GenericPokemonData
import com.gustavoeliseu.pokedexdata.models.GenericPokemonDataList

class PokemonPagingSource(private val searchTerm: String,private val response: suspend (String,String,Int) -> GenericPokemonDataList?) :
    PagingSource<Int, GenericPokemonData>() {

    //TODO - CLEAN THIS CLASS SO IT CAN BE RE-UTILIZED FOR OTHER PURPOSES, SOLID
    //THIS CLASS IS TOO SPECIFIC, SHOULD BE MORE GENERIC SO IT CAN BE RE-UTILIZED FOR PART 3(tier search)
    private var isSearching = false
    private var nextPage = 0
    private var currentSearchTerm: String = ""

    override fun getRefreshKey(state: PagingState<Int, GenericPokemonData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GenericPokemonData> {
        val localSearchTerm: String = if (isSearching && searchTerm.isNotEmpty()) "%$searchTerm" else searchTerm
        val startsWith = if (isSearching && searchTerm.isNotEmpty()) searchTerm else ""

        // Reset nextPage only when the search term changes
        if (localSearchTerm != currentSearchTerm) {
            nextPage = 0
            currentSearchTerm = localSearchTerm
        }

        val currentNextPage = nextPage
        nextPage = currentNextPage + PAGE_SIZE

        val pokeList = response.invoke(currentSearchTerm,startsWith,currentNextPage)?.pokemonItems ?: listOf()
        val prevKey = if (currentNextPage > 0) currentNextPage - PAGE_SIZE else null
        val nextKey = when{
            pokeList.size>= PAGE_SIZE ->{currentNextPage + PAGE_SIZE}
            pokeList.size < PAGE_SIZE && searchTerm.isNotEmpty() &&!isSearching->{0}
            else-> null
        }

        if (pokeList.isEmpty() && searchTerm.isNotEmpty() && !isSearching) {
            isSearching = true
            nextPage= 0
            return load(params)
        }

        return LoadResult.Page(
            data = pokeList,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }
}