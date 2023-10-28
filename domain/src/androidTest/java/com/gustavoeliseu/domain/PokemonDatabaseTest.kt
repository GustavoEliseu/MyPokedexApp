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
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class PokemonDatabaseTest {

    //TODO ADD ASYNC TO DATABASE AND TESTS

    private lateinit var pokemonDao: PokemonDao
    private lateinit var db: PokemonDatabase
    private val testPokemonArray = arrayOf(
        "Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon",
        "Charizard", "Squirtle", "Wartortle", "Blastoise", "Caterpie",
        "Metapod", "Butterfree", "Weedle", "Kakuna", "Beedrill",
        "Pidgey", "Pidgeotto", "Pidgeot", "Rattata", "Raticate"
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
        }
    }

    @Test
    fun get_pokemonList_from_5_to_10() {
        val pageStart = 5
        val pageSize = 5
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
        CoroutineScope(Dispatchers.IO).launch {
            val resultPokemon = pokemonDao.getAllDataPokemonPaginatedSearch(searchTerm, "", 0, 5)

            Truth.assertThat(resultPokemon).isNotNull()
            Truth.assertThat(resultPokemon.size).isGreaterThan(0)
            resultPokemon.forEachIndexed { index, pokemon ->
                Truth.assertThat(pokemon).isEqualTo(
                    expetedResult[index]
                )
            }
        }
    }

    @Test
    fun get_pokemon_paginated_with_search_not_start_with() {
        val searchTerm = "pi%"
        val expetedResult = listOf(
            PokemonSimpleListItem(name = "Caterpie", id = 10),
        )
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
        }
    }

    @Test
    fun update_pokemonDetails_then_get_the_updated_value() {
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