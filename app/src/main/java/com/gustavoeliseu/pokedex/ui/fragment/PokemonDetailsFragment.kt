package com.gustavoeliseu.pokedex.ui.fragment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gustavoeliseu.commonui.utils.extensions.shimmerEffect
import com.gustavoeliseu.domain.models.PokemonDetails
import com.gustavoeliseu.domain.models.PokemonSimpleListItem
import com.gustavoeliseu.domain.models.PokemonSpeciesEvolution
import com.gustavoeliseu.domain.models.PokemonSpeciesEvolution.Companion.neededToEvolve
import com.gustavoeliseu.domain.models.PokemonSpecy
import com.gustavoeliseu.domain.models.PokemonSpecy.Companion.toSimplePokemon
import com.gustavoeliseu.domain.utils.ClickType
import com.gustavoeliseu.domain.utils.ColorEnum
import com.gustavoeliseu.domain.utils.EvolutionRequirementTypeEnum
import com.gustavoeliseu.domain.utils.EvolutionTypeEnum
import com.gustavoeliseu.domain.utils.GenderEnum
import com.gustavoeliseu.domain.utils.TypeEnum
import com.gustavoeliseu.domain.utils.TypeEnum.Companion.isValid
import com.gustavoeliseu.pokedex.R
import com.gustavoeliseu.pokedex.ui.DefaultValue
import com.gustavoeliseu.pokedex.ui.PokemonCard
import com.gustavoeliseu.pokedex.ui.PokemonImageLoader
import com.gustavoeliseu.pokedex.ui.PokemonTypeRoundComposable
import com.gustavoeliseu.pokedex.ui.PokemonTypeTextComposable
import com.gustavoeliseu.pokedex.viewmodel.PokemonDetailsViewModel
import java.util.Locale

@Composable
fun PokemonDetailsFragment(
    modifier: Modifier = Modifier,
    onClick: (clickType: ClickType, id: Int) -> Unit = { _, _ -> },
    pokeId: Int,
    pokemonDetailsViewModel: PokemonDetailsViewModel = hiltViewModel(),
    onError: () -> Unit
) {
    val pokemonDetails by pokemonDetailsViewModel.details.collectAsState()
    LaunchedEffect(pokeId) {
        if (pokeId > 0) {
            pokemonDetailsViewModel.searchPokemonWithId(pokeId)
        } else {
            onError()
        }
    }
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (pokemonDetails != null) {
            pokemonDetails?.let {
                PokemonDetailsScreen(modifier, it, onError)
            }
        } else {
            loadingStateScreen(modifier)
        }
    }
}

@Composable
fun PokemonDetailsScreen(
    modifier: Modifier = Modifier,
    details: PokemonDetails,
    onError: () -> Unit
) {
    val colorEnum = ColorEnum.fromInt(details.pokemonColorId)
    var boxBackground by remember {
        mutableStateOf(if (details.baseColor != null) Color(details.baseColor!!) else Color.Black)
    }
    var textsColor by remember { mutableStateOf(if (details.textColor != null) Color(details.textColor!!) else Color.White) }
    val picture = if (details.id > 0) stringResource(
        id = com.gustavoeliseu.pokedexlist.R.string.pokemon_sprite_url,
        details.id
    ) else ""
    var loading by remember { mutableStateOf(true) }
    var retryHash by remember { mutableStateOf(0) }
    val baseColor = colorEnum.tintColor
    Box(modifier = modifier) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .align(Alignment.CenterHorizontally)
                    .size(170.dp)
                    .background(boxBackground)
                    .testTag("PokemonDetailsScreen")
            ) {
                Box(
                    modifier = modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .align(Alignment.Center)
                        .size(120.dp)
                        .testTag("PokemonDetailsScreen")
                ) {
                    if (picture.isEmpty() || details.drawableId != null) {
                        DefaultValue(
                            modifier.align(Alignment.Center),
                            details.name,
                            details.drawableId,
                            details.description
                        )
                        boxBackground = Color.Black
                        textsColor = Color.White
                    } else {
                        PokemonImageLoader(pokemonItemSimple = PokemonSimpleListItem.fromDetails(
                            details
                        ),
                            reloading = false,
                            picture = picture,
                            baseColor = baseColor,
                            retryHash = retryHash,
                            updateColors = { backColor, textColor, isLoading ->
                                boxBackground = backColor
                                textsColor = textColor
                                loading = isLoading
                                details.baseColor = backColor.toArgb()
                                details.textColor = textColor.toArgb()
                            }) {
                            retryHash++
                        }
                    }
                }
            }
            Text(
                text = details.name.uppercase(Locale.getDefault()),
                fontSize = 15.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 15.dp, end = 8.dp, start = 8.dp),
                color = textsColor,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )

            //Add Types
            Row(
                modifier = modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                details.types.forEach { type ->
                    PokemonTypeTextComposable(
                        type = TypeEnum.fromInt(type.type.id),
                        Modifier.width(80.dp)
                    )
                }
            }

            if (details.specy?.evolutionChain != null) {
                details.specy?.evolutionChain?.pokemonEvolutionChain?.sortedBy { it.evolvesFromId }
                    ?.firstOrNull()?.let { spec ->
                        showPokemonAndEvolutions(spec)
                    }
            }
        }
    }
}

