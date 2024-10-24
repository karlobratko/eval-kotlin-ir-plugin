description = "Kotlin Eval Compiler Plugin (Backend)"

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gradle.buildconfig)
}

dependencies {
    implementation(libs.kotlin.compiler)
    implementation(kotlin("stdlib"))

    testImplementation(libs.test.junit.jupiter)
    testImplementation(libs.test.kotest.assertions.core)
    testImplementation(libs.test.kotest.assertions.core.jvm)
    testImplementation(libs.test.mockk)
    testRuntimeOnly(libs.test.junit.jupiter.engine)
}