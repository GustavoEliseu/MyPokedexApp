package com.gustavoeliseu.pokedex.ui.pokemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.gustavoeliseu.pokedex.R
import com.gustavoeliseu.pokedex.utils.ColorEnum
import com.gustavoeliseu.pokedex.utils.extensions.colorDistance
import com.gustavoeliseu.pokedex.utils.extensions.isDarkColor
import com.gustavoeliseu.pokedex.utils.extensions.notTooDarkNorTooBright
import com.gustavoeliseu.pokedex.utils.extensions.shimmerEffect
import java.util.Locale

@Composable
fun PokemonCard(
    id: Int,
    name: String,
    picture: String,
    colorEnum: ColorEnum,
    modifier: Modifier = Modifier,
    reloading: Boolean = false
) {
    var boxBackground by remember {
        mutableStateOf(Color.White)
    }
    var textsColor by remember { mutableStateOf(Color.White) }
    var loading by remember { mutableStateOf(true) }
    if (picture.isEmpty()) loading = false
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
                text = id.toString(),
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
                MissingNo(modifier.align(Alignment.Center), name)
                boxBackground = Color.Black
                textsColor = Color.White
            } else {
                PokemonImageLoader(name = name,
                    reloading = reloading,
                    picture = picture,
                    colorEnum = colorEnum,
                    retryHash = retryHash,
                    updateColors = { backColor, textColor, isLoading ->
                        boxBackground = backColor
                        textsColor = textColor
                        loading = isLoading
                    }) {
                    retryHash++
                }
            }
        }
        if (!loading) {
            Text(
                text = name.uppercase(Locale.getDefault()),
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
    name: String,
    reloading: Boolean,
    picture: String,
    colorEnum: ColorEnum,
    updateColors: (Color, Color, Boolean) -> Unit,
    retryHash: Int,
    updateRetryHash: () -> Unit
) {
    val baseColor = colorEnum.tintColor
    val isDarkTheme = isSystemInDarkTheme()
    SubcomposeAsyncImage(
        model =
        ImageRequest.Builder(LocalContext.current).data(picture)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .allowHardware(false).crossfade(true).setParameter("retry_hash", retryHash, null)
            .build(),
        onSuccess = {
            val mBitmap = it.result.drawable.toBitmap()
            val range = 24
            Palette.from(mBitmap).setRegion(
                mBitmap.width / 2 - range,
                mBitmap.height / 2 - range,
                mBitmap.width / 2 + range,
                mBitmap.height / 2 + range
            ).clearFilters().addFilter(Palette.Filter { color, hsl ->
                    compareColors(color, hsl, baseColor)
                }).generate { p ->
                    p?.let {
                        var domSwatch = p.dominantSwatch
                        domSwatch = when {
                            p.vibrantSwatch != null -> {
                                val comparison = Color(
                                    p.vibrantSwatch!!.rgb
                                ).colorDistance(baseColor)
                                val population =
                                    ((p.dominantSwatch?.population
                                        ?: 0) - (p.vibrantSwatch?.population ?: 0))
                                if (population in 1..79 && comparison >= .38f) {
                                    p.vibrantSwatch
                                } else p.dominantSwatch
                            }

                            p.lightVibrantSwatch != null && p.vibrantSwatch == null && isDarkTheme -> {
                                val comparison = Color(
                                    p.lightVibrantSwatch!!.rgb
                                ).colorDistance(baseColor)
                                val population =
                                    ((p.lightVibrantSwatch?.population
                                        ?: 0) - (p.vibrantSwatch?.population ?: 0))
                                if (population < 80 && comparison < .38f) {
                                    p.lightVibrantSwatch
                                } else p.dominantSwatch
                            }

                            p.darkVibrantSwatch != null && p.vibrantSwatch == null && !isDarkTheme -> {
                                val comparison = Color(
                                    p.darkVibrantSwatch!!.rgb
                                ).colorDistance(baseColor)
                                val population =
                                    ((p.darkVibrantSwatch?.population
                                        ?: 0) - (p.vibrantSwatch?.population ?: 0))
                                if (population < 80 && comparison < .38f) {
                                    p.darkVibrantSwatch
                                } else p.dominantSwatch
                            }

                            else -> {
                                p.dominantSwatch
                            }
                        }
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
                    id = R.string.missing_no, name
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
        contentDescription = stringResource(R.string.pokemon_description, name)
    )
}

fun compareColors(color1: Int, hsl: FloatArray, baseColor: Color): Boolean {
    val comparison = Color(
        color1
    ).colorDistance(baseColor)
    return  hsl.notTooDarkNorTooBright(0.7f) && (comparison < .38f)
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
        id = -1,
        name = "Missigno",
        picture = "",
        colorEnum = ColorEnum.BLACK,
        modifier = Modifier.clickable {
        }
    )
}
