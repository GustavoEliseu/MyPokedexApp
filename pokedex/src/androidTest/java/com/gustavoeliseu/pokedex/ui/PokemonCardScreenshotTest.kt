package com.gustavoeliseu.pokedex.ui

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
import com.gustavoeliseu.domain.models.PokemonSimpleListItem
import com.gustavoeliseu.domain.utils.ColorEnum
import com.gustavoeliseu.domain.utils.GenderEnum
import com.gustavoeliseu.myapplication.R as commonResources
import com.karumi.shot.ScreenshotTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("TestFunctionName")
@ExperimentalCoilApi
class PokemonCardScreenshotTest : ScreenshotTest {
    //Run with pixel 3a api34 1080x2220  dp393x808
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
                                commonResources.drawable.ditto_test
                            }
                            data.endsWith("10.png") ->{
                                commonResources.drawable.caterpie_test
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

    @Test
    fun verify_pokemon_ditto_simple_card_snapshot() {
        composeRule.setContent {
            PokemonCardTest(
                id = 132,
                name = "Ditto",
                pokemonColorId = 7,
                simple = true
            )
        }
        compareScreenshot(composeRule)
    }

    @Test
    fun verify_pokemon_simple_female_caterpie_card_snapshot() {
        composeRule.setContent {
            PokemonCardTest(
                id = 10,
                name = "Caterpie",
                pokemonColorId = 5,
                simple = true,
                genderEnum = GenderEnum.FEMALE
            )
        }
        compareScreenshot(composeRule)
    }

    @Test
    fun verify_pokemon_simple_male_caterpie_card_snapshot() {
        composeRule.setContent {
            PokemonCardTest(
                id = 10,
                name = "Caterpie",
                pokemonColorId = 5,
                simple = true,
                genderEnum = GenderEnum.MALE
            )
        }
        compareScreenshot(composeRule)
    }

    @Composable
    fun PokemonCardTest(id: Int, name: String, pokemonColorId: Int, simple:Boolean = false, genderEnum: GenderEnum = GenderEnum.INVALID) {
        PokemonCard(
            pokemonItemSimple = PokemonSimpleListItem(
                id = id,
                name = name,
                pokemonColorId = pokemonColorId,
                gender = genderEnum
            ),
            colorEnum = ColorEnum.fromInt(pokemonColorId),
            modifier = Modifier.clickable {
            },
            simpleCard = simple
        )
    }
}