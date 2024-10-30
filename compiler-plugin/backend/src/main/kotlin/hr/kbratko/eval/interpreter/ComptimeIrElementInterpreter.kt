package hr.kbratko.eval.interpreter

import arrow.core.Either
import hr.kbratko.eval.ChildElementResultNotPresent
import hr.kbratko.eval.InsufficientNumberOfEvaluatedArguments
import hr.kbratko.eval.UninitializedVariable
import hr.kbratko.eval.UnsupportedElementType
import hr.kbratko.eval.UnsupportedMethod
import hr.kbratko.eval.ValueArgumentsCouldNotBeEvaluated
import hr.kbratko.eval.VariableNotDeclared
import hr.kbratko.eval.interpreter.EvaluationOutcome.ControlFlow
import hr.kbratko.eval.interpreter.EvaluationOutcome.ControlFlow.EvaluationError
import hr.kbratko.eval.interpreter.EvaluationOutcome.ControlFlow.LoopControl.Break
import hr.kbratko.eval.interpreter.EvaluationOutcome.ControlFlow.LoopControl.Continue
import hr.kbratko.eval.interpreter.EvaluationOutcome.ControlFlow.Return
import hr.kbratko.eval.interpreter.EvaluationOutcome.EvaluationResult.ConstantResult
import hr.kbratko.eval.interpreter.EvaluationOutcome.EvaluationResult.NoResult
import hr.kbratko.eval.interpreter.stack.DeclarationStack
import hr.kbratko.eval.toConstantValue
import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrBreak
import org.jetbrains.kotlin.ir.expressions.IrBreakContinue
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrContinue
import org.jetbrains.kotlin.ir.expressions.IrDoWhileLoop
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrLoop
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.expressions.IrSetValue
import org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall
import org.jetbrains.kotlin.ir.expressions.IrWhen
import org.jetbrains.kotlin.ir.expressions.IrWhileLoop
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.isPrimitiveType
import org.jetbrains.kotlin.ir.types.isString
import org.jetbrains.kotlin.ir.util.explicitParametersCount

data class ComptimeInterpreterContext(
    val scopeStack: DeclarationStack
)

sealed interface ComptimeIrElementInterpreter<E : IrElement> {
    val element: E

    fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome<*>
}

@Suppress("UNCHECKED_CAST")
fun <E : IrElement> E.toInterpreter(): ComptimeIrElementInterpreter<E> =
    when (this) {
        is IrBlock -> ComptimeIrBlockInterpreter(this)
        is IrBlockBody -> ComptimeIrBlockBodyInterpreter(this)
        is IrVariable -> ComptimeIrVariableInterpreter(this)
        is IrSetValue -> ComptimeIrSetValueInterpreter(this)
        is IrGetValue -> ComptimeIrGetValueInterpreter(this)
        is IrConst<*> -> ComptimeIrConstInterpreter(this)
        is IrLoop -> ComptimeIrLoopInterpreter(this)
        is IrWhen -> ComptimeIrWhenInterpreter(this)
        is IrCall -> ComptimeIrCallInterpreter(this)
        is IrTypeOperatorCall -> ComptimeIrTypeOperatorCallInterpreter(this)
        is IrReturn -> ComptimeIrReturnInterpreter(this)
        is IrBreakContinue -> ComptimeIrBreakContinueInterpreter(this)
        else -> ComptimeDefaultInterpreter(this)
    } as ComptimeIrElementInterpreter<E>

fun IrElement.interpret(context: ComptimeInterpreterContext) = toInterpreter().interpret(context)

class ComptimeDefaultInterpreter(
    override val element: IrElement,
) : ComptimeIrElementInterpreter<IrElement> {

    override fun interpret(context: ComptimeInterpreterContext) = EvaluationError(UnsupportedElementType(element))

}

class ComptimeIrBreakContinueInterpreter(
    override val element: IrBreakContinue
) : ComptimeIrElementInterpreter<IrBreakContinue> {

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome<*> {
        return when (element) {
            is IrBreak -> Break(element.loop, element.label)
            is IrContinue -> Continue(element.loop, element.label)
            else -> NoResult
        }
    }

}

