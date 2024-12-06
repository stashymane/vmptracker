import de.undercouch.gradle.tasks.download.Download

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.downloadTask)
}

android {
    namespace = "dev.stashy.vtracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.stashy.vtracker"
        minSdk = 24
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.serialization.json)
    implementation(libs.serialization.protobuf)

    implementation(libs.androidx.datastore)
    implementation(libs.bundles.koin)

    implementation(libs.bundles.camerax)
    implementation(libs.mediapipe.vision)

    implementation(libs.androidx.materialIconsExtended)
    implementation(libs.accompanist.permissions)
    implementation(libs.navigation.compose)
    implementation(libs.haze)
    implementation(libs.placeholder.material3)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

val assetDir = projectDir.resolve("src/main/assets/tasks")

tasks.create("downloadFaceLandmarker", Download::class) {
    src("https://storage.googleapis.com/mediapipe-models/face_landmarker/face_landmarker/float16/latest/face_landmarker.task")
    dest(assetDir.resolve("face_landmarker.task"))
    overwrite(false)
}

tasks.create("downloadHandLandmarker", Download::class) {
    src("https://storage.googleapis.com/mediapipe-models/hand_landmarker/hand_landmarker/float16/latest/hand_landmarker.task")
    dest(assetDir.resolve("hand_landmarker.task"))
    overwrite(false)
}

tasks.preBuild {
    dependsOn("downloadFaceLandmarker", "downloadHandLandmarker")
}
