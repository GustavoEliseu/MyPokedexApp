package com.gustavoeliseu.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gustavoeliseu.domain.utils.Const.PokemonData.POKEMON_TABLE_NAME
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import com.gustavoeliseu.pokedexdata.models.GenericPokemonData
import com.gustavoeliseu.pokedexdata.models.GenericPokemonDataList
import com.gustavoeliseu.pokedexdata.models.UrlGenericPokemonData
import java.io.Serializable


@Entity
data class PokemonSimpleListItem(
    override val name: String,
    @PrimaryKey
    override val id: Int,
    val pokemonColorId: Int? = null,
    var baseColor: Int? = null,
    var textColor: Int? = null
): GenericPokemonData(name,id){
        fun toPokemonDetails():PokemonDetails{
            return PokemonDetails(name = name, id = id, pokemonColorId = pokemonColorId, baseColor = baseColor, textColor = textColor)
        }
}

data class PokemonSimpleList(
    override val pokemonItems: List<PokemonSimpleListItem>
): GenericPokemonDataList(pokemonItems) {
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
    }
}
@Entity(tableName = POKEMON_TABLE_NAME)
data class PokemonDetails(
    val abilities: List<Ability> = listOf(),
    val baseExperience: Int = 0,
    val forms: List<UrlGenericPokemonData> = listOf(),
    val gameIndices: List<UrlGenericPokemonData> = listOf(),
    val height: Int= 0,
    val heldItems: List<HeldItems> = listOf(),
    @PrimaryKey
    val id: Int,
    val isDefault: Boolean = false,
    val moves: List<Moves> = listOf(),
    val name: String,
//    val sprites: Sprites,
    val stats: List<Stats> = listOf(),
    val types: List<Type> = listOf(),
    val weight: Int = 0,
    val pokemonColorId: Int? = null,
    var baseColor: Int? = null,
    var textColor: Int? = null,
    var hasDetails:Boolean = false
): Serializable

data class Ability(
    val ability: UrlGenericPokemonData,
    val isHidden: Boolean,
    val slot: Int
): Serializable

data class Stats(
    val baseStat: Int,
    val effort: Int,
    val stat: UrlGenericPokemonData
): Serializable

data class Moves(
    val move: UrlGenericPokemonData,
    val versionDetails: List<MoveVersionGroupDetails>
): Serializable

data class PastTypes(
    val generation: UrlGenericPokemonData,
    val types: List<Type>
): Serializable

data class Type(
    val slot: Int,
    val type: UrlGenericPokemonData
): Serializable

data class HeldItems(
    val item: UrlGenericPokemonData,
    val versionDetails: List<VersionDetails>
): Serializable

data class MoveVersionGroupDetails(
    val levelLearned: Int,
    val moveLearnMethod: UrlGenericPokemonData,
    val versionGroup: UrlGenericPokemonData
): Serializable

data class VersionDetails(
    val rarity: Int,
    val version: UrlGenericPokemonData
): Serializable

data class Sprites(
    val backDefault: String?,
    val backFemale: String?,
    val backShiny: String?,
    val backShinyFemale: String?,
    val frontDefault: String?,
    val frontFemale: String?,
    val frontShiny: String?,
    val frontShinyFemale: String?,
): Serializable