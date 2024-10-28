package hr.kbratko.eval

import arrow.core.Either
import arrow.core.left
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid

class EvalFunctionTransformer(
    private val pluginContext: IrPluginContext,
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
                .onLeft { messageCollector.reportInfo("Comptime evaluation failed for function ${function.name.asString()}, ${it.message}") }
        }

        return super.visitCall(call)
    }

    private fun tryEvaluateCall(call: IrCall): Either<EvalError, ConstantValue<*>> =
        if (call.areAllArgumentsIrConst()) tryEvaluateFunctionBody(call)
        else AllArgumentsAreNotConstants.left()

    private fun tryEvaluateFunctionBody(call: IrCall): Either<EvalError, ConstantValue<*>> {
        val function = call.symbol.owner
        val body = function.body ?: return FunctionBodyNotPresent.left()

        val comptimeEvaluator = ComptimeEvaluator(call)

        body.acceptVoid(comptimeEvaluator)
        return comptimeEvaluator.result
    }

    override fun visitBlockBody(body: IrBlockBody): IrBody {
        println(body.dump())
        return super.visitBlockBody(body)
    }
}