plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(androidLibs.plugins.library)
    alias(kotlinLibs.plugins.serialization)

    id("multiplatform.target.androidLibrary")
}

kotlin {
    android {
        namespace = "dev.stashy.vmptracker.model"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(kotlinLibs.serialization.core)
            implementation(kotlinLibs.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.androidx.datastore.core)
        }

        commonTest.dependencies {
            implementation(kotlinLibs.test)
        }
    }
}
