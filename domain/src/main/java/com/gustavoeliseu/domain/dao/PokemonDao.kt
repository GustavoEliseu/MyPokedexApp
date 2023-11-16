package com.gustavoeliseu.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gustavoeliseu.domain.models.PokemonDetails
import com.gustavoeliseu.domain.models.PokemonSimpleListItem

@Dao
interface PokemonDao {

    //No need for a delete function on this table

    @Query("SELECT name,id,pokemonColorId, baseColor, textColor,hasDetails FROM pokemon")
    suspend fun getAllSimpleDataPokemon(): List<PokemonSimpleListItem>

    @Query("SELECT name,id,pokemonColorId, baseColor, textColor,hasDetails FROM pokemon WHERE id= :id LIMIT 1")
    suspend fun getSingleSimpleDataPokemonFromId(id:Int): PokemonSimpleListItem

    @Query("SELECT * FROM pokemon")
    suspend fun getAllFullDataPokemon(): List<PokemonDetails>

    @Query("SELECT name,id,pokemonColorId, baseColor, textColor, hasDetails FROM pokemon WHERE id>= :startId AND name like :searchTerm and name not like :startsWith LIMIT :pageSize ")
    suspend fun getAllDataPokemonPaginatedSearch(searchTerm:String,startsWith:String,startId:Int, pageSize:Int): List<PokemonSimpleListItem>

    @Query("SELECT COUNT(id) FROM pokemon where id> :startId AND name like :searchTerm and name not like :startsWith LIMIT :pageSize")
    suspend fun getSearchPaginatedPokemonListCount(searchTerm:String = "%",startsWith:String = "",startId:Int, pageSize:Int): Int
    @Query("SELECT * FROM pokemon WHERE id = :pokeId limit 1")
    suspend fun getPokemonDetails(pokeId:Int): PokemonDetails

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAllPokemonSimple(list: List<PokemonDetails>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllPokemon(list: List<PokemonDetails>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPokemon(list: List<PokemonDetails>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePokemon(pokemon: PokemonDetails):Int
}