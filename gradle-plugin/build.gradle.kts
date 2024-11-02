plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gradle.buildconfig)

    `java-gradle-plugin`
}

val groupId: String by project
val versionName: String by project
val pluginId: String by project
val packagePath = "$groupId.gradle"

buildConfig {
    className("BuildProperties")
    packageName(packagePath)

    useKotlinOutput {
        internalVisibility = true
        topLevelConstants = true
    }

    buildConfigField<String>("PLUGIN_ID", pluginId)

    buildConfigField<String>("GROUP_ID", groupId)
    buildConfigField<String>("ARTIFACT_ID", "eval-plugin")
    buildConfigField<String>("VERSION", versionName)
}

gradlePlugin {
    plugins {
        create("evalPlugin") {
            id = groupId
            displayName = "Kotlin Eval Compiler Plugin"
            description = displayName
            implementationClass = "$packagePath.EvalGradleSubplugin"
        }
    }
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)

    testImplementation(libs.test.junit.jupiter)
    testImplementation(libs.test.kotest.assertions.core)
    testImplementation(libs.test.kotest.assertions.core.jvm)
    testImplementation(libs.test.mockk)
    testRuntimeOnly(libs.test.junit.jupiter.engine)
}

val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])
configurations["functionalTestRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])

val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    useJUnitPlatform()
}

gradlePlugin.testSourceSets.add(functionalTestSourceSet)

tasks.named("jar") {
    enabled = false
}

tasks.named<Task>("check") {
    dependsOn(functionalTest)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
