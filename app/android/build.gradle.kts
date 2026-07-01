import de.undercouch.gradle.tasks.download.Download

plugins {
    alias(androidLibs.plugins.application)
    alias(composeLibs.plugins.compose)
    alias(kotlinLibs.plugins.composeCompiler)
    alias(libs.plugins.downloadTask)
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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(libs.mediapipe.vision)
}

val assetDir = projectDir.resolve("src/main/assets/tasks")

tasks.register("downloadFaceLandmarker", Download::class) {
    description = "Download face landmarker task"
    src("https://storage.googleapis.com/mediapipe-models/face_landmarker/face_landmarker/float16/latest/face_landmarker.task")
    dest(assetDir.resolve("face_landmarker.task"))
    overwrite(false)
}

tasks.register("downloadHandLandmarker", Download::class) {
    description = "Download hand landmarker task"
    src("https://storage.googleapis.com/mediapipe-models/hand_landmarker/hand_landmarker/float16/latest/hand_landmarker.task")
    dest(assetDir.resolve("hand_landmarker.task"))
    overwrite(false)
}

tasks.preBuild {
    dependsOn("downloadFaceLandmarker", "downloadHandLandmarker")
}
