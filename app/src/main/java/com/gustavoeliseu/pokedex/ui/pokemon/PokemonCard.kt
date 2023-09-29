package com.gustavoeliseu.pokedex.ui.pokemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.gustavoeliseu.pokedex.utils.notBlackNorWhite
import java.util.Locale

@Composable
fun PokemonCard(
    id: Int,
    name: String,
    picture: String,
    modifier: Modifier = Modifier,
) {
    //TODO - 6 - card Layout improvements
    var boxBackground by remember {
        mutableStateOf(Color.White)
    }
    var textsColor by remember { mutableStateOf(Color.White) }

    Box(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .size(170.dp)
            .background(boxBackground)
    ) {
        Text(
            text = id.toString(),
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 10.dp, start = 10.dp),
            fontWeight = FontWeight.Bold,
            color = textsColor
        )

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
                val context = LocalContext.current
                SubcomposeAsyncImage(
                    model =
                    ImageRequest.Builder(LocalContext.current)
                        .data(picture)
                        .allowHardware(false)
                        .crossfade(true)
                        .build(),
                    onSuccess = {
                        Palette.from(it.result.drawable.toBitmap())
                            .addFilter(Palette.Filter { color, hsl ->
                                //TODO - modify filters to fix background of beedril, vulpix and some others
                                color.notBlackNorWhite()
                            })
                            .generate { p ->
                                p?.let {
                                    p.dominantSwatch?.let { domSwatch ->
                                        boxBackground = Color(domSwatch.rgb)
                                        textsColor = Color(domSwatch.titleTextColor)
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
