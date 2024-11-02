description = "Kotlin Eval Compiler Plugin (CLI)"

plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("kapt")
    alias(libs.plugins.gradle.buildconfig)
}

val pluginId: String by project
val groupId: String by project
val packagePath = groupId

buildConfig {
    className("BuildProperties")
    packageName(packagePath)

    useKotlinOutput {
        internalVisibility = true
        topLevelConstants = true
    }

    buildConfigField<String>("PLUGIN_ID", pluginId)
}

dependencies {
    implementation(project(":compiler-plugin:backend"))

    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.24")

    implementation(libs.auto.service.annotations)
    implementation(libs.arrow.core)

    testImplementation(libs.test.kotest.junit5)
    testImplementation(libs.test.kotest.datatest)
    testImplementation(libs.test.kotest.property)
    testImplementation(libs.test.kotest.assertions.core)
    testImplementation(libs.test.kotest.assertions.core.jvm)

    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.6.0")

    testImplementation("org.ow2.asm:asm:9.7.1")
    testImplementation("org.ow2.asm:asm-tree:9.7.1")
}

tasks.test {
    useJUnitPlatform()
}