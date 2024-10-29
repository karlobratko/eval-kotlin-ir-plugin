package hr.kbratko.eval

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class EvalIrGenerationExtension(
    messageCollector: MessageCollector,
    private val annotations: List<String>,
    private val prefixes: List<String>,
) : IrGenerationExtension {
    private val messageCollector = EvalMessageCollector(messageCollector)

    override fun generate(
        moduleFragment: IrModuleFragment,
        pluginContext: IrPluginContext
    ) {
        messageCollector.reportInfo("Argument 'annotations' = $annotations")
        messageCollector.reportInfo("Argument 'prefixes' = $prefixes")

        moduleFragment.transformChildrenVoid(
            EvalFunctionTransformer(
                messageCollector = EvalMessageCollector(messageCollector),
                evalConfig = EvalConfig(
                    annotations = annotations,
                    prefixes = prefixes
                ),
            )
        )

        messageCollector.reportInfo(moduleFragment.dump())
    }
}