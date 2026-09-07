plugins {
    alias(androidLibs.plugins.application)
    alias(composeLibs.plugins.compose)
    alias(kotlinLibs.plugins.composeCompiler)
}

android {
    namespace = "dev.stashy.vmptracker"
    compileSdk = androidLibs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "dev.stashy.vmptracker"
        minSdk = androidLibs.versions.minSdk.get().toInt()
        targetSdk = androidLibs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }

        create("debugMinified") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("debug")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "${rootProject.projectDir}/proguard-rules.pro"
            )
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "${rootProject.projectDir}/proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
