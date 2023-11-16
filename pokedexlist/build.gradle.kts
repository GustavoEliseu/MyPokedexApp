@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("shot")
}

android {
    namespace = "com.gustavoeliseu.pokedexlist"
    compileSdk = 34

    shot {
        applicationId = "ge.pokedexlist"
    }
    defaultConfig {
        testApplicationId = "ge.pokedexlist"
        minSdk = 26
        testInstrumentationRunner = "com.karumi.shot.ShotTestRunner"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("localHost") {
            isMinifyEnabled = false
            buildFeatures.buildConfig = true
            initWith(getByName("debug"))
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":commonui"))
    implementation(libs.androidx.ktx)
    implementation(platform(libs.kotlin.bom))
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(project(mapOf("path" to ":data")))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.material3)

    //Compose
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.compose.ui)
    implementation(libs.compose.graphics)
    implementation(platform(libs.compose.bom))
    // AndroidX Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Coil
    implementation(libs.coil.compose)
    // Palette
    implementation(libs.palette)

    // Dagger Hilt
    implementation(libs.hilt.core)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation)

    // Paging
    implementation(libs.androidx.paging.compose)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.compose.junit)
    androidTestImplementation(libs.compose.test)
    debugImplementation(libs.compose.test)
    androidTestImplementation(libs.mockito.android)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    androidTestImplementation(libs.coil.test)
}