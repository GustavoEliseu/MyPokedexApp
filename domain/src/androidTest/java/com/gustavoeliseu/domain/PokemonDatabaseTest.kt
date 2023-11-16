@file:Suppress("SpellCheckingInspection")

package com.gustavoeliseu.domain

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.gustavoeliseu.domain.dao.PokemonDao
import com.gustavoeliseu.domain.database.PokemonDatabase
import com.gustavoeliseu.domain.models.PokemonDetails
import com.gustavoeliseu.domain.models.PokemonSimpleListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import okhttp3.internal.wait
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class PokemonDatabaseTest {

    private lateinit var pokemonDao: PokemonDao
    private lateinit var db: PokemonDatabase
    private val testPokemonArray = arrayOf(
        "Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon",
        "Charizard", "Squirtle", "Wartortle", "Blastoise", "Caterpie",
        "Metapod", "Butterfree", "Weedle", "Kakuna", "Beedrill",
        "Pidgey", "Pidgeotto", "Pidgeot", "Rattata", "Raticate"
    )
    private val testPokemonArray2 = arrayOf(
        "Bulbasaur2", "Ivysaur2", "Venusaur2", "Charmander2", "Charmeleon2",
        "Charizard2", "Squirtle2", "Wartortle2", "Blastoise2", "Caterpie2",
        "Metapod2", "Butterfree2", "Weedle2", "Kakuna2", "Beedrill2",
        "Pidgey2", "Pidgeotto2", "Pidgeot2", "Rattata2", "Raticate2"
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, PokemonDatabase::class.java
        ).build()
        pokemonDao = db.pokemonDao()

        val pokemon = mutableListOf<PokemonDetails>()
        for (i in 1..20) {
            pokemon.add(
                PokemonDetails(
                    name = testPokemonArray[i - 1], id = i
                )
            )
        }
        CoroutineScope(Dispatchers.IO).launch {
            pokemonDao.addAllPokemon(pokemon)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun save_SimplePokemonList_And_Load_All() {
        db.clearAllTables()
        val pokemonList = mutableListOf<PokemonDetails>()
        for (i in 1..20) {
            pokemonList.add(
                PokemonDetails(
                    name = testPokemonArray[i - 1], id = i
                )
            )
        }
        val syncObject = Any()
        CoroutineScope(Dispatchers.IO).launch {
            pokemonDao.addAllPokemon(pokemonList)
            val resultPokemon = pokemonDao.getAllSimpleDataPokemon()
            Truth.assertThat(resultPokemon).isNotNull()
            Truth.assertThat(resultPokemon.size).isGreaterThan(0)
            resultPokemon.forEachIndexed { index, pokemon ->
                Truth.assertThat(pokemon).isEqualTo(
                    PokemonSimpleListItem(
                        testPokemonArray[index],
                        index + 1
                    )
                )
            }
            synchronized (syncObject){
                syncObject.notify();
            }
        }
        synchronized (syncObject){
            syncObject.wait();
        }
    }

    @Test
    fun get_pokemonList_from_5_to_10() {
        val pageStart = 5
        val pageSize = 5
        val syncObject = Any()
        CoroutineScope(Dispatchers.IO).launch {
            val resultPokemon =
                pokemonDao.getAllDataPokemonPaginatedSearch("%", "", pageStart, pageSize)

            Truth.assertThat(resultPokemon).isNotNull()
            Truth.assertThat(resultPokemon.size).isGreaterThan(0)
            Truth.assertThat(resultPokemon.size).isEqualTo(pageSize)
            resultPokemon.forEachIndexed { index, pokemon ->
                Truth.assertThat(pokemon).isEqualTo(
                    PokemonSimpleListItem(
                        testPokemonArray[pageStart + index - 1],
                        pageStart + index
                    )
                )
            }
            synchronized (syncObject){
                syncObject.notify();
            }
        }
        synchronized (syncObject){
            syncObject.wait();
        }
    }

    @Test
    fun get_pokemon_paginated_with_search() {
        val searchTerm = "pi%"
        val expetedResult = listOf(
            PokemonSimpleListItem(name = "Pidgey", id = 16),
            PokemonSimpleListItem(name = "Pidgeotto", id = 17),
            PokemonSimpleListItem(name = "Pidgeot", id = 18),
        )
        val syncObject = Any()
        CoroutineScope(Dispatchers.IO).launch {
            val resultPokemon = pokemonDao.getAllDataPokemonPaginatedSearch(searchTerm, "", 0, 5)

            Truth.assertThat(resultPokemon).isNotNull()
            Truth.assertThat(resultPokemon.size).isGreaterThan(0)
            resultPokemon.forEachIndexed { index, pokemon ->
                Truth.assertThat(pokemon).isEqualTo(
                    expetedResult[index]
                )
            }
            synchronized (syncObject){
                syncObject.notify();
            }
        }
        synchronized (syncObject){
            syncObject.wait();
        }
    }

    @Test
    fun get_pokemon_paginated_with_search_not_start_with() {
        val searchTerm = "pi%"
        val expetedResult = listOf(
            PokemonSimpleListItem(name = "Caterpie", id = 10),
        )
        val syncObject= Any()
        CoroutineScope(Dispatchers.IO).launch {
            val resultPokemon = pokemonDao.getAllDataPokemonPaginatedSearch(
                "%$searchTerm",
                searchTerm,
                0,
                5
            )

            Truth.assertThat(resultPokemon).isNotNull()
            Truth.assertThat(resultPokemon.size).isGreaterThan(0)
            resultPokemon.forEachIndexed { index, pokemon ->
                Truth.assertThat(pokemon).isEqualTo(
                    expetedResult[index]
                )
            }
            synchronized (syncObject){
                syncObject.notify();
            }
        }
        synchronized (syncObject){
            syncObject.wait();
        }
    }

    @Test
    fun get_pokemon_paginated_with_search_then_with_not_start_with() {
        val searchTerm = "pi%"
        val expetedResult = listOf(
            PokemonSimpleListItem(name = "Pidgey", id = 16),
            PokemonSimpleListItem(name = "Pidgeotto", id = 17),
            PokemonSimpleListItem(name = "Pidgeot", id = 18),
            PokemonSimpleListItem(name = "Caterpie", id = 10),
        )
        val syncObject = Any()
        CoroutineScope(Dispatchers.IO).launch {
            val resultPokemon =
                pokemonDao.getAllDataPokemonPaginatedSearch(searchTerm, "", 0, 5).toMutableList()
            resultPokemon.addAll(
                pokemonDao.getAllDataPokemonPaginatedSearch(
                    "%$searchTerm",
                    searchTerm,
                    0,
                    5
                )
            )

            Truth.assertThat(resultPokemon).isNotNull()
            Truth.assertThat(resultPokemon.size).isGreaterThan(0)
            resultPokemon.forEachIndexed { index, pokemon ->
                Truth.assertThat(pokemon).isEqualTo(
                    expetedResult[index]
                )
            }
            synchronized (syncObject){
                syncObject.notify();
            }
        }
        synchronized (syncObject){
            syncObject.wait();
        }
    }

    @Test
    fun update_pokemonDetails_then_get_the_updated_value() {
        val syncObject = Any()
        CoroutineScope(Dispatchers.IO).launch {
            val expetedResult =
                PokemonDetails(
                    name = "Pidgey",
                    id = 16,
                    height = 10,
                    weight = 15,
                    hasDetails = true
                )

            val beforeChangePokemon = pokemonDao.getPokemonDetails(16)
            pokemonDao.updatePokemon(
                PokemonDetails(
                    name = "Pidgey",
                    id = 16,
                    height = 10,
                    weight = 15,
                    hasDetails = true
                )
            )
            val resultPokemon = pokemonDao.getPokemonDetails(16)

            Truth.assertThat(resultPokemon).isNotNull()
            Truth.assertThat(resultPokemon).isNotEqualTo(beforeChangePokemon)
            Truth.assertThat(resultPokemon).isEqualTo(expetedResult)
            synchronized (syncObject){
                syncObject.notify();
            }
        }
        synchronized (syncObject){
            syncObject.wait();
        }
    }

    @Test
    fun check_if_pagination_works_correctly() {
        val pokemon = mutableListOf<PokemonDetails>()
        for (i in 1..20) {
            pokemon.add(
                PokemonDetails(
                    name = testPokemonArray2[i-1], id = i+testPokemonArray.size
                )
            )
        }
        val syncObject = Any()

        CoroutineScope(Dispatchers.IO).launch {
                pokemonDao.addAllPokemon(pokemon)

                var pageStart = 1
                val pageSize = 5
                while (pageStart < testPokemonArray.size + testPokemonArray2.size) {
                    val resultPokemon =
                        pokemonDao.getAllDataPokemonPaginatedSearch("%", "", pageStart, pageSize)

                    Truth.assertThat(resultPokemon).isNotNull()
                    Truth.assertThat(resultPokemon.size).isGreaterThan(0)
                    Truth.assertThat(resultPokemon.size).isEqualTo(pageSize)
                    resultPokemon.forEachIndexed { index, pokemon ->
                        Truth.assertThat(pokemon).isEqualTo(
                            PokemonSimpleListItem(
                                if (pageStart + index <= testPokemonArray.size)
                                    testPokemonArray[pageStart + index-1]
                                else testPokemonArray2[pageStart + index -1 - testPokemonArray.size],
                                pageStart + index
                            )
                        )
                    }
                    pageStart += pageSize
                }
            synchronized (syncObject){
                syncObject.notify();
            }
        }
        synchronized(syncObject){
            syncObject.wait()
        }
    }

    @Test
    fun get_single_simple_pokemon_from_id() {
        val syncObject = Any()
        val searchId = 3
        val expectedResult =PokemonSimpleListItem(testPokemonArray[searchId-1], searchId)
        CoroutineScope(Dispatchers.IO).launch {
            val resultPokemon =
                pokemonDao.getSingleSimpleDataPokemonFromId(searchId)


            Truth.assertThat(resultPokemon).isNotNull()
            Truth.assertThat(resultPokemon.id).isEqualTo(expectedResult.id)
            synchronized (syncObject){
                syncObject.notify();
            }
        }
        synchronized(syncObject){
            syncObject.wait()
        }
    }

    @Test
    fun check_if_pokemon_details_exists() {
        CoroutineScope(Dispatchers.IO).launch {
            val resultPokemon = pokemonDao.getPokemonDetails(16)
            Truth.assertThat(resultPokemon).isNotNull()
        }
    }
}