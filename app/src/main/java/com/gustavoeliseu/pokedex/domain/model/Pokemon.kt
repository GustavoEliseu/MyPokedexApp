package com.gustavoeliseu.pokedex.domain.model

//used for ANY data with name and URL only
data class SimpleGenericPokemonData(
    val name: String?,
    val url: String
)

data class NewPokemonListResponse(
    val pokemonList: List<NewPokemonListData>
)

data class NewPokemonListData(
    val name: String,
    val id: Int,
    val colorId: Int
)

data class PokemonSpecieDetails(

    val baseHappiness: Int,
    val captureRate: Int,
    val eggGroup: SimpleGenericPokemonData?,
    val evolutionChain: SimpleGenericPokemonData?,
    val evolvesFromSpecie: SimpleGenericPokemonData?,
    val isBaby: Boolean,
    val isLegendary: Boolean,
    val isMythical: Boolean,
    val name: String,
    val id: Int,
    val hasGenderDifferences: Boolean,
    val generation: SimpleGenericPokemonData
)

data class PokemonDetails(
    val abilities: List<Ability>,
    val baseExperience: Int,
    val forms: List<SimpleGenericPokemonData>,
    val gameIndices: List<SimpleGenericPokemonData>,
    val height: Int,
    val heldItems: List<HeldItems>,
    val id: Int,
    val isDefault: Boolean,
    val locationAreaEncounters: String,
    val moves: List<Moves>,
    val name: String,
    val order: Int,
    val pastTypes: List<PastTypes>,
    val species: SimpleGenericPokemonData,
    val sprites: Sprites,
    val stats: List<Stats>,
    val types: List<Type>,
    val weight: Int
)

data class Ability(
    val ability: SimpleGenericPokemonData,
    val isHidden: Boolean,
    val slot: Int
)

data class Stats(
    val baseStat: Int,
    val effort: Int,
    val stat: SimpleGenericPokemonData
)

data class Moves(
    val move: SimpleGenericPokemonData,
    val versionDetails: List<MoveVersionGroupDetails>
)

data class PastTypes(
    val generation: SimpleGenericPokemonData,
    val types: List<Type>
)

data class Type(
    val slot: Int,
    val type: SimpleGenericPokemonData
)

data class HeldItems(
    val item: SimpleGenericPokemonData,
    val versionDetails: List<VersionDetails>
)

data class MoveVersionGroupDetails(
    val levelLearned: Int,
    val moveLearnMethod: SimpleGenericPokemonData,
    val versionGroup: SimpleGenericPokemonData
)

data class VersionDetails(
    val rarity: Int,
    val version: SimpleGenericPokemonData
)

data class Sprites(
    val backDefault: String?,
    val backFemale: String?,
    val backShiny: String?,
    val backShinyFemale: String?,
    val frontDefault: String?,
    val frontFemale: String?,
    val frontShiny: String?,
    val frontShinyFemale: String?,
)