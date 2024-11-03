package hr.kbratko.eval

import arrow.core.Either
import arrow.core.left
import hr.kbratko.eval.interpreter.executeInComptime
import hr.kbratko.eval.types.*
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

class EvalFunctionTransformer(
    private val messageCollector: EvalMessageCollector,
    private val evalConfig: EvalConfig
) : IrElementTransformerVoid() {
    override fun visitCall(expression: IrCall): IrExpression {
        val function = expression.symbol.owner

        if (function.nameHasAnyOfPrefixes(evalConfig.prefixes)) {
            messageCollector.reportInfo("Located function ${function.name.asString()}, starting comptime evaluation")

            val evaluatedResult = tryEvaluateCall(expression)

            evaluatedResult
                .onRight { return it.toIrConst(function.returnType, expression.startOffset, expression.endOffset) }
                .onLeft {
                    when (it) {
                        is ComptimeUnexpectedError -> messageCollector.reportWarn("Error while evaluating function ${function.name.asString()}, ${it.description}")
                        is ComptimeProducedError -> messageCollector.reportInfo("Comptime evaluation failed for function ${function.name.asString()}, ${it.description}")
                    }
                }
        }

        return super.visitCall(expression)
    }

    private fun tryEvaluateCall(call: IrCall): Either<ComptimeError, ComptimeConstant> {
        val function = call.symbol.owner

        if (!call.returnTypeIsComptimeConstant()) return ReturnTypeNotPrimitive(call).left()

        if (!function.allParameterTypesAreComptimeConstants()) return ArgumentTypesNotPrimitive(call).left()
        val parameters = function.valueParameters

        val arguments = call.getAllConstArguments()
        if (arguments.size != parameters.size) return ArgumentsNotConstants(call).left()

        return function.executeInComptime(parameters.zip(arguments).toMap())
    }
}
