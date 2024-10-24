description = "Kotlin Eval Compiler Plugin"

plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":compiler-plugin:backend"))
    implementation(project(":compiler-plugin:cli"))
}