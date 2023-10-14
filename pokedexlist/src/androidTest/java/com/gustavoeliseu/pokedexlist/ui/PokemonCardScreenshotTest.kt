package com.gustavoeliseu.pokedexlist.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
            val engine = FakeImageLoaderEngine.Builder()
                .addInterceptor { chain ->
                    val data = chain.request.data
                    if (data is String) {
                        val drawableId = when {
                            data.endsWith("132.png") ->{
                                R.drawable.ditto_test
                            }
                            data.endsWith("10.png") ->{
                                R.drawable.caterpie_test
                            }
                            else->{
                                0
                            }
                        }
                        val drawable = context.getDrawable(drawableId)
                        drawable?.let {
                            SuccessResult(
                                drawable = it,
                                request = chain.request,
                                dataSource = DataSource.MEMORY,
                            )
                        }

                        drawable?.let {
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
                pokemonColorId = 7
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
                pokemonColorId = 5
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