allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

val groupId: String by project
val versionName: String by project

subprojects {
    group = groupId
    version = versionName
}