package com.gustavoeliseu.pokedex.ui.details

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.decode.DataSource
import coil.request.SuccessResult
import coil.test.FakeImageLoaderEngine
import com.google.gson.Gson
import com.gustavoeliseu.domain.models.PokemonSpecy
import com.gustavoeliseu.myapplication.R
import com.gustavoeliseu.pokedex.ui.showPokemonAndEvolutions
import com.karumi.shot.ScreenshotTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


@ExperimentalCoilApi
class PokemonEvolutionsScreenshotTest : ScreenshotTest {

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
        } catch (e: IOException) {

        } finally {
            inputStream.close()
        }
        byteArrayOutputStream.toString()?.let {
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
    fun verifyShotRuleBulba() {
        composeRule.setContent {
            bulba?.let {
            PokemonEvolutionsTestProvider(spec = it)
            }
        }
        compareScreenshot(composeRule)
    }

    @Test
    fun verifyShotRuleRalts() {
        composeRule.setContent {
            ralts?.let {
                PokemonEvolutionsTestProvider(spec = it)
            }
        }
        compareScreenshot(composeRule)
    }

    @Test
    fun verifyShotRuleEevee() {
        composeRule.setContent {
            eevee?.let {
                PokemonEvolutionsTestProvider(spec = it)
            }
        }
        compareScreenshot(composeRule)
    }



    @Composable
    fun PokemonEvolutionsTestProvider(spec: PokemonSpecy) {
        showPokemonAndEvolutions(spec)
    }
}