package com.gustavoeliseu.pokedex.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.gustavoeliseu.pokedex.BuildConfig
import com.gustavoeliseu.pokedex.BuildConfig.GRAPHQLAPI_URL
import com.gustavoeliseu.pokedex.domain.repository.PokemonRepository
import com.gustavoeliseu.pokedex.network.repository.PokemonRepositoryImpl
import com.gustavoeliseu.pokedex.utils.Const.PAGE_SIZE
import com.gustavoeliseu.pokedex.utils.Const.PAGE_SIZE_TEXT
import com.gustavoeliseu.pokedex.utils.Const.WEB_API
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
class AppModules {

    @Provides
    @Named(WEB_API)
    fun provideWebAPI(): String = GRAPHQLAPI_URL

    @Provides
    @Named("PAGE_SIZE")
    fun providePageSize(): Int = PAGE_SIZE

    @Provides
    @Singleton
    fun okhttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
            .apply { level = BuildConfig.INTERCEPTOR_LEVEL }
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideApollo(client: OkHttpClient): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(GRAPHQLAPI_URL)
            .okHttpClient(client).build()
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
    ): PokemonRepository = PokemonRepositoryImpl(apolloClient, pageSize)

}