package com.gustavoeliseu.pokedex.network.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gustavoeliseu.pokedex.domain.model.PokemonResponse
import com.gustavoeliseu.pokedex.domain.model.SimplePokemon
import com.gustavoeliseu.pokedex.utils.SafeCrashlyticsUtil

class PokemonPagingSource(private val response: suspend (Int) -> PokemonResponse):
    PagingSource<Int, SimplePokemon>() {
    override fun getRefreshKey(state: PagingState<Int, SimplePokemon>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SimplePokemon> {
        return try {
            val nextPage = params.key ?: 0
            val pokeList = response.invoke((nextPage))
            LoadResult.Page(
                data = pokeList.results,
                prevKey = if(nextPage == 0) null else nextPage -1,
                nextKey = pokeList.next
                    ?.substringAfter("offset=")
                    ?.substringBefore("&")
                    ?.toInt() ?: nextPage
            )
        } catch (e: Exception){
            SafeCrashlyticsUtil.logException(e)
            return LoadResult.Error(e)
        }
    }
}