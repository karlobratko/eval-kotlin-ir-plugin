package hr.kbratko.eval

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.types.isInt
import org.jetbrains.kotlin.ir.types.isString
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.name.FqName

class EvalFunctionTransformer(
    val pluginContext: IrPluginContext,
    val messageCollector: MessageCollector,
    val annotations: List<String>,
    val prefixes: List<String>
) : IrElementTransformerVoid() {

    override fun visitCall(expression: IrCall): IrExpression {
        val function = expression.symbol.owner

        if (function.hasAnyOfAnnotations(annotations) || function.nameHasAnyOfPrefixes(prefixes)) {
            println("Found eval function: ${function.name.asString()}")

            // Try to evaluate the function call at compile time
            val evaluatedResult = tryEvaluateCall(expression)

            // If we successfully evaluated the function, return the evaluated result
            if (evaluatedResult != null) {
                return evaluatedResult
            }
        }

        // Continue with the default IR traversal if not an "eval" function
        return super.visitCall(expression)
    }

    override fun visitBlockBody(body: IrBlockBody): IrBody {
        println(body.dump())
        return super.visitBlockBody(body)
    }

    // Tries to evaluate the function call at compile time
    private fun tryEvaluateCall(expression: IrCall): IrExpression? {
        val arguments = expression.valueArguments.filterIsInstance<IrConst<*>>()

        // Only proceed if all arguments are constants
        if (arguments.size == expression.valueArgumentsCount) {
            // Evaluate the function based on its name
            return when (expression.symbol.owner.name.asString()) {
                "evalAdd" -> {
                    if (arguments.all { it.type.isInt() }) {
                        val result = arguments.sumOf { (it.value as Int) }
                        return pluginContext.createIntConst(
                            expression.startOffset,
                            expression.endOffset,
                            result
                        )
                    } else null
                }

                "evalConcat" -> {
                    if (arguments.all { it.type.isString() }) {
                        val result = arguments.joinToString("") { it.value as String }
                        return pluginContext.createStringConst(
                            expression.startOffset,
                            expression.endOffset,
                            result
                        )
                    } else null
                }
                // You can add more cases for other eval functions like evalMultiply, evalSubtract, etc.
                else -> null
            }
        }

        return null // Cannot evaluate at compile time
    }
}

private fun IrSimpleFunction.hasAnyOfAnnotations(annotations: List<String>) =
    annotations.any { hasAnnotation(FqName(it)) }

private fun IrSimpleFunction.nameHasAnyOfPrefixes(prefixes: List<String>) =
    prefixes.any { name.asString().startsWith(it) }

// Helper functions for creating IR constants
private fun IrPluginContext.createIntConst(startOffset: Int, endOffset: Int, value: Int): IrConst<Int> {
    return IrConstImpl.int(startOffset, endOffset, irBuiltIns.intType, value)
}

private fun IrPluginContext.createStringConst(startOffset: Int, endOffset: Int, value: String): IrConst<String> {
    return IrConstImpl.string(startOffset, endOffset, irBuiltIns.stringType, value)
}
