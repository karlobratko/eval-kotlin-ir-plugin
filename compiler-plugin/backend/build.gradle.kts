description = "Kotlin Eval Compiler Plugin (Backend)"

plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.kotlin.compiler.embeddable)
    implementation(libs.arrow.core)
}