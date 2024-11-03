package hr.kbratko.eval.gradle.model

import hr.kbratko.eval.gradle.EvalExtension
import org.gradle.api.Project
import org.gradle.tooling.provider.model.ToolingModelBuilder

internal object EvalBuilder : ToolingModelBuilder {

    override fun canBuild(modelName: String): Boolean {
        return modelName == Eval::class.java.name
    }

    override fun buildAll(modelName: String, project: Project): Any {
        require(canBuild(modelName)) { "buildAll(\"$modelName\") has been called while canBeBuild is false" }

        val extension = project.extensions.getByType(EvalExtension::class.java)
        return EvalImpl(project.name, extension.prefixes.get())
    }

}