package com.gustavoeliseu.pokedex.network.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery

class PokemonPagingSource(private val response: suspend (Int) -> PokemonListGraphQlQuery.Data?) :
    PagingSource<Int, PokemonListGraphQlQuery.PokemonItem>() {
    override fun getRefreshKey(state: PagingState<Int, PokemonListGraphQlQuery.PokemonItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult.Page<Int, PokemonListGraphQlQuery.PokemonItem> {
        val nextPage = params.key ?: 0
        val pokeList = response.invoke((nextPage))?.pokemonItem ?: listOf()

        return LoadResult.Page(
            data = pokeList,
            prevKey = if (nextPage == 0) null else nextPage - 1,
            nextKey = if (pokeList.isNotEmpty()) pokeList.last().id else null
        )
    }

}