package com.gustavoeliseu.pokedexlist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.gustavoeliseu.domain.entity.PokemonSimpleListItem
import com.gustavoeliseu.commonui.utils.extensions.colorDistance
import com.gustavoeliseu.commonui.utils.extensions.isDarkColor
import com.gustavoeliseu.commonui.utils.extensions.notTooDarkNorTooBright
import com.gustavoeliseu.commonui.utils.extensions.shimmerEffect
import com.gustavoeliseu.pokedexlist.ColorEnum
import com.gustavoeliseu.pokedexlist.R
import java.util.Locale

@Composable
fun PokemonCard(
    pokemonItemSimple : PokemonSimpleListItem,
    colorEnum: ColorEnum,
    picture: String,
    modifier: Modifier = Modifier,
    reloading: Boolean = false
) {
    var boxBackground by remember {
        mutableStateOf(Color.White)
    }
    var textsColor by remember { mutableStateOf(Color.White) }
    var loading by remember { mutableStateOf(true) }
    if (picture.isEmpty()) loading = false
    val baseColor = colorEnum.tintColor
    var retryHash by remember { mutableStateOf(0) }
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .size(170.dp)
            .background(boxBackground)
    ) {
        if (!loading) {
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
            if (picture.isEmpty()) {
                MissingNo(modifier.align(Alignment.Center), pokemonItemSimple.name)
                boxBackground = Color.Black
                textsColor = Color.White
            } else {
                PokemonImageLoader(pokemonItemSimple = pokemonItemSimple,
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
            Text(
                text = pokemonItemSimple.name.uppercase(Locale.getDefault()),
                fontSize = 15.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 15.dp, end = 8.dp, start = 8.dp),
                color = textsColor,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}

@Composable
fun PokemonImageLoader(
    pokemonItemSimple : PokemonSimpleListItem,
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
            if(pokemonItemSimple.baseColor == null || pokemonItemSimple.textColor == null){
            val mBitmap = it.result.drawable.toBitmap()
            val range = 24
            Palette.from(mBitmap).setRegion(
                mBitmap.width / 2 - range,
                mBitmap.height / 2 - range,
                mBitmap.width / 2 + range,
                mBitmap.height / 2 + range
            ).clearFilters()
                .addFilter(Palette.Filter { color, hsl ->
                    val comparison = Color(
                        color
                    ).colorDistance(baseColor)
                    hsl.notTooDarkNorTooBright() && (comparison <= .5f)
                }).generate { p ->
                    p?.let {
                        val domSwatch =
                            if (p.dominantSwatch != null) p.dominantSwatch else p.lightVibrantSwatch
                        if (domSwatch != null) {
                            updateColors(
                                Color(domSwatch.rgb),
                                Color(domSwatch.titleTextColor), false
                            )
                        } else {
                            updateColors(
                                baseColor,
                                if ((baseColor.toArgb()).isDarkColor()) Color.White else Color.Black,
                                false
                            )
                        }
                    }
                }
            }else{
                var mBaseColor = baseColor
                var mTextColor = if ((baseColor.toArgb()).isDarkColor()) Color.White else Color.Black
                pokemonItemSimple.baseColor?.let{
                    mBaseColor= Color(it)
                }
                pokemonItemSimple.textColor?.let {
                    mTextColor = Color(it)
                }

                updateColors(mBaseColor,mTextColor,false)
            }
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
                painter = painterResource(R.drawable.missingno),
                contentDescription = stringResource(
                    id = R.string.missing_no, pokemonItemSimple.name
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
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(120.dp)
            .width(120.dp),
        contentDescription = stringResource(R.string.pokemon_description, pokemonItemSimple.name)
    )
}

@Composable
fun MissingNo(modifier: Modifier, name: String) {
    Image(
        painter = painterResource(R.drawable.missingno),
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .height(120.dp)
            .width(120.dp),
        contentDescription = stringResource(R.string.missing_no, name)
    )
}

@Preview
@Composable
fun PokemonCardPreview() {
    PokemonCard(
        pokemonItemSimple = PokemonSimpleListItem(
            id = -1,
            name = "Missigno",
            pokemonColorId = 1
            ),
        picture = "",
        colorEnum = ColorEnum.BLACK,
        modifier = Modifier.clickable {
        }
    )
}