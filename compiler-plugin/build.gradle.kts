description = "Kotlin Eval Compiler Plugin"

plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":compiler-plugin:backend"))
    implementation(project(":compiler-plugin:cli"))

    testImplementation(libs.test.kotest.junit5)
    testImplementation(libs.test.kotest.datatest)
    testImplementation(libs.test.kotest.property)
    testImplementation(libs.test.kotest.assertions.core)
    testImplementation(libs.test.kotest.assertions.core.jvm)

    testImplementation(libs.test.kotlin.compile)

    testImplementation(libs.asm.core)
    testImplementation(libs.asm.tree)
}

tasks.test {
    useJUnitPlatform()
}