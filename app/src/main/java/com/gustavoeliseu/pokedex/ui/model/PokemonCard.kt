package com.gustavoeliseu.pokedex.ui.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gustavoeliseu.pokedex.R

var testeState = "testee"
@Composable
fun PokemonCard(
    id: Int,
    name: String,
    picture: String
) {
    var boxBackground by remember {
        mutableStateOf(Color.Transparent)
    }
    var textsColor by remember { mutableStateOf(Color.White) }

    Box(
        modifier = Modifier.padding(horizontal = 16.dp).background(boxBackground),
    ) {
            Text(
                text = id.toString(),
                modifier = Modifier.align(Alignment.TopStart),
                color = textsColor
            )
            if (picture.isEmpty()) {
                Image(
                    painter = painterResource(R.drawable.missingno),
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
                        .crossfade(true)
                        .build(),
                    onSuccess = {
                        val mBitmap = it.result.drawable.toBitmap()
                        Palette.from(mBitmap).generate { p ->
                            p?.dominantSwatch?.let {
                                boxBackground = Color(it.rgb)
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(dimensionResource(id = R.dimen.image_size)),
                    placeholder = painterResource(R.drawable.missingno),
                    contentDescription = stringResource(R.string.pokemon_description, name)
                )
            }
            Text(
                text = name,
                modifier = Modifier.align(Alignment.BottomCenter),
                color = textsColor
            )
    }
}

@Preview
@Composable
fun PokemonCardPreview() {
    var name by remember { mutableStateOf("MissingNo") }
    PokemonCard(id = 1, name = name, "")
}