class ComptimeIrReturnInterpreter(
    override val element: IrReturn
) : ComptimeIrElementInterpreter<IrReturn> {

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome<*> {
        val outcome = element.value.interpret(context)

        return when (outcome) {
            is ConstantResult -> Return(outcome.value, element.returnTargetSymbol)
            NoResult -> EvaluationError(ChildElementResultNotPresent(element, element.value))
            else -> outcome
        }
    }

}

class ComptimeIrTypeOperatorCallInterpreter(
    override val element: IrTypeOperatorCall
) : ComptimeIrElementInterpreter<IrTypeOperatorCall> {

    override fun interpret(context: ComptimeInterpreterContext) =
        element.argument.interpret(context)

}

class ComptimeIrCallInterpreter(
    override val element: IrCall
) : ComptimeIrElementInterpreter<IrCall> {

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome<*> {
        val function = element.symbol.owner

        val arguments = mutableListOf<ConstantValue<*>>()

        val dispatchReceiver = element.dispatchReceiver
        if (dispatchReceiver != null) {
            val receiverOutcome = dispatchReceiver.interpret(context)

            arguments.add(
                when (receiverOutcome) {
                    is ConstantResult -> receiverOutcome.value
                    NoResult -> return EvaluationError(ChildElementResultNotPresent(element, dispatchReceiver))
                    else -> return receiverOutcome
                }
            )
        }

        val valueArguments = element.valueArguments.filterNotNull()
        if (valueArguments.size != element.valueArguments.size) {
            return EvaluationError(ValueArgumentsCouldNotBeEvaluated(element))
        }

        valueArguments.forEach { argument ->
            val argumentOutcome = argument.interpret(context)

            arguments.add(
                when (argumentOutcome) {
                    is ConstantResult -> argumentOutcome.value
                    NoResult -> return EvaluationError(ChildElementResultNotPresent(element, argument))
                    else -> return argumentOutcome
                }
            )
        }

        // TODO: add support for other eval function invocation
        if (arguments.isEmpty() || arguments.size != function.explicitParametersCount) {
            return EvaluationError(InsufficientNumberOfEvaluatedArguments(element))
        }

        if (!function.returnType.isPrimitiveTypeOrString()) {
            return EvaluationError(UnsupportedMethod(element, function.name.asString()))
        }

        val operation = DefaultComptimeFunctionRegistry.findOperation(
            name = function.name.asString(),
            signature = ComptimeFunctionSignature(arguments.map { it::class })
        )
        if (operation == null) {
            return EvaluationError(UnsupportedMethod(element, function.name.asString()))
        }

        val result = operation(arguments)
        return when (result) {
            is Either.Left -> EvaluationError(result.value)
            is Either.Right -> ConstantResult(result.value)
        }
    }

    fun IrType.isPrimitiveTypeOrString(): Boolean = this.isPrimitiveType() || this.isString()

}

class ComptimeIrWhenInterpreter(
    override val element: IrWhen
) : ComptimeIrElementInterpreter<IrWhen> {

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome<*> {
        element.branches.forEach { branch ->
            branch.condition.let { condition ->
                val conditionOutput = condition.interpret(context)
                if (conditionOutput.isControlFlow) return conditionOutput

                if (conditionOutput.isBooleanTrueValue()) {
                    branch.result.let { body ->
                        val bodyOutput = body.interpret(context)
                        return bodyOutput
                    }
                }
            }
        }
        return NoResult
    }

}

