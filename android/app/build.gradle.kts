plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.metaclone.android"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.metaclone.android"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}
dependencies {
    //feature
    implementation(project(":feature:authorization"))
    //data
    implementation(project(":data:auth-repository"))

    implementation(libs.bundles.core)
    implementation(libs.bundles.ui)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.navigation)
}