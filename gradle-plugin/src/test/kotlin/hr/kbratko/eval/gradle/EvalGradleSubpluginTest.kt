package hr.kbratko.eval.gradle

import hr.kbratko.eval.gradle.model.EvalBuilder
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.Project
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EvalGradleSubpluginTest {

    private val registry = mockk<ToolingModelBuilderRegistry>(relaxed = true)
    private lateinit var evalSubplugin: EvalGradleSubplugin

    @BeforeEach
    fun setUp() {
        evalSubplugin = EvalGradleSubplugin(registry)
    }

    @Test
    fun `apply should register EvalExtension and EvalBuilder`() {
        val project = mockk<Project>(relaxed = true)
        val evalExtension = mockk<EvalExtension>(relaxed = true)

        every { project.extensions.create("eval", EvalExtension::class.java, any()) } returns evalExtension

        evalSubplugin.apply(project)

        verify { project.extensions.create("eval", EvalExtension::class.java, any()) }
        verify { registry.register(EvalBuilder) }
    }

    @Test
    fun `isApplicable should always return true`() {
        val kotlinCompilation = mockk<KotlinCompilation<*>>()

        evalSubplugin.isApplicable(kotlinCompilation).shouldBeTrue()
    }

    @Test
    fun `getCompilerPluginId should return correct ID`() {
        PLUGIN_ID shouldBeEqual evalSubplugin.getCompilerPluginId()
    }

    @Test
    fun `getPluginArtifact should return correct artifact details`() {
        val artifact = evalSubplugin.getPluginArtifact()

        ARTIFACT_ID shouldBeEqual artifact.artifactId
        GROUP_ID shouldBeEqual artifact.groupId
        VERSION shouldBeEqual artifact.version!!
    }
}
