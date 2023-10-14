package com.gustavoeliseu.pokedexlist.ui

import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.decode.DataSource
import coil.request.SuccessResult
import coil.test.FakeImageLoaderEngine
import com.gustavoeliseu.commonui.utils.extensions.colorDistance
import com.gustavoeliseu.domain.entity.PokemonSimpleListItem
import com.gustavoeliseu.domain.utils.ColorEnum
import com.gustavoeliseu.pokedexlist.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import java.util.Locale


@Suppress("TestFunctionName")
@ExperimentalCoilApi
@RunWith(AndroidJUnit4::class)
class PokemonCardInstrumentedColorTest {
    @get:Rule
    var composeRule = createComposeRule()

    var mMockContext: Context? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mMockContext = InstrumentationRegistry.getInstrumentation().context
        mMockContext?.let { context ->
            val dittoDrawable = context.getDrawable(R.drawable.ditto_test)
            val caterpieDrawable = context.getDrawable(R.drawable.caterpie_test)
            val engine = FakeImageLoaderEngine.Builder()
                .addInterceptor { chain ->
                    val data = chain.request.data
                    if (data is String && data.endsWith("132.png")) {
                        dittoDrawable?.let {
                            SuccessResult(
                                drawable = it,
                                request = chain.request,
                                dataSource = DataSource.MEMORY,
                            )
                        }
                    } else if (data is String && data.endsWith("10.png")) {
                        caterpieDrawable?.let {
                            SuccessResult(
                                drawable = it,
                                request = chain.request,
                                dataSource = DataSource.MEMORY,
                            )
                        }
                    } else {
                        null
                    }
                }
                .default(ColorDrawable(android.graphics.Color.BLUE))
                .build()
            val imageLoader = ImageLoader.Builder(context)
                .components { add(engine) }
                .build()
            Coil.setImageLoader(imageLoader)
        }
    }

    @Test
    fun verify_if_ditto_color_is_close_to_default() {
        val ditto = "Ditto"
        composeRule.setContent {
            PokemonCardTest(
                id = 132,
                name = ditto,
                pokemonColorId = 7
            )
        }
        val arrayDitto = IntArray(20)
        composeRule.onNodeWithTag("PokemonCardBox").captureToImage()
            .readPixels(arrayDitto, startY = 45, startX = 50, width = 5, height = 4)

        arrayDitto.forEach { color ->
            assert(Color(color).colorDistance(ColorEnum.PURPLE.tintColor) < .5f)
        }
    }

    @Test
    fun verify_if_missingno_color_is_close_to_default() {
        val missingno = "Missingno"
        composeRule.setContent {
            PokemonCardTest(
                id = -1,
                name = missingno,
                pokemonColorId = 1
            )
        }
        val arrayDitto = IntArray(20)
        composeRule.onNodeWithTag("PokemonCardBox").captureToImage()
            .readPixels(arrayDitto, startY = 45, startX = 50, width = 5, height = 4)

        arrayDitto.forEach { color ->
            assert(Color(color).colorDistance(ColorEnum.BLACK.tintColor) < .5f)
        }
    }

    @Test
    fun verify_if_caterpie_color_is_close_to_default() {
        val caterpie = "Caterpie"
        composeRule.setContent {
            PokemonCardTest(
                id = 10,
                name = caterpie,
                pokemonColorId = 5
            )
        }
        val arrayCaterpie = IntArray(20)
        composeRule.onNodeWithTag("PokemonCardBox").captureToImage()
            .readPixels(arrayCaterpie, startY = 450, startX = 500, width = 5, height = 4)
        arrayCaterpie.forEach { color ->
            assert(Color(color).colorDistance(ColorEnum.GREEN.tintColor) < .5f)
        }
    }

    @Composable
    fun PokemonCardTest(id: Int, name: String, pokemonColorId: Int) {
        PokemonCard(
            pokemonItemSimple = PokemonSimpleListItem(
                id = id,
                name = name,
                pokemonColorId = pokemonColorId
            ),
            colorEnum = ColorEnum.fromInt(pokemonColorId),
            modifier = Modifier.clickable {
            }
        )
    }
}