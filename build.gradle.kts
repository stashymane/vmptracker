plugins {
    alias(kotlinLibs.plugins.multiplatform) apply false
    alias(kotlinLibs.plugins.serialization) apply false
    alias(kotlinLibs.plugins.composeCompiler) apply false
    alias(composeLibs.plugins.compose) apply false
    alias(composeLibs.plugins.hotReload) apply false
    alias(androidLibs.plugins.application) apply false
    alias(androidLibs.plugins.library) apply false
}

group = "dev.stashy.vmptracker"
