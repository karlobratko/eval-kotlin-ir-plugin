package hr.kbratko.eval.gradle

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

// TODO: use Kotest
class EvalGradleSubpluginFunctionalTest {

    @TempDir
    lateinit var projectDir: File

    @Test
    fun `should work with default configuration`() {
        // given
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

    @Test
    fun `should work with custom configuration`() {
        // given
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
}