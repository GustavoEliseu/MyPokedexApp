package com.gustavoeliseu.pokedexlist.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.decode.DataSource
import coil.request.SuccessResult
import coil.test.FakeImageLoaderEngine
import com.gustavoeliseu.domain.entity.PokemonSimpleListItem
import com.gustavoeliseu.domain.utils.ColorEnum
import com.gustavoeliseu.pokedexlist.R
import com.karumi.shot.ScreenshotTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("TestFunctionName")
@ExperimentalCoilApi
class PokemonCardScreenshotTest : ScreenshotTest {
    @get:Rule
    var composeRule = createComposeRule()
    var mMockContext: Context? = null

    @Before
    fun setup() {
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
                .default(ColorDrawable(Color.BLUE))
                .build()
            val imageLoader = ImageLoader.Builder(context)
                .components { add(engine) }
                .build()
            Coil.setImageLoader(imageLoader)

        }
    }

    @Test
    fun missingnoCardTest() {
        composeRule.setContent {
            PokemonCardTest(
                id = -1,
                name = "Missingno",
                pokemonColorId = 1
            )
        }
        compareScreenshot(composeRule)

    }

    @Test
    fun verify_pokemon_ditto_card_snapshot() {
        composeRule.setContent {
            PokemonCardTest(
                id = 132,
                name = "Ditto",
                pokemonColorId = 6
            )
        }
        compareScreenshot(composeRule)
    }

    @Test
    fun verify_pokemon_caterpie_card_snapshot() {
        composeRule.setContent {
            PokemonCardTest(
                id = 10,
                name = "Caterpie",
                pokemonColorId = 1
            )
        }
        compareScreenshot(composeRule)
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