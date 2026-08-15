plugins {
    alias(kotlinLibs.plugins.multiplatform)
    alias(kotlinLibs.plugins.serialization)
    alias(kotlinLibs.plugins.composeCompiler)
    alias(composeLibs.plugins.compose)
    alias(composeLibs.plugins.hotReload)
    alias(androidLibs.plugins.library)

    id("multiplatform.target.androidLibrary")
    id("multiplatform.plugin.compose")
}

kotlin {
    android {
        namespace = "dev.stashy.vmptracker.common"

        androidResources {
            enable = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.model)
            implementation(projects.app.icons)
            implementation(projects.components.battery)

            implementation(kotlinLibs.serialization.json)
            implementation(kotlinLibs.serialization.protobuf)
            implementation(kotlinLibs.datetime)
            implementation(libs.kotlin.logging)

            implementation(composeLibs.bundles.jb)
            implementation(composeLibs.bundles.lifecycle)
            implementation(composeLibs.bundles.adaptive)
            implementation(composeLibs.bundles.nav3)
            implementation(composeLibs.bundles.app)

            implementation(libs.androidx.datastore.core)
            implementation(libs.bundles.koin)
        }

        androidMain.dependencies {
            implementation(libs.slf4j.android)

            implementation(androidLibs.bundles.camerax)
            implementation(androidLibs.accompanist.permissions)

            implementation(androidLibs.androidx.core.ktx)
        }
    }
}

dependencies {
    androidRuntimeClasspath(composeLibs.jb.uiTooling)
}
