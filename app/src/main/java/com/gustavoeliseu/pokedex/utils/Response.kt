package com.gustavoeliseu.pokedex.utils

import com.apollographql.apollo3.api.ApolloResponse

sealed class Response<out T> {
    //TODO REMOVE THIS CLASS
    object Loading: Response<Nothing>()

    data class Success<out T>(
        val data: T?
    ): Response<T>()

    data class Failure(
        val e: Exception?
    ): Response<Nothing>()
}

