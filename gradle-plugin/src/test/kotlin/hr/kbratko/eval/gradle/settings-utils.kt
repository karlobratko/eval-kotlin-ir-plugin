package hr.kbratko.eval.gradle

import java.io.File

val File.settingsGradleKts get() = resolve("settings.gradle.kts")

val File.buildGradleKts get() = resolve("build.gradle.kts")

fun File.withDefaultConfig() = apply {
    appendText(
        """
            rootProject.name = "test-project"
        """.trimIndent()
    )
}

fun File.registerDefaultPlugins() = apply {
    appendText(
        """
            plugins {
                id("hr.kbratko.eval")
                kotlin("jvm")
            }
        
        """.trimIndent()
    )
}

fun File.appendPluginConfig(config: String) = apply {
    appendText(
        """
        $config
        
        """.trimIndent()
    )
}

fun File.registerCheckEvalExtensionTask() = apply {
    appendText(
        """
            tasks.register("checkEvalExtension") {
                doLast {
                    val evalExtension = project.extensions.findByType(hr.kbratko.eval.gradle.EvalExtension::class.java)
                    if (evalExtension != null) {
                        println("Prefixes: ${'$'}{evalExtension.prefixes.get()}")
                    } else {
                        throw RuntimeException("EvalExtension is not registered!")
                    }
                }
            }
            
        """.trimIndent()
    )
}