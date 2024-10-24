pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "eval"

include(":gradle-plugin")

include(":compiler-plugin")
include(":compiler-plugin:cli")
include(":compiler-plugin:backend")