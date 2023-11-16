package com.gustavoeliseu.domain.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gustavoeliseu.domain.models.Ability.Companion.toAbility
import com.gustavoeliseu.domain.models.HeldItems.Companion.toHeldItem
import com.gustavoeliseu.domain.models.PokemonSpeciesEvolution.Companion.toPokemonSpeciesEvolutionList
import com.gustavoeliseu.domain.models.PokemonSpecy.Companion.toSpecy
import com.gustavoeliseu.domain.models.Stats.Companion.toStats
import com.gustavoeliseu.domain.models.Type.Companion.toType
import com.gustavoeliseu.domain.utils.Const.PokemonData.POKEMON_TABLE_NAME
import com.gustavoeliseu.domain.utils.EvolutionTypeEnum
import com.gustavoeliseu.domain.utils.GenderEnum
import com.gustavoeliseu.domain.utils.TypeEnum
import com.gustavoeliseu.pokedex.GetPokemonDetailsQuery
import com.gustavoeliseu.pokedex.PokemonListGraphQlQuery
import com.gustavoeliseu.pokedexdata.models.GenericPokemonData
import com.gustavoeliseu.pokedexdata.models.GenericPokemonDataList
import com.gustavoeliseu.pokedexdata.models.UrlGenericPokemonData


data class PokemonSimpleListItem(
    override val name: String,
    @PrimaryKey
    override val id: Int,
    val pokemonColorId: Int? = null,
    var baseColor: Int? = null,
    var textColor: Int? = null,
    var hasDetails: Boolean = false,
    @Ignore
    var drawableId: Int? = null,
    @Ignore
    var description: String? = null,
    @Ignore
    var gender: GenderEnum = GenderEnum.INVALID
) : GenericPokemonData(name, id) {
    constructor(
        name: String,
        id: Int,
        pokemonColorId: Int?,
        baseColor: Int?,
        textColor: Int?
    ) : this(name, id, pokemonColorId, baseColor, textColor, false)

    fun toPokemonDetails(): PokemonDetails {
        return PokemonDetails(
            name = name,
            id = id,
            pokemonColorId = pokemonColorId,
            baseColor = baseColor,
            textColor = textColor
        )
    }

    companion object {
        fun fromDetails(details: PokemonDetails): PokemonSimpleListItem {
            return details.toPokemonSimple()
        }
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
data class PokemonDetails(
    var abilities: List<Ability> = listOf(),
    var baseExperience: Int? = null,
    override var name: String,
    var height: Int? = null,
    var weight: Int? = null,
    @PrimaryKey
    override var id: Int,
    var heldItems: List<HeldItems> = listOf(),
    var isDefault: Boolean = false,
    var stats: List<Stats> = listOf(),
    var types: List<Type> = listOf(),
    var specy: PokemonSpecy? = null,
    var pokemonColorId: Int? = null,
    var baseColor: Int? = null,
    var textColor: Int? = null,
    var hasDetails: Boolean = false,
    @Ignore
    var drawableId: Int? = null,
    @Ignore
    var description: String? = null,
) : GenericPokemonData(name, id) {

    constructor(): this(id= -1,name="")

    fun addColors(simpleItem: PokemonSimpleListItem) {
        if (this.pokemonColorId == null && simpleItem.pokemonColorId != null)
            this.pokemonColorId = simpleItem.pokemonColorId
        if (this.baseColor == null && simpleItem.baseColor != null)
            this.baseColor = simpleItem.baseColor
        if (this.textColor == null && simpleItem.textColor != null)
            this.textColor = simpleItem.textColor
    }

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

        fun getExamplePokemonDetails(missingno:Boolean): PokemonDetails{
            return PokemonDetails(
                name = if(missingno)"Missingno" else "Caterpie", id = if(missingno)-1 else 10, drawableId = -1,description ="A picture of caterpie"
            )
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
    val pokemonColorId: Int?,
    val evolutionChain: PokemonEvolutionChain? = null,
    val evolution: MutableList<PokemonSpecy> = mutableListOf(),
    val pokemonSpecyEvolution: List<PokemonSpeciesEvolution>? = listOf(),
    val name: String,
    val id: Int
) {
    companion object {
        fun GetPokemonDetailsQuery.Pokemon_v2_pokemonspecy.toSpecy(): PokemonSpecy {
            val evolutionChain =
                PokemonEvolutionChain(
                    pokemon_v2_evolutionchain?.pokemon_v2_pokemonspecies?.map { species ->
                        PokemonSpecy(
                            id = species.id,
                            evolvesFromId = species.evolves_from_species_id,
                            legendary = species.is_legendary,
                            mythical = species.is_mythical,
                            baby = species.is_baby,
                            name = species.name,
                            pokemonColorId = species.pokemon_color_id,
                            pokemonSpecyEvolution = species.pokemon_v2_pokemonevolutions.toPokemonSpeciesEvolutionList()
                        )
                    })
            evolutionChain.pokemonEvolutionChain?.forEach {
                if (it.evolvesFromId != null) {
                    evolutionChain.pokemonEvolutionChain.find { inner -> it.evolvesFromId == inner.id }?.evolution?.add(
                        it
                    )
                }
            }
            val evolution =
                evolutionChain.pokemonEvolutionChain?.find { inner -> id == inner.id }?.evolution
                    ?: mutableListOf()

            return PokemonSpecy(
                evolvesFromId = evolves_from_species_id,
                legendary = is_legendary,
                mythical = is_mythical,
                baby = is_baby,
                evolutionChain = evolutionChain,
                evolution = evolution,
                name = name,
                pokemonColorId = pokemon_color_id,
                id = id
            )
        }

        fun PokemonSpecy.toSimplePokemon(gender: GenderEnum = GenderEnum.INVALID): PokemonSimpleListItem {
            return PokemonSimpleListItem(name, id, pokemonColorId, gender = gender)
        }
    }
}

data class PokemonEvolutionChain(
    val pokemonEvolutionChain: List<PokemonSpecy>?,
)

data class PokemonSpeciesEvolution(
    val id: Int,
    val evolutionItemId: Int? = null,
    val evolutionTriggerId: Int? = null,
    val evolvedSpeciesId: Int? = null,
    val heldItemId: Int? = null,
    val genderId: Int? = null,
    val knownMoveId: Int? = null,
    val knownMoveTypeId: Int? = null,
    val minBeauty: Int? = null,
    val minHappiness: Int? = null,
    val minLevel: Int? = null,
    val timeOfDay: String? = null,
    val tradeSpeciesId: Int? = null,
    val minAffection: Int? = null,
    val locationId: Int? = null,
    val evolutionItem: GenericPokemonData? = null,
    val moveNeeded: GenericPokemonData? = null,
    val locationNeeded: GenericPokemonData? = null
) {
    companion object {
        fun List<GetPokemonDetailsQuery.Pokemon_v2_pokemonevolution>.toPokemonSpeciesEvolutionList(): List<PokemonSpeciesEvolution> {
            return this.map {
                PokemonSpeciesEvolution(
                    id = it.id,
                    evolutionItemId = it.evolution_item_id,
                    evolutionTriggerId = it.evolution_trigger_id,
                    evolvedSpeciesId = it.evolved_species_id,
                    heldItemId = it.held_item_id,
                    genderId = it.gender_id,
                    knownMoveId = it.known_move_id,
                    knownMoveTypeId = it.known_move_type_id,
                    minBeauty = it.min_beauty,
                    minHappiness = it.min_happiness,
                    minLevel = it.min_level,
                    timeOfDay = it.time_of_day,
                    tradeSpeciesId = it.trade_species_id,
                    minAffection = it.min_affection,
                    locationId = it.location_id,
                    evolutionItem = if (it.pokemon_v2_item != null) GenericPokemonData(
                        it.pokemon_v2_item.name,
                        it.pokemon_v2_item.id
                    ) else null,
                    moveNeeded = if (it.pokemon_v2_move != null) GenericPokemonData(
                        it.pokemon_v2_move.name,
                        it.pokemon_v2_move.id
                    ) else null,
                    locationNeeded = if (it.pokemon_v2_location != null) GenericPokemonData(
                        it.pokemon_v2_location.name,
                        it.pokemon_v2_location.id
                    ) else null
                )
            }
        }

        fun PokemonSpeciesEvolution.neededToEvolve(
            evolutionTypeEnum: EvolutionTypeEnum,
            resultCallback: (String, Int?) -> Unit
        ) {
            var result = ""
            var itemId: Int? = heldItemId
            when (evolutionTypeEnum) {
                EvolutionTypeEnum.LEVEL -> {
                    if (minLevel != null) {
                        result += " to $minLevel"
                    }
                    if (!timeOfDay.isNullOrEmpty()) result += " during $timeOfDay"
                    if (knownMoveTypeId != null) result += " knowing a ${
                        TypeEnum.fromInt(
                            knownMoveTypeId
                        ).name.lowercase()
                    } move"
                    if (knownMoveId != null && moveNeeded != null) result += " knowing ${moveNeeded.name}"
                    if (locationId != null && locationNeeded != null) result += " near ${locationNeeded.name}"

                }

                EvolutionTypeEnum.TRADE -> {
                    result = "Trade"
                }

                EvolutionTypeEnum.USEITEM -> {
                    result = "Use"
                    itemId = evolutionItemId
                }

                EvolutionTypeEnum.AGILEMOVE -> {
                    itemId = knownMoveId
                }

                EvolutionTypeEnum.STRONGMOVE -> {
                    itemId = knownMoveId
                }

                else -> {
                }
            }

            resultCallback(result, itemId)
        }
    }
}

data class Ability(
    val slot: Int,
    val id: Int,
    val ability: GenericPokemonData?,
    val isHidden: Boolean,
)  {

    companion object {

        fun List<GetPokemonDetailsQuery.Pokemon_v2_pokemonability>.toAbility(): List<Ability> {
            return this.map {
                Ability(
                    id = it.id,
                    slot = it.slot,
                    ability = GenericPokemonData(
                        it.pokemon_v2_ability?.name.orEmpty(),
                        it.pokemon_v2_ability?.id ?: -1
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
) {

    companion object {
        fun List<GetPokemonDetailsQuery.Pokemon_v2_pokemonstat>.toStats(): List<Stats> {

            return this.map {
                Stats(
                    baseStat = it.base_stat,
                    effort = it.effort,
                    stat = GenericPokemonData(it.pokemon_v2_stat?.name.orEmpty(), it.pokemon_v2_stat?.id ?: -1),
                    id = it.id
                )
            }
        }
    }
}

data class Moves(
    val move: UrlGenericPokemonData,
    val versionDetails: List<MoveVersionGroupDetails>
)

data class Type(
    val slot: Int,
    val type: GenericPokemonData
) {
    companion object {
        fun List<GetPokemonDetailsQuery.Pokemon_v2_pokemontype>.toType(): List<Type> {
            return this.map {
                Type(
                    slot = it.slot,
                    type = GenericPokemonData(it.pokemon_v2_type?.name.orEmpty(), it.pokemon_v2_type?.id  ?: -1)
                )
            }
        }
    }
}

data class HeldItems(
    val item: GenericPokemonData,
    val versionDetails: GenericPokemonData
) {

    companion object {
        fun List<GetPokemonDetailsQuery.Pokemon_v2_pokemonitem>.toHeldItem(): List<HeldItems> {
            return this.map {
                HeldItems(
                    GenericPokemonData(
                        it.pokemon_v2_item?.name.orEmpty(),
                        it.pokemon_v2_item?.id ?: -1
                    ), GenericPokemonData(it.pokemon_v2_version?.name.orEmpty(), it.pokemon_v2_version?.id ?: -1)
                )
            }
        }
    }
}

data class MoveVersionGroupDetails(
    val levelLearned: Int,
    val moveLearnMethod: UrlGenericPokemonData,
    val versionGroup: UrlGenericPokemonData
)