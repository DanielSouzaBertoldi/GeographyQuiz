plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "daniel.bertoldi.geographyquiz"
    compileSdk = 34

    defaultConfig {
        applicationId = "daniel.bertoldi.geographyquiz"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "daniel.bertoldi.geographyquiz.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            // had to add these to avoid weird errors with Mockk in instrumented tests
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    // external modules
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
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
    implementation(libs.datastore.prefs)
    implementation(libs.lottie) // TODO: won't be able to use lottie files unfortunately..
    implementation(libs.kotlinx.serialization)

    // internal modules
    implementation(project(":database"))
    implementation(project(":network"))

    ksp(libs.hilt.compiler)
    ksp(libs.moshi.codegen)
    kspAndroidTest(libs.hilt.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.jupiter.api)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.turbine) // TODO: remove this guy if I can't use it.
    testImplementation(projects.testUtils)
    testImplementation(testFixtures(projects.database))
    testImplementation(testFixtures(projects.network))
    testRuntimeOnly(libs.jupiter.engine)
    testRuntimeOnly(libs.jupiter.vintage)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.compose.navigation.testing)
    androidTestImplementation(libs.dagger.hilt.testing)
    androidTestImplementation(libs.kotlin.test)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(projects.testUtils)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}