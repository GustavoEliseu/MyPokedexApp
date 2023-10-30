package com.gustavoeliseu.domain.utils.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gustavoeliseu.domain.models.Ability
import com.gustavoeliseu.domain.models.HeldItems
import com.gustavoeliseu.domain.models.Moves
import com.gustavoeliseu.domain.models.PokemonDetails
import com.gustavoeliseu.domain.models.PokemonSimpleListItem
import com.gustavoeliseu.domain.models.PokemonSpecy
import com.gustavoeliseu.domain.models.Stats
import com.gustavoeliseu.domain.models.Type
import com.gustavoeliseu.pokedexdata.models.UrlGenericPokemonData

class PokemonTypeConverters {

    @TypeConverter
    fun fromPokemonSimpleListItemString(value: String): List<PokemonSimpleListItem> {
        val listType = object : TypeToken<List<PokemonSimpleListItem>>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromPokemonSimpleListItemList(list: List<PokemonSimpleListItem>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromPokemonDetailsString(value: String): PokemonDetails {
        val listType = object : TypeToken<PokemonDetails>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromPokemonDetailsList(pokemon: PokemonDetails): String {
        val gson = Gson()
        return gson.toJson(pokemon)
    }

    @TypeConverter
    fun fromPokemonAbilityListString(value: String): List<Ability> {
        val listType = object : TypeToken<List<Ability>>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromPokemonAbilityList(pokemonAbility: List<Ability>): String {
        val gson = Gson()
        return gson.toJson(pokemonAbility)
    }

    @TypeConverter
    fun fromPokemonHeldItemsListString(value: String): List<HeldItems> {
        val listType = object : TypeToken<List<HeldItems>>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromPokemonHeldItemsList(pokemonAbility: List<HeldItems>): String {
        val gson = Gson()
        return gson.toJson(pokemonAbility)
    }

    @TypeConverter
    fun fromPokemonMovesString(value: String): List<Moves> {
        val listType = object : TypeToken<List<Moves>>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromPokemonMovesList(pokemonMoves: List<Moves>): String {
        val gson = Gson()
        return gson.toJson(pokemonMoves)
    }

    @TypeConverter
    fun fromPokemonStatsListString(value: String): List<Stats> {
        val listType = object : TypeToken<List<Stats>>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromPokemonStatsList(pokemonAbility: List<Stats>): String {
        val gson = Gson()
        return gson.toJson(pokemonAbility)
    }

    @TypeConverter
    fun fromPokemonTypeListString(value: String): List<Type> {
        val listType = object : TypeToken<List<Type>>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromPokemonTypeList(pokemonType: List<Type>): String {
        val gson = Gson()
        return gson.toJson(pokemonType)
    }

    //Generic data
    @TypeConverter
    fun fromUrlGenericPokemonDataString(value: String): UrlGenericPokemonData {
        val listType = object : TypeToken<UrlGenericPokemonData>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromUrlGenericPokemonData(data: UrlGenericPokemonData): String {
        val gson = Gson()
        return gson.toJson(data)
    }

    @TypeConverter
    fun fromUrlGenericPokemonDataListString(value: String): List<UrlGenericPokemonData> {
        val listType = object : TypeToken<List<UrlGenericPokemonData>>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromUrlGenericPokemonDataList(data: List<UrlGenericPokemonData>): String {
        val gson = Gson()
        return gson.toJson(data)
    }

    @TypeConverter
    fun fromStringToPokemonSpecyList(value: String): List<PokemonSpecy> {
        val listType = object : TypeToken<List<PokemonSpecy>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromPokemonSpecyListToString(data: List<PokemonSpecy>): String {
        val gson = Gson()
        return gson.toJson(data)
    }

    @TypeConverter
    fun fromStringToPokemonSpecy(value: String?): PokemonSpecy? {
        val listType = object : TypeToken<PokemonSpecy?>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromPokemonSpecyToString(data: PokemonSpecy?): String? {
        val gson = Gson()
        return gson.toJson(data)
    }
}