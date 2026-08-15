plugins {
    alias(androidLibs.plugins.application)
    alias(composeLibs.plugins.compose)
    alias(kotlinLibs.plugins.composeCompiler)
}

android {
    namespace = "dev.stashy.vmptracker"
    compileSdk = 37

    defaultConfig {
        applicationId = "dev.stashy.vmptracker"
        minSdk = 24
        targetSdk = 37
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(projects.model)
    implementation(projects.app.common)

    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.startup)
    implementation(libs.androidx.datastore.preferences)

    implementation(androidLibs.bundles.androidx)
    implementation(androidLibs.bundles.camerax)
}
