import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

fun getLocalProperty(key: String): String {
    val properties = Properties()
    val localPropertiesFile = project.rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(localPropertiesFile.inputStream())
    }
    return properties.getProperty(key) ?: ""
}

android {
    namespace = "com.tech.pokedex"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.tech.pokedex"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", getLocalProperty("baseUrl"))
        buildConfigField("String", "IMAGE_URL", getLocalProperty("imageUrl"))
        buildConfigField("String", "DB_SECRET", getLocalProperty("dbSecretKeySha256"))
    }

    flavorDimensions += "environment"

    productFlavors {
        create("development") {
            dimension = "environment"

            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        create("production") {
            dimension = "environment"
        }
    }

    signingConfigs {
        create("release") {
            val storeFilePath = System.getenv("STORE_FILE")
            if (storeFilePath != null) {
                storeFile = file(storeFilePath)
                storePassword = System.getenv("STORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            } else {
                val localStoreFile = getLocalProperty("storeFile")
                if (localStoreFile.isNotEmpty()) {
                    storeFile = file(localStoreFile)
                    storePassword = getLocalProperty("storePassword")
                    keyAlias = getLocalProperty("keyAlias")
                    keyPassword = getLocalProperty("keyPassword")
                }
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // --- Compose & Core Defaults ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.core.splashscreen)

    // --- Production Pokedex Stack ---

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Dependency Injection (Koin)
    implementation(libs.koin.androidx.compose)

    // Networking (Retrofit)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    // Database (Room)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.lifecycle.process)
    ksp(libs.androidx.room.compiler) // Using KSP to process Room annotations

    implementation(libs.room.paging)
    implementation(libs.androidx.datastore.preferences)

    // Sql security
    implementation(libs.bundles.database.security)

    // Images (Coil)
    implementation(libs.coil.compose)

    // Pagination (Paging 3)
    implementation(libs.androidx.paging.compose)

    // Biometric
    implementation(libs.androidx.biometric)

    implementation(libs.androidx.compose.material.icons.extended)

    // --- Testing ---
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}