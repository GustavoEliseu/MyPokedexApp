package com.gustavoeliseu.pokedex.network.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apollographql.apollo3.api.Optional
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import com.gustavoeliseu.pokedex.domain.model.PokemonSimpleList
import com.gustavoeliseu.pokedex.domain.model.PokemonSimpleListItem
import com.gustavoeliseu.pokedex.utils.Const.PAGE_SIZE

class PokemonPagingSource(private val searchTerm: String,private val response: suspend (PokemonListGraphQlQuery) -> PokemonSimpleList?) :
    PagingSource<Int, PokemonSimpleListItem>() {

    //TODO - CLEAN THIS CLASS SO IT CAN BE RE-UTILIZED FOR OTHER PURPOSES, SOLID
    //THIS CLASS IS TOO SPECIFIC, SHOULD BE MORE GENERIC SO IT CAN BE RE-UTILIZED FOR PART 3(tier search)
    private var isSearching = false
    private var nextPage = 0
    private var currentSearchTerm: String? = null

    override fun getRefreshKey(state: PagingState<Int, PokemonSimpleListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonSimpleListItem> {
        val localSearchTerm = if (isSearching && searchTerm.isNotEmpty()) "%$searchTerm" else searchTerm
        val startsWith = if (isSearching && searchTerm.isNotEmpty()) searchTerm else ""

        // Reset nextPage only when the search term changes
        if (localSearchTerm != currentSearchTerm) {
            nextPage = 0
            currentSearchTerm = localSearchTerm
        }

        val currentNextPage = nextPage
        nextPage = currentNextPage + PAGE_SIZE
        // Attempt to load from the query
        val query = PokemonListGraphQlQuery(
            searchTerm = Optional.present(currentSearchTerm),
            notStartsWith = Optional.present(startsWith),
            offset = Optional.present(currentNextPage),
            pageSize = Optional.present(PAGE_SIZE)
        )

        val pokeList = response.invoke(query)?.pokemonItems ?: listOf()
        val prevKey = if (currentNextPage > 0) currentNextPage - PAGE_SIZE else null
        val nextKey = when{
            pokeList.size> PAGE_SIZE-1 ->{currentNextPage + PAGE_SIZE}
            pokeList.size < PAGE_SIZE-1 && searchTerm.isNotEmpty() &&!isSearching->{0}
            else-> null
        }

        if (pokeList.isEmpty() && searchTerm.isNotEmpty() && !isSearching) {
            // Switch to searching if no results found
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