class ComptimeIrLoopInterpreter(
    override val element: IrLoop
) : ComptimeIrElementInterpreter<IrLoop> {

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome<*> =
        when (element) {
            is IrWhileLoop -> handleWhileLoop(context)
            is IrDoWhileLoop -> handleDoWhileLoop(context)
            else -> NoResult
        }

    private fun handleWhileLoop(context: ComptimeInterpreterContext): EvaluationOutcome<*> {
        while (true) {
            val conditionOutcome = element.condition.interpret(context)
            if (conditionOutcome.isControlFlow) return conditionOutcome

            if (conditionOutcome.isBooleanFalseValue()) break

            val body = element.body
            if (body != null) {
                val bodyOutcome = body.interpret(context)

                when (bodyOutcome) {
                    is Break -> {
                        if (bodyOutcome.matches(element)) break
                        else return bodyOutcome
                    }

                    is Continue -> {
                        if (bodyOutcome.matches(element)) continue
                        else return bodyOutcome
                    }

                    is ControlFlow -> return bodyOutcome

                    else -> {}
                }
            }
        }
        return NoResult
    }

    private fun handleDoWhileLoop(context: ComptimeInterpreterContext): EvaluationOutcome<*> {
        do {
            val body = element.body
            if (body != null) {
                val bodyOutcome = body.interpret(context)

                when (bodyOutcome) {
                    is Break -> {
                        if (bodyOutcome.matches(element)) break
                        else return bodyOutcome
                    }

                    is Continue -> {
                        if (bodyOutcome.matches(element)) continue
                        else return bodyOutcome
                    }

                    is ControlFlow -> return bodyOutcome

                    else -> {}
                }
            }

            val conditionOutcome = element.condition.interpret(context)
            if (conditionOutcome.isControlFlow) return conditionOutcome

            if (conditionOutcome.isBooleanTrueValue()) break
        } while (true)
        return NoResult
    }

}

class ComptimeIrConstInterpreter(
    override val element: IrConst<*>
) : ComptimeIrElementInterpreter<IrConst<*>> {

    override fun interpret(context: ComptimeInterpreterContext) =
        ConstantResult(element.toConstantValue())

}

class ComptimeIrGetValueInterpreter(
    override val element: IrGetValue
) : ComptimeIrElementInterpreter<IrGetValue> {

    override fun interpret(context: ComptimeInterpreterContext) =
        context.scopeStack[element.symbol.owner]
            ?.let { ConstantResult(it) }
            ?: EvaluationError(VariableNotDeclared(element, element.symbol.owner))

}

class ComptimeIrSetValueInterpreter(
    override val element: IrSetValue
) : ComptimeIrElementInterpreter<IrSetValue> {

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome<*> {
        val outcome = element.value.interpret(context)

        return when (outcome) {
            is ConstantResult -> {
                context.scopeStack.write(
                    name = element.symbol.owner,
                    value = outcome.value
                )

                NoResult
            }

            NoResult -> EvaluationError(ChildElementResultNotPresent(element, element.value))

            else -> outcome
        }
    }

}

class ComptimeIrVariableInterpreter(
    override val element: IrVariable
) : ComptimeIrElementInterpreter<IrVariable> {

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome<*> {
        val initializer = element.initializer
        if (initializer != null) {
            val outcome = initializer.interpret(context)

            return when (outcome) {
                is ConstantResult -> {
                    context.scopeStack.declare(
                        name = element,
                        value = outcome.value
                    )

                    NoResult
                }

                NoResult -> EvaluationError(ChildElementResultNotPresent(element, initializer))

                else -> outcome
            }
        } else {
            return EvaluationError(UninitializedVariable(element))
        }
    }

}

class ComptimeIrBlockBodyInterpreter(
    override val element: IrBlockBody
) : ComptimeIrElementInterpreter<IrBlockBody> {

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome<*> {
        context.scopeStack.scope {
            this@ComptimeIrBlockBodyInterpreter.element.statements.forEach {
                val outcome = it.interpret(context)
                if (outcome.isControlFlow) return outcome
            }
        }
        return NoResult
    }

}

class ComptimeIrBlockInterpreter(
    override val element: IrBlock
) : ComptimeIrElementInterpreter<IrBlock> {

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome<*> {
        val outcomes = mutableListOf<EvaluationOutcome<*>>()
        context.scopeStack.scope {
            this@ComptimeIrBlockInterpreter.element.statements.forEach {
                val outcome = it.interpret(context)
                if (outcome.isControlFlow) return outcome

                outcomes.add(outcome)
            }
        }
        return outcomes.lastOrNull() ?: NoResult
    }

}