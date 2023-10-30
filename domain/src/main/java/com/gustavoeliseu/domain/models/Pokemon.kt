package com.gustavoeliseu.domain.models

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gustavoeliseu.domain.models.Ability.Companion.toAbility
import com.gustavoeliseu.domain.models.HeldItems.Companion.toHeldItem
import com.gustavoeliseu.domain.models.PokemonSpecy.Companion.toSpecy
import com.gustavoeliseu.domain.models.Stats.Companion.toStats
import com.gustavoeliseu.domain.models.Type.Companion.toType
import com.gustavoeliseu.domain.utils.Const.PokemonData.POKEMON_TABLE_NAME
import com.gustavoeliseu.pokedex.GetPokemonDetailsQuery
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import com.gustavoeliseu.pokedexdata.models.GenericPokemonData
import com.gustavoeliseu.pokedexdata.models.GenericPokemonDataList
import com.gustavoeliseu.pokedexdata.models.UrlGenericPokemonData
import java.io.Serializable
import java.lang.ref.SoftReference


data class PokemonSimpleListItem(
    override val name: String,
    @PrimaryKey
    override val id: Int,
    val pokemonColorId: Int? = null,
    var baseColor: Int? = null,
    var textColor: Int? = null,
    @Ignore
    var drawable: SoftReference<Drawable>? = null,
    var hasDetails: Boolean = false
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
    val baseExperience: Int? = null,
    override val name: String,
    val height: Int? = null,
    val weight: Int? = null,
    @PrimaryKey
    override val id: Int,
    val heldItems: List<HeldItems> = listOf(),
    val isDefault: Boolean = false,
    val stats: List<Stats> = listOf(),
    val types: List<Type> = listOf(),
    val specy: PokemonSpecy? = null,
    val pokemonColorId: Int? = null,
    var baseColor: Int? = null,
    var textColor: Int? = null,
    var hasDetails: Boolean = false
) : Serializable, GenericPokemonData(name,id) {
    constructor(
        name: String,
        id: Int,
        pokemonColorId: Int?,
        baseColor: Int?,
        textColor: Int?,
        hasDetails: Boolean = false
    ) : this(
        name = name,
        id = id,
        pokemonColorId = pokemonColorId,
        baseColor = baseColor,
        textColor = textColor,
        hasDetails = hasDetails,
        height = 0
    )

    companion object {
        fun GetPokemonDetailsQuery.PokemonItem?.toPokemonDetails(): PokemonDetails? {
            return if (this != null) PokemonDetails(
                name = name,
                baseExperience = base_experience,
                abilities = pokemon_v2_pokemonabilities.toAbility(),
                id = id,
                heldItems = pokemon_v2_pokemonitems.toHeldItem(),
                isDefault = is_default,
                height = height,
                weight = weight,
                stats = pokemon_v2_pokemonstats.toStats(),
                types = pokemon_v2_pokemontypes.toType(),
                specy = pokemon_v2_pokemonspecy?.toSpecy(),
                hasDetails = true,
            ) else null
        }
    }

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

data class PokemonSpecy(
    val evolvesFromId: Int?,
    val legendary: Boolean,
    val mythical: Boolean,
    val baby: Boolean,
    val evolutionChain: PokemonEvolutionChain?,
    val name: String,
    val id: Int
) {
    companion object {
        fun GetPokemonDetailsQuery.Pokemon_v2_pokemonspecy.toSpecy(): PokemonSpecy {
            return PokemonSpecy(
                evolvesFromId = evolves_from_species_id,
                legendary = is_legendary,
                mythical = is_mythical,
                baby = is_baby,
                evolutionChain =
                PokemonEvolutionChain(
                    pokemon_v2_evolutionchain?.pokemon_v2_pokemonspecies?.map {
                        PokemonSpecy(
                            evolvesFromId = evolves_from_species_id,
                            legendary = is_legendary,
                            mythical = is_mythical,
                            baby = is_baby,
                            name = name,
                            id = id,
                            evolutionChain = null
                        )
                    }),
                name = name,
                id = id
            )
        }
    }
}

data class PokemonEvolutionChain(
    val pokemonEvolutionChain: List<PokemonSpecy>?,
)

data class Ability(
    val slot: Int,
    val id: Int,
    val ability: GenericPokemonData?,
    val isHidden: Boolean,
) : Serializable {

    companion object {

        fun List<GetPokemonDetailsQuery.Pokemon_v2_pokemonability>.toAbility(): List<Ability> {
            return this.map {
                Ability(
                    id = it.id,
                    slot = it.slot,
                    ability = GenericPokemonData(
                        it.pokemon_v2_ability?.name,
                        it.pokemon_v2_ability?.id
                    ),
                    isHidden = it.is_hidden
                )
            }
        }
    }
}

data class Stats(
    val baseStat: Int,
    val effort: Int,
    val stat: GenericPokemonData,
    val id: Int
) : Serializable {

    companion object {
        fun List<GetPokemonDetailsQuery.Pokemon_v2_pokemonstat>.toStats(): List<Stats> {

            return this.map {
                Stats(
                    baseStat = it.base_stat,
                    effort = it.effort,
                    stat = GenericPokemonData(it.pokemon_v2_stat?.name, it.pokemon_v2_stat?.id),
                    id = it.id
                )
            }
        }
    }
}

data class Moves(
    val move: UrlGenericPokemonData,
    val versionDetails: List<MoveVersionGroupDetails>
) : Serializable

data class Type(
    val slot: Int,
    val id: Int,
    val type: GenericPokemonData
) : Serializable {
    companion object {
        fun List<GetPokemonDetailsQuery.Pokemon_v2_pokemontype>.toType(): List<Type> {
            return this.map {
                Type(
                    slot = it.slot,
                    id = it.id,
                    type = GenericPokemonData(it.pokemon_v2_type?.name, it.pokemon_v2_type?.id)
                )
            }
        }
    }
}

data class HeldItems(
    val item: GenericPokemonData,
    val versionDetails: GenericPokemonData
) : Serializable {

    companion object {
        fun List<GetPokemonDetailsQuery.Pokemon_v2_pokemonitem>.toHeldItem(): List<HeldItems> {
            return this.map {
                HeldItems(
                    GenericPokemonData(
                        it.pokemon_v2_item?.name,
                        it.pokemon_v2_item?.id
                    ), GenericPokemonData(it.pokemon_v2_version?.name, it.pokemon_v2_version?.id)
                )
            }
        }
    }
}

data class MoveVersionGroupDetails(
    val levelLearned: Int,
    val moveLearnMethod: UrlGenericPokemonData,
    val versionGroup: UrlGenericPokemonData
) : Serializable