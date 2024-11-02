description = "Kotlin Eval Compiler Plugin (Backend)"

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gradle.buildconfig)
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.24")
    implementation(libs.arrow.core)
}