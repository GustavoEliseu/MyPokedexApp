package com.gustavoeliseu.pokedex.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.decode.DataSource
import coil.request.SuccessResult
import coil.test.FakeImageLoaderEngine
import com.gustavoeliseu.domain.models.PokemonSimpleListItem
import com.gustavoeliseu.domain.utils.ColorEnum
import com.gustavoeliseu.myapplication.R as commonResources
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations

@Suppress("TestFunctionName")
@ExperimentalCoilApi
@RunWith(AndroidJUnit4::class)
class PokemonCardInstrumentedContentDescriptionTest {

    @get:Rule
    var composeRule = createComposeRule()

    var mMockContext: Context? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mMockContext = InstrumentationRegistry.getInstrumentation().context
        mMockContext?.let { context ->
            val dittoDrawable = context.getDrawable(commonResources.drawable.ditto_test)
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
    fun verify_if_error_content_description_exists() {
        var testDescription = ""
        composeRule.setContent {
            PokemonCardTest(-1, "Missingno", 1)
            testDescription = stringResource(
                id = commonResources.string.missing_no, "Missingno"
            )
        }
        composeRule.onNodeWithContentDescription(testDescription)
            .assertExists("Description does not exists")
    }

    @Test
    fun verify_if_correct_content_description_exists() {
        var testDescription = ""
        composeRule.setContent {
            PokemonCardTest(132, "Ditto", 7)
            testDescription = stringResource(
                id = commonResources.string.pokemon_description, "Ditto"
            )
        }
        composeRule.onNodeWithContentDescription(testDescription)
            .assertExists("Description does not exists")
    }

    @Test
    fun verify_if_error_content_description_not_exists() {
        var testDescriptionNot = ""
        composeRule.setContent {
            PokemonCardTest(132, "Ditto", 7)
            testDescriptionNot = stringResource(
                id = commonResources.string.missing_no, "Ditto"
            )
        }
        composeRule.onNodeWithContentDescription(testDescriptionNot).assertDoesNotExist()
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