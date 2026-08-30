plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.konaet.cover.core.network"
    compileSdk = 36

    defaultConfig {
        minSdk = 28
        buildConfigField("String", "API_URL", "\"https://api.konaet.cover/\"")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(project(":core:model"))

    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:3.0.0")
    implementation("com.squareup.okhttp3:okhttp:5.4.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")

    // Hilt for DI
    implementation("com.google.dagger:hilt-android:2.58")
    kapt("com.google.dagger:hilt-compiler:2.58")

    // DataStore for preferences
    implementation("androidx.core:core-ktx:1.18.0")
}
