package com.gustavoeliseu.pokedex.ui.pokemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.gustavoeliseu.pokedex.R
import java.util.Locale

@Composable
fun PokemonCard(
    id: Int,
    name: String,
    picture: String,
    modifier: Modifier = Modifier,
) {
    var boxBackground by remember {
        mutableStateOf(Color.Black)
    }
    var textsColor by remember { mutableStateOf(Color.White) }

    Box(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .size(170.dp)
            .background(boxBackground)
    ) {
        val darkTheme = isSystemInDarkTheme()
        Text(
            text = id.toString(),
            fontSize = 24.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 6.dp, start = 6.dp),
            color = textsColor
        )

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.Center)
                .size(120.dp)
        ) {
            if (picture.isEmpty()) {
                Image(
                    painter = painterResource(R.drawable.missingno),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .height(120.dp)
                        .width(120.dp),
                    contentDescription = stringResource(R.string.pokemon_description, name)
                )
                boxBackground = Color.Black
                textsColor = Color.White
            } else {
                AsyncImage(
                    model =
                    ImageRequest.Builder(LocalContext.current)
                        .data(picture)
                        .allowHardware(false)
                        .scale(Scale.FILL)
                        .crossfade(true)
                        .build(),
                    onSuccess = {
                        Palette.from(it.result.drawable.toBitmap()).generate { p ->
                            val swatch =
                                if (darkTheme) p?.darkVibrantSwatch else p?.lightVibrantSwatch
                            swatch?.let {
                                boxBackground = Color(swatch.rgb)
                                textsColor = Color(swatch.bodyTextColor)
                            }
                        }
                    },
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp),
                    placeholder = painterResource(R.drawable.missingno),
                    contentDescription = stringResource(R.string.pokemon_description, name)
                )
            }
        }
        Text(
            text = name.uppercase(Locale.getDefault()),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 10.dp),
            color = textsColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun PokemonCardPreview() {
//    PokemonCard(id = -1, name = "Missigno", picture = "")
}
