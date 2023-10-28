package com.gustavoeliseu.domain.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.logCacheMisses
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.cache.normalized.refetchPolicy
import com.apollographql.apollo3.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo3.network.okHttpClient
import com.gustavoeliseu.domain.BuildConfig
import com.gustavoeliseu.domain.BuildConfig.GRAPHQLAPI_URL
import com.gustavoeliseu.domain.database.PokemonDatabase
import com.gustavoeliseu.domain.repository.GenericPokemonListRepositoryImpl
import com.gustavoeliseu.domain.utils.Const.PAGE_SIZE
import com.gustavoeliseu.domain.utils.Const.PAGE_SIZE_TEXT
import com.gustavoeliseu.domain.utils.Const.WEB_API
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModules {

    @Provides
    @Named(WEB_API)
    fun provideWebAPI(): String = GRAPHQLAPI_URL

    @Provides
    @Named(PAGE_SIZE_TEXT)
    fun providePageSize(): Int = PAGE_SIZE

    @Provides
    @Singleton
    fun okhttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
            .apply { level = BuildConfig.INTERCEPTOR_LEVEL }
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(logging)
            .followRedirects(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideApollo(client: OkHttpClient): ApolloClient {
        val memCacheFactory = MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024)

        val sqlNormalizedCacheFactory = SqlNormalizedCacheFactory("apollo.db")

        val inMemoryThenSqliteCache = memCacheFactory.chain(sqlNormalizedCacheFactory)

        return ApolloClient.Builder()
            .serverUrl(GRAPHQLAPI_URL)
            .logCacheMisses()
            .normalizedCache(
                normalizedCacheFactory = inMemoryThenSqliteCache,
            )
            .okHttpClient(client)
            .refetchPolicy(FetchPolicy.CacheFirst)
            .build()
    }

    class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
            return chain.proceed(request.build())
        }
    }

    @Provides
    fun providePokemonRepository(
        apolloClient: ApolloClient,
        @Named(PAGE_SIZE_TEXT) pageSize: Int,
        pokemonDatabase: PokemonDatabase
    ): com.gustavoeliseu.pokedexdata.repository.GenericPokemonListRepository = GenericPokemonListRepositoryImpl(apolloClient,pokemonDatabase, pageSize)
}