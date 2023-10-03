package com.gustavoeliseu.pokedex.network.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import com.gustavoeliseu.pokedex.utils.Const.PAGE_SIZE

class PokemonPagingSource(private val response: suspend (Int) -> PokemonListGraphQlQuery.Data?) :
    PagingSource<Int, PokemonListGraphQlQuery.PokemonItem>() {
    override fun getRefreshKey(state: PagingState<Int, PokemonListGraphQlQuery.PokemonItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonListGraphQlQuery.PokemonItem> {
        val nextPage = params.key ?: 0
        val pokeList = response.invoke((nextPage))?.pokemonItem ?: listOf()
        val prevKey = if (nextPage > 0) nextPage - PAGE_SIZE else null
        val nextKey =
            if (pokeList.isNotEmpty() && pokeList.size > PAGE_SIZE - 1) nextPage + PAGE_SIZE else null
        if (pokeList.isEmpty()) return LoadResult.Error(Exception())
        return LoadResult.Page(
            data = pokeList ?: listOf(),
            prevKey = prevKey,
            nextKey = nextKey
        )
    }

}