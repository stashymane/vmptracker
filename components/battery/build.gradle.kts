plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(androidLibs.plugins.library)

    id("multiplatform.target.androidLibrary")
}

kotlin {
    android {
        namespace = "dev.stashy.battery"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(kotlinLibs.coroutines.core)
        }

        androidMain.dependencies {
            implementation(androidLibs.androidx.core.ktx)
            implementation(androidLibs.androidx.startup)
            implementation(androidLibs.androidx.lifecycle.runtime)
        }
    }
}

