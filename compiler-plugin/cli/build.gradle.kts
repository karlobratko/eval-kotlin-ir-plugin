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

    implementation(libs.kotlin.compiler)
    implementation(kotlin("stdlib"))

    implementation(libs.auto.service.annotations)

    implementation(kotlin("compiler-embeddable"))
    implementation(kotlin("compiler"))

    testImplementation(kotlin("test")) // Includes JUnit and Kotlin Test DSL
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.6.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}