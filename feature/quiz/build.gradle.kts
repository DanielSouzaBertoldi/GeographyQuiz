plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.secretsGradlePlugin)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "daniel.bertoldi.quiz"
    compileSdk = 34

    defaultConfig {
        minSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/*"
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
        buildConfig = true
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    // external modules
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.dagger.hilt)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi.converter)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.compose.navigation)
    implementation(libs.hilt.compose.navigation)
    implementation(libs.kotlinx.serialization)
    implementation(libs.gemini.generative.ai)

    // internal modules
    implementation(projects.network)

    ksp(libs.hilt.compiler)
    ksp(libs.moshi.codegen)
    kspAndroidTest(libs.hilt.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.jupiter.api)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlin.test)
    testImplementation(projects.testUtils)
    testRuntimeOnly(libs.jupiter.engine)
    testRuntimeOnly(libs.jupiter.vintage)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.kotlin.test)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(projects.testUtils)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}