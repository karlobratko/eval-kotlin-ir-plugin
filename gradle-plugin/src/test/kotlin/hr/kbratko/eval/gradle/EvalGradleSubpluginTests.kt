package hr.kbratko.eval.gradle

import hr.kbratko.eval.gradle.model.EvalBuilder
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.Project
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation

class EvalGradleSubpluginTests : ShouldSpec({

    val registry = mockk<ToolingModelBuilderRegistry>(relaxed = true)
    lateinit var evalSubplugin: EvalGradleSubplugin

    beforeTest {
        evalSubplugin = EvalGradleSubplugin(registry)
    }

    should("register EvalExtension and EvalBuilder on apply call") {
        val project = mockk<Project>(relaxed = true)
        val evalExtension = mockk<EvalExtension>(relaxed = true)

        every { project.extensions.create("eval", EvalExtension::class.java, any()) } returns evalExtension

        evalSubplugin.apply(project)

        verify { project.extensions.create("eval", EvalExtension::class.java, any()) }
        verify { registry.register(EvalBuilder) }
    }

    should("always return true on isApplicable call") {
        val kotlinCompilation = mockk<KotlinCompilation<*>>()

        evalSubplugin.isApplicable(kotlinCompilation).shouldBeTrue()
    }

    should("return correct ID on getCompilerPluginId call") {
        PLUGIN_ID shouldBeEqual evalSubplugin.getCompilerPluginId()
    }

    should("return correct artifact details on getPluginArtifact call") {
        val artifact = evalSubplugin.getPluginArtifact()

        ARTIFACT_ID shouldBeEqual artifact.artifactId
        GROUP_ID shouldBeEqual artifact.groupId
        VERSION shouldBeEqual artifact.version!!
    }
})