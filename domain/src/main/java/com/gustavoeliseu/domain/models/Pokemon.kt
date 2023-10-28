package com.gustavoeliseu.domain.models

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gustavoeliseu.domain.utils.Const.PokemonData.POKEMON_TABLE_NAME
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import com.gustavoeliseu.pokedexdata.models.GenericPokemonData
import com.gustavoeliseu.pokedexdata.models.GenericPokemonDataList
import com.gustavoeliseu.pokedexdata.models.UrlGenericPokemonData
import java.io.Serializable
import java.lang.ref.SoftReference
import javax.annotation.Nullable


data class PokemonSimpleListItem(
    override val name: String,
    @PrimaryKey
    override val id: Int,
    val pokemonColorId: Int? = null,
    var baseColor: Int? = null,
    var textColor: Int? = null,
    @Ignore
    var drawable: SoftReference<Drawable>? = null,
    var hasDetails:Boolean = false
) : GenericPokemonData(name, id) {
    constructor(
        name: String,
        id: Int,
        pokemonColorId: Int?,
        baseColor: Int?,
        textColor: Int?
    ) : this(name, id, pokemonColorId, baseColor, textColor, null)

    fun toPokemonDetails(): PokemonDetails {
        return PokemonDetails(
            name = name,
            id = id,
            pokemonColorId = pokemonColorId,
            baseColor = baseColor,
            textColor = textColor
        )
    }
}

data class PokemonSimpleList(
    override val pokemonItems: List<PokemonSimpleListItem>
) : GenericPokemonDataList(pokemonItems) {
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
            if (isPreview) {
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

@Entity(tableName = POKEMON_TABLE_NAME)
data class PokemonDetails @JvmOverloads constructor(
    val abilities: List<Ability> = listOf(),
    val baseExperience: Int = 0,
    val forms: List<UrlGenericPokemonData> = listOf(),
    val gameIndices: List<UrlGenericPokemonData> = listOf(),
    val height: Int = 0,
    val heldItems: List<HeldItems> = listOf(),
    @PrimaryKey
    val id: Int,
    val isDefault: Boolean = false,
    val moves: List<Moves> = listOf(),
    val name: String,
    val stats: List<Stats> = listOf(),
    val types: List<Type> = listOf(),
    val weight: Int = 0,
    val pokemonColorId: Int? = null,
    var baseColor: Int? = null,
    var textColor: Int? = null,
    var hasDetails: Boolean = false
) : Serializable{
    constructor(
        name: String,
        id: Int,
        pokemonColorId: Int?,
        baseColor: Int?,
        textColor: Int?,
        hasDetails: Boolean = false
    ) : this(name = name,
        id=id,
        pokemonColorId = pokemonColorId,
        baseColor= baseColor,
        textColor= textColor,
        hasDetails= hasDetails,
        height = 0)

    fun toPokemonSimple(): PokemonSimpleListItem {
        return PokemonSimpleListItem(
            name = name,
            id = id,
            pokemonColorId = pokemonColorId,
            baseColor = baseColor,
            textColor = textColor,
            hasDetails = hasDetails
        )
    }
}

data class Ability(
    val ability: UrlGenericPokemonData,
    val isHidden: Boolean,
    val slot: Int
) : Serializable

data class Stats(
    val baseStat: Int,
    val effort: Int,
    val stat: UrlGenericPokemonData
) : Serializable

data class Moves(
    val move: UrlGenericPokemonData,
    val versionDetails: List<MoveVersionGroupDetails>
) : Serializable

data class PastTypes(
    val generation: UrlGenericPokemonData,
    val types: List<Type>
) : Serializable

data class Type(
    val slot: Int,
    val type: UrlGenericPokemonData
) : Serializable

data class HeldItems(
    val item: UrlGenericPokemonData,
    val versionDetails: List<VersionDetails>
) : Serializable

data class MoveVersionGroupDetails(
    val levelLearned: Int,
    val moveLearnMethod: UrlGenericPokemonData,
    val versionGroup: UrlGenericPokemonData
) : Serializable

data class VersionDetails(
    val rarity: Int,
    val version: UrlGenericPokemonData
) : Serializable

data class Sprites(
    val backDefault: String?,
    val backFemale: String?,
    val backShiny: String?,
    val backShinyFemale: String?,
    val frontDefault: String?,
    val frontFemale: String?,
    val frontShiny: String?,
    val frontShinyFemale: String?,
) : Serializable