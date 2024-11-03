package hr.kbratko.eval

import com.google.auto.service.AutoService
import hr.kbratko.eval.EvalConfigurationKeys.PREFIX
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY
import org.jetbrains.kotlin.cli.common.messages.MessageCollector.Companion.NONE
import org.jetbrains.kotlin.compiler.plugin.*
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

object EvalConfigurationKeys {
    val PREFIX: CompilerConfigurationKey<List<String>> = CompilerConfigurationKey.create("prefix")
}

@OptIn(ExperimentalCompilerApi::class)
@AutoService(CommandLineProcessor::class)
class EvalCommandLineProcessor : CommandLineProcessor {
    companion object {
        val PREFIX_OPTION = CliOption(
            "prefix", "<prefix>", "Prefix name",
            required = false, allowMultipleOccurrences = true
        )
    }

    override val pluginId = "hr.kbratko.eval"

    override val pluginOptions = listOf(PREFIX_OPTION)

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) =
        when (option) {
            PREFIX_OPTION -> configuration.appendList(PREFIX, value)
            else -> throw CliOptionProcessingException("Unknown option: ${option.optionName}")
        }
}

@OptIn(ExperimentalCompilerApi::class)
@AutoService(CompilerPluginRegistrar::class)
class EvalComponentRegistrar(private val defaultPrefixes: List<String> = emptyList()) : CompilerPluginRegistrar() {

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val messageCollector = configuration.get(MESSAGE_COLLECTOR_KEY, NONE)
        val prefixes = configuration.get(PREFIX)?.toMutableList() ?: defaultPrefixes

        if (prefixes.isEmpty()) return

        IrGenerationExtension.registerExtension(EvalIrGenerationExtension(messageCollector, prefixes))
    }

    override val supportsK2: Boolean
        get() = true
}
