package hr.kbratko.eval.gradle

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.engine.spec.tempdir
import java.io.File

class EvalGradleSubpluginFunctionalTests : ShouldSpec({

    should("work with default configuration") {
        // given
        val projectDir: File = tempdir("projectDir")

        projectDir.settingsGradleKts
            .withDefaultConfig()

        projectDir.buildGradleKts
            .registerDefaultPlugins()
            .registerCheckEvalExtensionTask()

        val runner = gradleRunner(projectDir).executeCheckEvalExtensionTask()

        // when
        val result = runner.build()

        // then
        result outputShouldContainPrefixes "[eval]"
    }

    should("work with custom configuration") {
        // given
        val projectDir: File = tempdir("projectDir")

        projectDir.settingsGradleKts
            .withDefaultConfig()

        projectDir.buildGradleKts
            .registerDefaultPlugins()
            .appendPluginConfig(
                """
                    eval {
                        prefixes("eval", "test")
                    }
                """.trimIndent()
            )
            .registerCheckEvalExtensionTask()

        val runner = gradleRunner(projectDir).executeCheckEvalExtensionTask()

        // when
        val result = runner.build()

        // then
        result outputShouldContainPrefixes "[eval, test]"
    }
})