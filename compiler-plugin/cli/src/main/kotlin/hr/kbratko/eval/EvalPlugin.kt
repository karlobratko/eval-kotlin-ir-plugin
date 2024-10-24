package hr.kbratko.eval

import com.google.auto.service.AutoService
import hr.kbratko.eval.EvalConfigurationKeys.ANNOTATION
import hr.kbratko.eval.EvalConfigurationKeys.PREFIX
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CliOptionProcessingException
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar.ExtensionStorage
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

object EvalConfigurationKeys {
    val ANNOTATION: CompilerConfigurationKey<List<String>> = CompilerConfigurationKey.create("annotation")

    val PREFIX: CompilerConfigurationKey<List<String>> = CompilerConfigurationKey.create("prefix")
}

@OptIn(ExperimentalCompilerApi::class)
@AutoService(CommandLineProcessor::class)
class EvalCommandLineProcessor : CommandLineProcessor {
    companion object {
        val ANNOTATION_OPTION = CliOption(
            "annotation", "<fqname>", "Annotation qualified names",
            required = false, allowMultipleOccurrences = true
        )

        val PREFIX_OPTION = CliOption(
            "prefix", "<prefix>", "Prefix name",
            required = false, allowMultipleOccurrences = true
        )
    }

    override val pluginId = "hr.kbratko.eval"

    override val pluginOptions = listOf(ANNOTATION_OPTION, PREFIX_OPTION)

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) =
        when (option) {
            ANNOTATION_OPTION -> configuration.appendList(ANNOTATION, value)
            PREFIX_OPTION -> configuration.appendList(PREFIX, value)
            else -> throw CliOptionProcessingException("Unknown option: ${option.optionName}")
        }
}

@OptIn(ExperimentalCompilerApi::class)
@AutoService(CompilerPluginRegistrar::class)
class EvalComponentRegistrar : CompilerPluginRegistrar() {
    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val messageCollector = configuration.get(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)
        val annotations = configuration.get(ANNOTATION)?.toMutableList() ?: emptyList()

        // TODO:  remove test defaults after development
        val prefixes = configuration.get(PREFIX)?.toMutableList() ?: listOf("eval")

        if (annotations.isEmpty() && prefixes.isEmpty()) return

        IrGenerationExtension.registerExtension(EvalIrGenerationExtension(messageCollector, annotations, prefixes))
    }

    override val supportsK2: Boolean
        get() = true
}
