package com.gustavoeliseu.pokedex.ui.pokemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import coil.request.ImageRequest
import com.gustavoeliseu.pokedex.R
import com.gustavoeliseu.pokedex.utils.extensions.notBlackNorWhite
import com.gustavoeliseu.pokedex.utils.extensions.shimmerEffect
import java.util.Locale

@Composable
fun PokemonCard(
    id: Int,
    name: String,
    picture: String,
    modifier: Modifier = Modifier,
) {
    var boxBackground by remember {
        mutableStateOf(Color.White)
    }
    var textsColor by remember { mutableStateOf(Color.White) }
    var loading by remember { mutableStateOf(true) }

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
            Box(modifier = modifier.shimmerEffect().padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.Center)
                .size(120.dp))
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
                SubcomposeAsyncImage(
                    model =
                    ImageRequest.Builder(LocalContext.current)
                        .data(picture)
                        .allowHardware(false)
                        .crossfade(true)
                        .build(),
                    onSuccess = {
                        val mBitmap = it.result.drawable.toBitmap()
                        val range = 24
                        Palette.from(mBitmap)
                            .setRegion( mBitmap.width/2-range,mBitmap.height/2-range,mBitmap.width/2+range,mBitmap.height/2+range)
                            .clearFilters()
                            .addFilter(Palette.Filter { color, hsl ->
                                //TODO - modify filters to fix background of beedril, vulpix and some others
                                color.notBlackNorWhite()
                            })
                            .generate { p ->
                                p?.let {
                                    val domSwatch = if( p.dominantSwatch != null) p.dominantSwatch else {if(p.lightVibrantSwatch != null) p.lightVibrantSwatch else p.swatches[0]}
                                    domSwatch?.let {
                                        boxBackground = Color(domSwatch.rgb)
                                        textsColor = Color(domSwatch.titleTextColor)
                                        loading = false
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
                    error = { painterResource(R.drawable.missingno) },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp),
                    contentDescription = stringResource(R.string.pokemon_description, name)
                )
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
        picture = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/132.png"
    )
}