@Composable
fun showPokemonAndEvolutions(spec: PokemonSpecy) {
    Column {
        if (spec.evolution.isNotEmpty()) {
            Row {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp),) {
                    spec.evolution.sortedWith(compareBy<PokemonSpecy> { it.evolvesFromId }.thenBy { it.id })
                        .forEach { evolution ->
                            val evolutionData =
                                evolution.pokemonSpecyEvolution?.filter { it.evolvedSpeciesId == evolution.id }
                            val simplePokeEvol = evolution.toSimplePokemon()
                            evolutionData?.sortedWith(compareBy<PokemonSpeciesEvolution> { it.evolvedSpeciesId }.thenBy { it.id })
                                ?.filter { it.locationId == null }
                                ?.forEachIndexed { index, innerSpec ->
                                    Card(modifier = Modifier,
                                    shape = CardDefaults.shape,
                                    colors = CardDefaults.cardColors(),
                                    elevation = CardDefaults.cardElevation(),
                                    border = null) {
                                        val hasLocationRelatedEvolution =
                                            evolutionData.any { it.locationId != null }
                                        Row() {
                                            val gender = GenderEnum.fromInt(innerSpec.genderId)
                                            val simplePoke = spec.toSimplePokemon(gender)
                                            PokemonCard(
                                                pokemonItemSimple = simplePoke,
                                                colorEnum = ColorEnum.fromInt(simplePoke.pokemonColorId),
                                                simpleCard = true,
                                                modifier = Modifier
                                                    .size(150.dp)
                                                    .weight(1f),
                                            )
                                            Column(
                                                modifier = Modifier
                                                    .weight(.8f)
                                                    .align(Alignment.CenterVertically)
                                            ) {
                                                val context = LocalContext.current
                                                var textResult = ""

                                                val evolutionTypeEnum =
                                                    EvolutionTypeEnum.fromInt(innerSpec.evolutionTriggerId)

                                                var item: EvolutionRequirementTypeEnum? = null

                                                val moveType: TypeEnum =
                                                    TypeEnum.fromInt(innerSpec.knownMoveTypeId)

                                                val friendship = if ((innerSpec.minAffection
                                                        ?: innerSpec.minHappiness) != null
                                                ) EvolutionRequirementTypeEnum.FRIENDSHIP else null

                                                val dayTime =
                                                    if (innerSpec.timeOfDay != null) EvolutionRequirementTypeEnum.getDayOrNight(
                                                        innerSpec.timeOfDay
                                                    ) else null

                                                innerSpec.neededToEvolve(evolutionTypeEnum) { text, id ->
                                                    when (evolutionTypeEnum) {
                                                        EvolutionTypeEnum.USEITEM -> {
                                                            item =
                                                                EvolutionRequirementTypeEnum.fromInt(
                                                                    id
                                                                )
                                                            textResult = context.getString(
                                                                R.string.evolve_use_item,
                                                                context.getString(item!!.nameId)
                                                            )
                                                        }

                                                        EvolutionTypeEnum.AGILEMOVE -> {
                                                            textResult = context.getString(
                                                                R.string.evolve_agile,
                                                                innerSpec.moveNeeded?.name
                                                            )
                                                        }

                                                        EvolutionTypeEnum.RECOIL -> {
                                                            textResult = context.getString(
                                                                R.string.evolve_recoil
                                                            )
                                                        }

                                                        EvolutionTypeEnum.STRONGMOVE -> {
                                                            textResult = context.getString(
                                                                R.string.evolve_strong,
                                                                innerSpec.moveNeeded?.name
                                                            )
                                                        }

                                                        EvolutionTypeEnum.SHED -> {
                                                            textResult =
                                                                context.getString(R.string.evolve_shed)
                                                        }

                                                        EvolutionTypeEnum.TRADE -> {
                                                            textResult =
                                                                if (innerSpec.heldItemId != null) {
                                                                    context.getString(
                                                                        R.string.evolve_trade_holding,
                                                                        context.getString(
                                                                            EvolutionRequirementTypeEnum.fromInt(
                                                                                id
                                                                            ).nameId
                                                                        )
                                                                    )
                                                                } else {
                                                                    context.getString(
                                                                        R.string.evolve_trade
                                                                    )
                                                                }
                                                        }

                                                        else -> {
                                                            textResult = context.getString(
                                                                R.string.evolve_level_custom,
                                                                text
                                                            )
                                                        }
                                                    }
                                                }
                                                Row(
                                                    modifier = Modifier
                                                        .align(Alignment.CenterHorizontally),
                                                ) {
                                                    item?.let {
                                                        if (it.value > 0) {
                                                            Image(
                                                                modifier = Modifier
                                                                    .size(30.dp)
                                                                    .align(Alignment.CenterVertically),
                                                                painter = painterResource(id = it.drawableId),
                                                                contentDescription = stringResource(
                                                                    id = it.nameId
                                                                )
                                                            )
                                                        }
                                                    }
                                                    friendship?.let {
                                                        if (it != EvolutionRequirementTypeEnum.INVALID) {
                                                            Image(
                                                                modifier = Modifier
                                                                    .size(30.dp),
                                                                painter = painterResource(id = it.drawableId),

                                                                contentDescription = stringResource(
                                                                    id = it.nameId
                                                                )
                                                            )
                                                        }
                                                    }
                                                    dayTime?.let {
                                                        if (it != EvolutionRequirementTypeEnum.INVALID) {
                                                            Image(
                                                                modifier = Modifier
                                                                    .size(30.dp),
                                                                painter = painterResource(id = it.drawableId),
                                                                contentDescription = stringResource(
                                                                    id = it.nameId
                                                                )
                                                            )
                                                        }
                                                    }
                                                    if (moveType != TypeEnum.INVALID) {
                                                        if (moveType.isValid()) {
                                                            PokemonTypeRoundComposable(
                                                                moveType, modifier = Modifier
                                                                    .size(30.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                                Text(
                                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                                    text = textResult,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                            PokemonCard(
                                                pokemonItemSimple = simplePokeEvol,
                                                colorEnum = ColorEnum.fromInt(simplePokeEvol.pokemonColorId),
                                                simpleCard = true,
                                                modifier = Modifier
                                                    .size(150.dp)
                                                    .weight(1f)
                                            )
                                        }
                                        if (hasLocationRelatedEvolution) {
                                            Text(
                                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                                text = "Also level up near specific places",
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                        }
                    spec.evolution?.forEach {
                        showPokemonAndEvolutions(it)
                    }
                }
            }
        }
    }
}


@Composable
fun loadingStateScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = modifier
                .shimmerEffect()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.Center)
                .size(120.dp)
        )
    }
}

@Preview
@Composable
fun LoadingStatePreview() {
    loadingStateScreen()
}

@Preview
@Composable
fun PokemonDetailsFragmentPreview(){
    PokemonDetailsScreen(Modifier, PokemonDetails.getExamplePokemonDetails(false)){}
}