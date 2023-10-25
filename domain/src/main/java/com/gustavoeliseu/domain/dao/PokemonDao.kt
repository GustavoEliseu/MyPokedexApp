package com.gustavoeliseu.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gustavoeliseu.domain.models.PokemonDetails
import com.gustavoeliseu.domain.models.PokemonSimpleListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

//    @Query("SELECT * FROM pokemon")
//    fun getAllDataPokemon(): List<T>

    @Query("SELECT name,id,pokemonColorId, baseColor, textColor FROM pokemon")
    fun getAllSimpleDataPokemon(): List<PokemonSimpleListItem>

    @Query("SELECT * FROM pokemon")
    fun getAllFullDataPokemon(): List<PokemonDetails>

    @Query("SELECT name,id,pokemonColorId, baseColor, textColor FROM pokemon WHERE id>= :startId AND name like :searchTerm and name not like :startsWith LIMIT :pageSize ")
    fun getAllDataPokemonPaginatedSearch(searchTerm:String,startsWith:String,startId:Int, pageSize:Int): List<PokemonSimpleListItem>

    @Query("SELECT COUNT(id) FROM pokemon where id>= :startId AND name like :searchTerm and name not like :startsWith LIMIT :pageSize")
    fun checkIfPokemonExists(startId:Int, pageSize:Int,searchTerm:String = "%",startsWith:String = ""): Int
    @Query("SELECT * FROM pokemon WHERE id = :pokeId limit 1")
    fun getPokemonDetails(pokeId:Int): PokemonDetails

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAllPokemon(list: List<PokemonDetails>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPokemon(list: List<PokemonDetails>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePokemon(pokemon: PokemonDetails)
}