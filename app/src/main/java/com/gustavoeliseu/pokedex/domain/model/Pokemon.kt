package com.gustavoeliseu.pokedex.domain.model

import com.google.gson.annotations.SerializedName

//used for ANY data with name and URL only
data class SimpleGenericPokemonData(
    @field:SerializedName("name")
    val name:String,
    @field:SerializedName("url")
    val url: String
)

data class PokemonDetails(
    @field:SerializedName("abilities")
    val abilities: List<Ability>,
    @field:SerializedName("base_experience")
    val baseExperience: Int,
    @field:SerializedName("forms")
    val forms: List<SimpleGenericPokemonData>,
    @field:SerializedName("game_indices")
    val gameIndices : List<SimpleGenericPokemonData>,
    @field:SerializedName("height")
    val height : Int,
    @field:SerializedName("held_items")
    val heldItems: List<HeldItems>,
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("is_default")
    val isDefault: Boolean,
    @field:SerializedName("location_area_encounters")
    val locationAreaEncounters: String,
    @field:SerializedName("moves")
    val moves: List<Moves>,
    @field:SerializedName("name")
    val name:String,
    @field:SerializedName("order")
    val order:Int,
    @field:SerializedName("past_types")
    val pastTypes: List<PastTypes>,
    @field:SerializedName("species")
    val species: SimpleGenericPokemonData,
    @field:SerializedName("sprites")
    val sprites: Sprites,
    @field:SerializedName("stats")
    val stats: List<Stats>,
    @field:SerializedName("types")
    val types: List<Type>,
    @field:SerializedName("weight")
    val weight: Int
)
data class Ability(
    @field:SerializedName("ability")
    val ability: SimpleGenericPokemonData,
    @field:SerializedName("is_hidden")
    val isHidden: Boolean,
    @field:SerializedName("slot")
    val slot: Int
)

data class Stats(
    @field:SerializedName("base_stat")
    val baseStat: Int,
    @field:SerializedName("effort")
    val effort: Int,
    @field:SerializedName("stat")
    val stat: SimpleGenericPokemonData
)

data class Moves(
    @field:SerializedName("move")
    val move: SimpleGenericPokemonData,
    @field:SerializedName("version_group_details")
    val versionDetails: List<MoveVersionGroupDetails>
)

data class PastTypes(
    @field:SerializedName("generation")
    val generation: SimpleGenericPokemonData,
    @field:SerializedName("types")
    val types: List<Type>
)

data class Type(
    @field:SerializedName("slot")
    val slot: Int,
    @field:SerializedName("type")
    val type: SimpleGenericPokemonData
)

data class HeldItems(
    @field:SerializedName("item")
    val item: SimpleGenericPokemonData,
    @field:SerializedName("version_details")
    val versionDetails: List<VersionDetails>
)

data class MoveVersionGroupDetails(
    @field:SerializedName("level_learned_at")
    val levelLearned: Int,
    @field:SerializedName("move_learn_method")
    val moveLearnMethod: SimpleGenericPokemonData,
    @field:SerializedName("version_group")
    val versionGroup: SimpleGenericPokemonData
)

data class VersionDetails(
    @field:SerializedName("rarity")
    val rarity: Int,
    @field:SerializedName("version")
    val version: SimpleGenericPokemonData
)

data class Sprites(
    @field:SerializedName("back_default")
    val backDefault: String?,
    @field:SerializedName("back_female")
    val backFemale: String?,
    @field:SerializedName("back_shiny")
    val backShiny: String?,
    @field:SerializedName("back_shiny_female")
    val backShinyFemale: String?,
    @field:SerializedName("front_default")
    val frontDefault: String?,
    @field:SerializedName("front_female")
    val frontFemale: String?,
    @field:SerializedName("front_shiny")
    val frontShiny: String?,
    @field:SerializedName("front_shiny_female")
    val frontShinyFemale: String?,
)