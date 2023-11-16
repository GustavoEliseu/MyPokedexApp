package com.gustavoeliseu.pokedex.ui

import android.graphics.drawable.Drawable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import com.gustavoeliseu.myapplication.R as commonResources
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.gustavoeliseu.commonui.utils.extensions.chooseBestPalette
import com.gustavoeliseu.commonui.utils.extensions.getPaletteFilterFromColor
import com.gustavoeliseu.commonui.utils.extensions.isDarkColor
import com.gustavoeliseu.commonui.utils.extensions.shimmerEffect
import com.gustavoeliseu.domain.models.PokemonSimpleListItem
import com.gustavoeliseu.domain.utils.ColorEnum
import com.gustavoeliseu.domain.utils.GenderEnum
import com.gustavoeliseu.pokedexlist.R
import java.util.Locale

@Composable
fun PokemonCard(
    pokemonItemSimple: PokemonSimpleListItem,
    colorEnum: ColorEnum,
    modifier: Modifier = Modifier,
    reloading: Boolean = false,
    simpleCard: Boolean = false
) {
    var boxBackground by remember {
        mutableStateOf(if (pokemonItemSimple.baseColor != null) Color(pokemonItemSimple.baseColor!!) else Color.Black)
    }
    var textsColor by remember { mutableStateOf(Color.White) }
    var loading by remember { mutableStateOf(false) }
    val picture = if (pokemonItemSimple.id > 0) stringResource(
        id = R.string.pokemon_sprite_url,
        pokemonItemSimple.id
    ) else ""
    if (picture.isEmpty()) loading = false
    val baseColor = colorEnum.tintColor
    var retryHash by remember { mutableStateOf(0) }
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .size(if (simpleCard) 120.dp else 170.dp, 170.dp)
            .background(boxBackground)
            .testTag("PokemonCardBox")
    ) {
        if (pokemonItemSimple.gender != GenderEnum.INVALID) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.TopEnd)
                    .padding(3.dp)
                    .clip(CircleShape)
                    .border(
                        BorderStroke(
                            1.dp,
                            (if ((boxBackground.toArgb()).isDarkColor()) Color.White else Color.Black)
                        ), CircleShape
                    )
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            (if ((boxBackground.toArgb()).isDarkColor()) Color.White else Color.Black).copy(
                                alpha = .7f
                            )
                        )
                ) {
                    Image(
                        painter = painterResource(id = pokemonItemSimple.gender.drawableId),
                        contentDescription = stringResource(pokemonItemSimple.gender.nameId),
                        modifier =
                        Modifier
                            .size(30.dp)
                            .padding(5.dp)
                    )
                }
            }
        }
        if (!loading && !simpleCard) {
            Text(
                text = pokemonItemSimple.id.toString(),
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 10.dp, start = 10.dp),
                fontWeight = FontWeight.Bold,
                color = textsColor
            )
        }
        if (loading) {
            Box(
                modifier = modifier
                    .shimmerEffect()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .align(Alignment.Center)
                    .size(120.dp)
            )
        }
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.Center)
                .size(120.dp)
        ) {
            if (picture.isEmpty() || pokemonItemSimple.drawableId != null) {
                DefaultValue(
                    modifier.align(Alignment.Center),
                    pokemonItemSimple.name,
                    pokemonItemSimple.drawableId,
                    pokemonItemSimple.description
                )
                boxBackground = Color.Black
                textsColor = Color.White
            } else {
                PokemonImageLoader(
                    modifier = modifier.align(Alignment.Center),
                    pokemonItemSimple = pokemonItemSimple,
                    reloading = reloading,
                    picture = picture,
                    baseColor = baseColor,
                    retryHash = retryHash,
                    updateColors = { backColor, textColor, isLoading ->
                        boxBackground = backColor
                        textsColor = textColor
                        loading = isLoading
                        pokemonItemSimple.baseColor = backColor.toArgb()
                        pokemonItemSimple.textColor = textColor.toArgb()
                    }) {
                    retryHash++
                }
            }
        }
        if (!loading) {
            if(simpleCard){
                Spacer(modifier = Modifier.height(10.dp))
            }
            Text(
                text = pokemonItemSimple.name.uppercase(Locale.getDefault()),
                fontSize = if(simpleCard)10.sp else 15.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 15.dp, end = 8.dp, start = 8.dp),
                color = textsColor,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

@Composable
fun PokemonImageLoader(
    modifier: Modifier = Modifier,
    pokemonItemSimple: PokemonSimpleListItem,
    reloading: Boolean,
    picture: String,
    baseColor: Color,
    updateColors: (Color, Color, Boolean) -> Unit,
    retryHash: Int,
    updateRetryHash: () -> Unit
) {
    SubcomposeAsyncImage(
        model =
        ImageRequest.Builder(LocalContext.current).data(picture)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .allowHardware(false).crossfade(true).setParameter("retry_hash", retryHash, null)
            .build(),
        onSuccess = {
            getBackgroundColors(it.result.drawable, pokemonItemSimple, baseColor, updateColors)
        },
        loading = {
            CircularProgressIndicator(
                modifier = Modifier
                    .requiredHeight(40.dp)
                    .requiredWidth(40.dp)
            )
        },
        error = {
            Image(
                painter = painterResource(commonResources.drawable.missingno),
                contentDescription = stringResource(
                    id = commonResources.string.missing_no, pokemonItemSimple.name
                )
            )
            updateColors(
                baseColor,
                if ((baseColor.toArgb()).isDarkColor()) Color.White else Color.Black,
                false
            )
        },
        onError = {
            if (reloading) {
                updateRetryHash()
            }
        },
        contentScale = ContentScale.Fit,
        modifier = modifier
            .height(120.dp)
            .width(120.dp),
        contentDescription = stringResource(
            commonResources.string.pokemon_description,
            pokemonItemSimple.name
        )
    )
}

fun getBackgroundColors(
    drawable: Drawable, pokemonItemSimple: PokemonSimpleListItem, baseColor: Color,
    updateColors: (Color, Color, Boolean) -> Unit
) {
    if (pokemonItemSimple.baseColor == null || pokemonItemSimple.textColor == null) {
        Palette.from(drawable.toBitmap()).clearFilters()
            .addFilter { color, hsl ->
                hsl.getPaletteFilterFromColor(color, baseColor)
            }.generate { p ->
                p?.let {
                    p.chooseBestPalette(baseColor) { bColor, tColor, isLoading ->
                        updateColors(
                            bColor ?: baseColor,
                            tColor
                                ?: if ((baseColor.toArgb()).isDarkColor()) Color.White else Color.Black,
                            isLoading
                        )
                    }
                }
            }
    } else {
        val mPokemonBaseColor = pokemonItemSimple.baseColor
        val mPokemonTextColor = pokemonItemSimple.textColor
        val textColor =
            if (mPokemonTextColor != null) Color(mPokemonTextColor) else if ((baseColor.toArgb()).isDarkColor()) Color.White else Color.Black
        updateColors(
            if (mPokemonBaseColor != null) Color(mPokemonBaseColor) else baseColor,
            textColor,
            false
        )
    }
}

@Composable
fun DefaultValue(
    modifier: Modifier,
    name: String,
    drawableId: Int? = null,
    textDescription: String? = null
) {
    Image(
        painter = painterResource(drawableId ?: commonResources.drawable.missingno),
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .height(120.dp)
            .width(120.dp),
        contentDescription = textDescription ?: stringResource(commonResources.string.missing_no, name)
    )
}

@Preview
@Composable
fun PokemonCardPreview() {
    PokemonCard(
        pokemonItemSimple = PokemonSimpleListItem(
            id = -1,
            name = "Missingno",
            pokemonColorId = 1,
            gender = GenderEnum.FEMALE
        ),
        colorEnum = ColorEnum.BLACK,
        modifier = Modifier.clickable {
        }
    )
}

@Preview
@Composable
fun PokemonCardCaterpiePreview() {
    PokemonCard(
        pokemonItemSimple = PokemonSimpleListItem(
            id = 10,
            name = "Caterpie",
            pokemonColorId = 1,
            gender = GenderEnum.MALE,
            baseColor = Color.Green.hashCode(),
            drawableId = commonResources.drawable.caterpie_test,
            description = "A picture of caterpie"
        ),
        colorEnum = ColorEnum.GREEN,
        modifier = Modifier.clickable {
        }
    )
}

@Preview
@Composable
fun PokemonCardSimpleCaterpiePreview() {
    PokemonCard(
        pokemonItemSimple = PokemonSimpleListItem(
            id = 10,
            name = "Caterpie",
            pokemonColorId = 1,
            gender = GenderEnum.MALE,
            baseColor = Color.Green.hashCode(),
            drawableId = commonResources.drawable.caterpie_test,
            description = "A picture of caterpie"
        ),
        colorEnum = ColorEnum.GREEN,
        modifier = Modifier.clickable {
        },
        simpleCard = true
    )
}