package hr.kbratko.eval.gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

internal val Project.evalExtension: EvalExtension get() = extensions.getByType(EvalExtension::class.java)

internal fun Project.createEvalExtension(body: EvalExtension.() -> Unit = {}) =
    extensions.create("eval", EvalExtension::class.java, project.objects).apply(body)

internal fun AnnotationOption(annotation: String) = SubpluginOption(key = "annotation", value = annotation)

internal fun PrefixOption(prefix: String) = SubpluginOption(key = "prefix", value = prefix)