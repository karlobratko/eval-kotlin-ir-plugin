package hr.kbratko.eval

import arrow.core.Either
import arrow.core.left
import hr.kbratko.eval.interpreter.ComptimeInterpreter
import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

class EvalFunctionTransformer(
    private val messageCollector: EvalMessageCollector,
    private val evalConfig: EvalConfig
) : IrElementTransformerVoid() {
    override fun visitCall(call: IrCall): IrExpression {
        val function = call.symbol.owner

        if (function.hasAnyOfAnnotations(evalConfig.annotations) || function.nameHasAnyOfPrefixes(evalConfig.prefixes)) {
            messageCollector.reportInfo("Located function ${function.name.asString()}, starting comptime evaluation")

            val evaluatedResult = tryEvaluateCall(call)

            evaluatedResult
                .onRight { return it.toIrConst(function.returnType, call.startOffset, call.endOffset) }
                .onLeft {
                    when (it) {
                        is ComptimeUnexpectedError -> messageCollector.reportWarn("Error while evaluating function ${function.name.asString()}, ${it.description}")
                        is ComptimeProducedError -> messageCollector.reportInfo("Comptime evaluation failed for function ${function.name.asString()}, ${it.description}")
                    }
                }
        }

        return super.visitCall(call)
    }

    private fun tryEvaluateCall(call: IrCall): Either<ComptimeError, ConstantValue<*>> {
        val function = call.symbol.owner

        if (!call.returnTypeIsPrimitive()) {
            return ReturnTypeNotPrimitive(call).left()
        }

        if (!function.allParameterTypesArePrimitive()) {
            return ArgumentTypesNotPrimitive(call).left()
        }
        val parameters = function.valueParameters

        val arguments = call.getAllConstArguments()
        if (arguments.size != parameters.size) {
            return ArgumentsNotConstants(call).left()
        }
        val body = function.body
        if (body == null) {
            return FunctionBodyNotPresent(function).left()
        }

        val comptimeInterpreter = ComptimeInterpreter(parameters.zip(arguments).toMap(), body)
        return comptimeInterpreter.interpret()
    }
}