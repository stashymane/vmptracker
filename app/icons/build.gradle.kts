plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(kotlinLibs.plugins.serialization)
    alias(kotlinLibs.plugins.composeCompiler)
    alias(composeLibs.plugins.compose)
    alias(composeLibs.plugins.hotReload)
    alias(androidLibs.plugins.library)

    id("multiplatform.target.androidLibrary")
}

kotlin {
    android {
        namespace = "dev.stashy.vmptracker.icons"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(composeLibs.bundles.jb)
        }
    }
}

dependencies {
    androidRuntimeClasspath(composeLibs.jb.uiTooling)
}
