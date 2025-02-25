plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("com.google.devtools.ksp") version "2.0.20-1.0.24"
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.test"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.test"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // coroutines support
    ksp(libs.androidx.room.compiler)


    // JUnit for unit tests
    testImplementation(libs.junit)

    // mockk
    val mockkVersion = "1.13.9"
    testImplementation("io.mockk:mockk-android:${mockkVersion}")
    testImplementation("io.mockk:mockk-agent:${mockkVersion}")


    // JUnit 5 for testing
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.jupiter.junit.jupiter.engine)

    // Android instrumented tests
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    testImplementation(libs.kotlinx.coroutines.test)

    // Debugging tools
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}