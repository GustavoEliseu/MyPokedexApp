package com.gustavoeliseu.pokedex.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gustavoeliseu.domain.utils.TypeEnum
import com.gustavoeliseu.domain.utils.TypeEnum.Companion.isValid

@Composable
fun PokemonTypeRoundComposable(
    type: TypeEnum,
    modifier: Modifier = Modifier
) {
    if (type.isValid()) {
        Box(
            modifier = modifier
                .border(BorderStroke(1.dp, Color.Gray), CircleShape)
                .background(color = type.tintColor, shape = CircleShape)
        ) {
            Image(
                painter = painterResource(id = type.drawableId),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(5.dp),
                contentDescription = "Icon for type " + stringResource(id = type.nameId)
            )
        }
    }
}

@Composable
fun PokemonTypeTextComposable(
    type: TypeEnum,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .clip(RoundedCornerShape(10.dp))
        .width(80.dp)) {
        Box(
            modifier = modifier
                .background(type.tintColor)
                .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(10.dp))
                .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(10.dp))
                .padding(5.dp)
        ) {
            Text(
                text = stringResource(id = type.nameId).uppercase(),
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}


@Preview
@Composable
fun PokemonTypeTextPreview() {
    val normalEnum = TypeEnum.NORMAL
    PokemonTypeTextComposable(normalEnum)
}

@Preview
@Composable
fun PokemonTypeTextFightPreview() {
    PokemonTypeTextComposable(TypeEnum.FIGHTING)
}


@Preview
@Composable
fun PokemonTypeRoundPreview() {
    PokemonTypeRoundComposable(TypeEnum.FAIRY, Modifier)
}