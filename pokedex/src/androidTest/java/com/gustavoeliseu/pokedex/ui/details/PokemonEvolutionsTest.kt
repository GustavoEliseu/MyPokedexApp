package com.gustavoeliseu.pokedex.ui.details

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.decode.DataSource
import coil.request.SuccessResult
import coil.test.FakeImageLoaderEngine
import com.google.gson.Gson
import com.gustavoeliseu.commonui.utils.extensions.colorDistance
import com.gustavoeliseu.domain.models.PokemonSpecy
import com.gustavoeliseu.domain.utils.ColorEnum
import com.gustavoeliseu.domain.utils.Const.EVOLUTION_CARD_LOCATION_TEST
import com.gustavoeliseu.domain.utils.Const.EVOLUTION_CARD_TEST
import com.gustavoeliseu.myapplication.R
import com.gustavoeliseu.pokedex.ui.showPokemonAndEvolutions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

@Suppress("TestFunctionName")
@ExperimentalCoilApi
@RunWith(AndroidJUnit4::class)
class PokemonEvolutionsTest {

    @get:Rule
    var composeRule = createComposeRule()
    var mMockContext: Context? = null

    var eevee: PokemonSpecy? = null
    var bulba: PokemonSpecy? = null
    var ralts: PokemonSpecy? = null
    val bulbaConst = "bulba"
    val raltsConst = "ralts"
    val eeveeConst = "eevee"

    @Before
    fun setup() {
        mMockContext = InstrumentationRegistry.getInstrumentation().context
        mMockContext?.let { context ->
            initPokemonSample(context,bulbaConst)
            initPokemonSample(context,raltsConst)
            initPokemonSample(context,eeveeConst)
            val engine = FakeImageLoaderEngine.Builder()
                .addInterceptor { chain ->
                    val data = chain.request.data
                    if (data is String) {
                        val drawableId = when {
                            //Eevee
                            data.endsWith("133.png") ->{
                                R.drawable.eevee_test
                            }
                            data.endsWith("134.png") ->{
                                R.drawable.vaporeon_test
                            }
                            data.endsWith("135.png") ->{
                                R.drawable.jolteon_test
                            }
                            data.endsWith("136.png") ->{
                                R.drawable.flareon_test
                            }
                            data.endsWith("196.png") ->{
                                R.drawable.espeon_test
                            }
                            data.endsWith("197.png") ->{
                                R.drawable.umbreon_test
                            }
                            data.endsWith("470.png") ->{
                                R.drawable.leafeon_test
                            }
                            data.endsWith("471.png") ->{
                                R.drawable.glaceon_test
                            }
                            data.endsWith("700.png") ->{
                                R.drawable.sylveon_test
                            }

                            //Bulba
                            data.endsWith("1.png") ->{
                                R.drawable.bulbasaur_test
                            }
                            data.endsWith("2.png") ->{
                                R.drawable.ivysaur_test
                            }
                            data.endsWith("3.png") ->{
                                R.drawable.venusaur_test
                            }

                            //Ralts
                            data.endsWith("280.png") ->{
                                R.drawable.ralts_test
                            }
                            data.endsWith("281.png") ->{
                                R.drawable.kirlia_test
                            }
                            data.endsWith("282.png") ->{
                                R.drawable.gardevoir_test
                            }
                            data.endsWith("475.png") ->{
                                R.drawable.gallade_test
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

    private fun initPokemonSample(context: Context, spec: String){
        val id: Int =when(spec){
            bulbaConst->{ R.raw.bulbasaur}
            raltsConst->{ R.raw.ralts}
            eeveeConst->{ R.raw.eevee}
            else -> return
        }
        val inputStream: InputStream = context.resources.openRawResource(id)

        val byteArrayOutputStream = ByteArrayOutputStream()

        var i: Int
        try {
            i = inputStream.read()
            while (i != -1) {
                byteArrayOutputStream.write(i)
                i = inputStream.read()
            }
            inputStream.close()
        } catch (_: IOException) {

        } finally {
            inputStream.close()
        }
        byteArrayOutputStream.toString().let {
            val gson = Gson()
            val result: PokemonSpecy? = gson.fromJson(it, PokemonSpecy::class.java)
            when(spec){
                bulbaConst->{ bulba = result}
                raltsConst->{ ralts = result}
                eeveeConst->{ eevee = result}
            }
        }
    }

    @Test
    fun verify_if_evolutions_bulba_size() {
        composeRule.setContent {
            bulba?.let {
                PokemonEvolutionsTestProvider(it)
            }
        }
        composeRule.onAllNodesWithTag(EVOLUTION_CARD_TEST)
            .assertCountEquals(2) //number of bulbasaur evolution cards
    }

    @Test
    fun verify_if_evolutions_eevee_size() {
        composeRule.setContent {
            eevee?.let {
                PokemonEvolutionsTestProvider(it)
            }
        }
        composeRule.onAllNodesWithTag(EVOLUTION_CARD_TEST)
            .assertCountEquals(8) //number of eevee evolution cards

        composeRule.onAllNodesWithTag(EVOLUTION_CARD_LOCATION_TEST).assertCountEquals(2)
    }

    @Test
    fun verify_if_evolutions_ralts_size() {
        composeRule.setContent {
            ralts?.let {
                PokemonEvolutionsTestProvider(it)
            }
        }
        composeRule.onAllNodesWithTag(EVOLUTION_CARD_TEST)
            .assertCountEquals(3) //number of ralts evolution cards
    }

    @Composable
    fun PokemonEvolutionsTestProvider(spec: PokemonSpecy) {
        showPokemonAndEvolutions(spec)
    }
}