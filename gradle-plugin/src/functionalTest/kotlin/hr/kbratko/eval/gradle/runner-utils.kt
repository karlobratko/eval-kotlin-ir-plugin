package hr.kbratko.eval.gradle

import io.kotest.matchers.string.shouldContain
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File

fun gradleRunner(projectDir: File) = GradleRunner.create()
    .withProjectDir(projectDir)
    .withPluginClasspath()
    .forwardOutput()

fun GradleRunner.executeCheckEvalExtensionTask() = apply {
    withArguments("checkEvalExtension")
}

infix fun BuildResult.outputShouldContain(expected: String) {
    output shouldContain expected
}

infix fun BuildResult.outputShouldContainAnnotations(expected: String) {
    this outputShouldContain "Annotations: $expected"
}

infix fun BuildResult.outputShouldContainPrefixes(expected: String) {
    output shouldContain "Prefixes: $expected"
}