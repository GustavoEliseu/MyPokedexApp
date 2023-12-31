package com.gustavoeliseu.domain.entity

import android.graphics.drawable.Drawable
import androidx.compose.ui.platform.LocalInspectionMode
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import java.lang.ref.SoftReference

//used for ANY data with name and URL only
data class SimpleGenericPokemonData(
    val name: String?,
    val url: String
)

data class PokemonSimpleListItem(
    val name: String,
    val id: Int,
    val pokemonColorId: Int?,
    var baseColor: Int? = null,  // Nullable mutable field
    var textColor: Int? = null,
    var drawable: SoftReference<Drawable>? = null
)

data class PokemonSimpleList(
    val pokemonItems: List<PokemonSimpleListItem>
) {
    companion object {
        fun PokemonListGraphQlQuery.Data.toSimplePokemonList(): PokemonSimpleList {
            return PokemonSimpleList(
                this.pokemonItem.map {
                    PokemonSimpleListItem(
                        it.name,
                        it.id,
                        it.pokemon_color_id
                    )
                })
        }

        fun getSimpleListExample(isPreview: Boolean): MutableList<PokemonSimpleListItem> {
            val pokeList = mutableListOf<PokemonSimpleListItem>()
            if(isPreview){
                for (i in 1..10) {
                    pokeList.add(
                        PokemonSimpleListItem(
                            id = -1,
                            name = "Missingno",
                            pokemonColorId = 0
                        )
                    )
                }
            }
            for (i in 1..2) {
                pokeList.add(
                    PokemonSimpleListItem(
                        id = 132,
                        name = "Ditto",
                        pokemonColorId = 7
                    )
                )
                pokeList.add(
                    PokemonSimpleListItem(
                        id = 10,
                        name = "Caterpie",
                        pokemonColorId = 5
                    )
                )
                pokeList.add(
                    PokemonSimpleListItem(
                        id = 18,
                        name = "Pidgeot",
                        pokemonColorId = 3
                    )
                )
                pokeList.add(
                    PokemonSimpleListItem(
                        id = 6,
                        name = "Charizard",
                        pokemonColorId = 8
                    )
                )
                pokeList.add(
                    PokemonSimpleListItem(
                        id = 43,
                        name = "Oddish",
                        pokemonColorId = 2
                    )
                )
                pokeList.add(
                    PokemonSimpleListItem(
                        id = 82,
                        name = "Magneton",
                        pokemonColorId = 4
                    )
                )
            }
            return pokeList
        }
    }
}


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