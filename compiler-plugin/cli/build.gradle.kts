description = "Kotlin Eval Compiler Plugin (CLI)"

plugins {
    alias(libs.plugins.kotlin.jvm)
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

    implementation(libs.kotlin.compiler.embeddable)

    implementation(libs.auto.service.annotations)
}

tasks.test {
    useJUnitPlatform()
}