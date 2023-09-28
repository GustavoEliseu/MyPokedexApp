package com.gustavoeliseu.pokedex.di

import com.gustavoeliseu.pokedex.BuildConfig
import com.gustavoeliseu.pokedex.domain.repository.PokemonRepository
import com.gustavoeliseu.pokedex.network.PokemonApi
import com.gustavoeliseu.pokedex.network.repository.PokemonRepositoryImpl
import com.gustavoeliseu.pokedex.utils.Const.PAGE_SIZE
import com.gustavoeliseu.pokedex.utils.Const.PAGE_SIZE_TEXT
import com.gustavoeliseu.pokedex.utils.Const.WEB_API
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
class AppModules {

    @Provides
    @Named(WEB_API)
    fun provideWebAPI(): String = BuildConfig.BASE_URL

    @Provides
    @Named("PAGE_SIZE")
    fun providePageSize(): Int = PAGE_SIZE

    @Provides
    fun provideRetrofit(
        @Named(WEB_API) webApi: String): Retrofit {
        val client = OkHttpClient
            .Builder()
            .addInterceptor(HttpLoggingInterceptor()
            .setLevel(BuildConfig.INTERCEPTOR_LEVEL))

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesPokemonApi(
        retrofit: Retrofit
    ): PokemonApi = retrofit.create(PokemonApi::class.java)

    @Provides
    fun providePokemonRepository(
        pokemonApi: PokemonApi,
        @Named(PAGE_SIZE_TEXT)pageSize:Int,
    ): PokemonRepository = PokemonRepositoryImpl(pokemonApi,pageSize)

}