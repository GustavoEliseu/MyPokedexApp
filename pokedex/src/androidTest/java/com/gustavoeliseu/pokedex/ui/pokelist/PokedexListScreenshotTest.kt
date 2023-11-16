package com.gustavoeliseu.pokedex.ui.pokelist

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.platform.app.InstrumentationRegistry
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.decode.DataSource
import coil.request.SuccessResult
import coil.test.FakeImageLoaderEngine
import com.gustavoeliseu.domain.models.PokemonSimpleList
import com.gustavoeliseu.myapplication.R as commonResources
import com.karumi.shot.ScreenshotTest
import com.gustavoeliseu.pokedex.ui.PokedexListScreen
import com.gustavoeliseu.pokedexdata.models.GenericPokemonData
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("TestFunctionName")
@ExperimentalCoilApi
class PokedexListScreenshotTest : ScreenshotTest {
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
                            data.endsWith("132.png") -> {
                                commonResources.drawable.ditto_test
                            }

                            data.endsWith("10.png") -> {
                                commonResources.drawable.caterpie_test
                            }

                            data.endsWith("18.png") -> {
                                commonResources.drawable.pidgeot_test
                            }

                            data.endsWith("6.png") -> {
                                commonResources.drawable.charizard_test
                            }

                            data.endsWith("43.png") -> {
                                commonResources.drawable.oddish_test
                            }

                            data.endsWith("82.png") -> {
                                commonResources.drawable.magneton_test
                            }

                            else -> 0
                        }
                        val drawable = context.getDrawable(drawableId)
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
    fun ScreenShotTestList() {
        composeRule.setContent {
            PokedexListFragmentPreview()
        }
        compareScreenshot(composeRule)
    }


    @Composable
    fun PokedexListFragmentPreview() {
        val data = PokemonSimpleList.getSimpleListExample(false).map { it as GenericPokemonData }
        val flow = MutableStateFlow(PagingData.from(data))
        val lazyPagingItems = flow.collectAsLazyPagingItems()
        PokedexListScreen(Modifier, lazyPagingItems, {}, false, "", {}, {}, {})
    }
}