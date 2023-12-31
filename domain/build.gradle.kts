@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.apollo3)
    id ("kotlin-kapt")
}

android {
    namespace = "com.gustavoeliseu.domain"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose =true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            buildFeatures.buildConfig= true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField( "String", "POKEAPI_URL", "\"https://pokeapi.co/api/v2/\"")
            buildConfigField("String", "GRAPHQLAPI_URL", "\"https://beta.pokeapi.co/graphql/v1beta\"")
            buildConfigField("okhttp3.logging.HttpLoggingInterceptor.Level", "INTERCEPTOR_LEVEL", "okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildFeatures.buildConfig= true
            buildConfigField("String", "POKEAPI_URL", "\"https://pokeapi.co/api/v2/\"")
            buildConfigField("String", "GRAPHQLAPI_URL", "\"https://beta.pokeapi.co/graphql/v1beta\"")
            buildConfigField("okhttp3.logging.HttpLoggingInterceptor.Level", "INTERCEPTOR_LEVEL", "okhttp3.logging.HttpLoggingInterceptor.Level.BODY")
        }
        create("localHost") {
            isMinifyEnabled = false
            buildFeatures.buildConfig= true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "POKEAPI_URL", "\"https://10.0.2.2:8080/api/v2/\"")
            buildConfigField("String", "GRAPHQLAPI_URL", "\"http://10.0.2.2:8080/v1/graphql\"")
            buildConfigField("okhttp3.logging.HttpLoggingInterceptor.Level", "INTERCEPTOR_LEVEL", "okhttp3.logging.HttpLoggingInterceptor.Level.BODY")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

apollo {
    service("service") {
        packageName.set("com.gustavoeliseu.pokedex")
        srcDir("src/main/graphql")
        // instruct the compiler to generate Kotlin models
        generateKotlinModels.set(true)
    }
}

dependencies {
    implementation(libs.androidx.ktx)
    implementation(platform(libs.kotlin.bom))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Compose
    implementation (libs.compose.ui)
    implementation (libs.compose.graphics)
    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.material3)

    //Firebase Crashlytics &firebase_ver
    implementation(libs.firebase.crashlytics)

    // Navigation
    implementation (libs.androidx.navigation.compose)

    // AndroidX Lifecycle
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.runtime.compose)

    // Dagger Hilt
    implementation(libs.hilt.core)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation)

    // Network
    //apollo
    implementation(libs.apollo.runtime)
    implementation(libs.apollo.api)
    implementation(libs.apollo.cache)
    implementation(libs.apollo.cache.sqlite)
    //okhttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Paging
    implementation(libs.androidx.paging.compose)
}