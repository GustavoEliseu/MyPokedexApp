package com.gustavoeliseu.domain

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.gustavoeliseu.domain.dao.PokemonDao
import com.gustavoeliseu.domain.database.PokemonDatabase
import com.gustavoeliseu.domain.models.PokemonDetails
import com.gustavoeliseu.domain.models.PokemonEvolutionChain
import com.gustavoeliseu.domain.models.PokemonSpecy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import okhttp3.internal.wait
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PokemonDetailsDatabaseTest {
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
    fun update_database_bulbasaur() {
        val syncObject = Any()
        CoroutineScope(Dispatchers.IO).launch {
            val beforePokemon =
                pokemonDao.getPokemonDetails( 1)

            val expectedResult = PokemonDetails(
                name = "Bulbasaur",
                baseExperience = 0,
                abilities = listOf(),
                id = 1,
                heldItems = listOf(),
                isDefault = true,
                height = 2,
                weight = 2,
                stats = listOf(),
                types = listOf(),
                specy = PokemonSpecy(
                    evolvesFromId = null,
                    legendary = false,
                    mythical = false,
                    baby = false,
                    evolutionChain =
                    PokemonEvolutionChain(
                        listOf(
                            PokemonSpecy(
                                evolvesFromId = 1,
                                legendary = false,
                                mythical = false,
                                baby = false,
                                name = "Ivysaur",
                                id = 2,
                                evolutionChain = null,
                                pokemonColorId = null
                            ),
                            PokemonSpecy(
                                evolvesFromId = 2,
                                legendary = false,
                                mythical = false,
                                baby = false,
                                name = "Venusaur",
                                id = 3,
                                evolutionChain = null,
                                pokemonColorId = null
                            )
                        )),
                    name = "Bulbasaur",
                    id = 1,
                    pokemonColorId = null
                ),
                hasDetails = true,
            )

            pokemonDao.updatePokemon(expectedResult)

            val resultPokemon = pokemonDao.getPokemonDetails(1)

            Truth.assertThat(resultPokemon).isNotNull()
            Truth.assertThat(resultPokemon).isNotEqualTo(beforePokemon)
            Truth.assertThat(resultPokemon).isEqualTo(expectedResult)
            synchronized (syncObject){
                syncObject.notify();
            }
        }
        synchronized (syncObject){
            syncObject.wait();
        }
    }
}