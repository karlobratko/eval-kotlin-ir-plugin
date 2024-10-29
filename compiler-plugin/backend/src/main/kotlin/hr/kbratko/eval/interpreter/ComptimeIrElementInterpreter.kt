package hr.kbratko.eval.interpreter

import arrow.core.Either
import hr.kbratko.eval.ChildElementResultNotPresent
import hr.kbratko.eval.InsufficientNumberOfEvaluatedArguments
import hr.kbratko.eval.UninitializedVariable
import hr.kbratko.eval.UnsupportedElementType
import hr.kbratko.eval.UnsupportedMethod
import hr.kbratko.eval.ValueArgumentsCouldNotBeEvaluated
import hr.kbratko.eval.VariableNotDeclared
import hr.kbratko.eval.interpreter.ComptimeOutcome.Control.Error
import hr.kbratko.eval.interpreter.ComptimeOutcome.Control.Return
import hr.kbratko.eval.interpreter.ComptimeOutcome.Empty
import hr.kbratko.eval.interpreter.ComptimeOutcome.Value
import hr.kbratko.eval.interpreter.stack.DeclarationStack
import hr.kbratko.eval.toConstantValue
import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrDoWhileLoop
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrLoop
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.expressions.IrSetValue
import org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall
import org.jetbrains.kotlin.ir.expressions.IrWhen
import org.jetbrains.kotlin.ir.expressions.IrWhileLoop
import org.jetbrains.kotlin.ir.util.explicitParametersCount

data class ComptimeInterpreterContext(
    val scopeStack: DeclarationStack
)

// TODO: support break[@symbol], continue[@symbol], and return@symbol
sealed interface ComptimeIrElementInterpreter<E : IrElement> {
    val element: E

    fun interpret(context: ComptimeInterpreterContext): ComptimeOutcome<*>
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
        else -> ComptimeDefaultInterpreter(this)
    } as ComptimeIrElementInterpreter<E>

fun IrElement.interpret(context: ComptimeInterpreterContext) = toInterpreter().interpret(context)

class ComptimeDefaultInterpreter(
    override val element: IrElement,
) : ComptimeIrElementInterpreter<IrElement> {

    override fun interpret(context: ComptimeInterpreterContext) = Error(UnsupportedElementType(element))

}

class ComptimeIrReturnInterpreter(
    override val element: IrReturn
) : ComptimeIrElementInterpreter<IrReturn> {

    override fun interpret(context: ComptimeInterpreterContext): ComptimeOutcome<*> {
        val outcome = element.value.interpret(context)

        return when (outcome) {
            is Value -> Return(outcome.value)
            Empty -> Error(ChildElementResultNotPresent(element, element.value))
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

    override fun interpret(context: ComptimeInterpreterContext): ComptimeOutcome<*> {
        val function = element.symbol.owner

        val arguments = mutableListOf<ConstantValue<*>>()

        val dispatchReceiver = element.dispatchReceiver
        if (dispatchReceiver != null) {
            val receiverOutcome = dispatchReceiver.interpret(context)

            arguments.add(
                when (receiverOutcome) {
                    is Value -> receiverOutcome.value
                    Empty -> return Error(ChildElementResultNotPresent(element, dispatchReceiver))
                    else -> return receiverOutcome
                }
            )
        }

        val valueArguments = element.valueArguments.filterNotNull()
        if (valueArguments.size != element.valueArguments.size) {
            return Error(ValueArgumentsCouldNotBeEvaluated(element))
        }

        valueArguments.forEach { argument ->
            val argumentOutcome = argument.interpret(context)

            arguments.add(
                when (argumentOutcome) {
                    is Value -> argumentOutcome.value
                    Empty -> return Error(ChildElementResultNotPresent(element, argument))
                    else -> return argumentOutcome
                }
            )
        }

        // TODO: add support for other eval function invocation
        if (arguments.isEmpty() || arguments.size != function.explicitParametersCount) {
            return Error(InsufficientNumberOfEvaluatedArguments(element))
        }

        val operation = DefaultComptimeFunctionRegistry.findOperation(function.name.asString(), arguments)
        if (operation == null) {
            return Error(UnsupportedMethod(element, function.name.asString()))
        }

        val result = operation(arguments)
        return when (result) {
            is Either.Left -> Error(result.value)
            is Either.Right -> Value(result.value)
        }
    }

}

class ComptimeIrWhenInterpreter(
    override val element: IrWhen
) : ComptimeIrElementInterpreter<IrWhen> {

    override fun interpret(context: ComptimeInterpreterContext): ComptimeOutcome<*> {
        element.branches.forEach { branch ->
            branch.condition.let { condition ->
                val conditionOutput = condition.interpret(context)
                if (conditionOutput.isControl) return conditionOutput

                if (conditionOutput.isBooleanTrueValue()) {
                    branch.result.let { body ->
                        val bodyOutput = body.interpret(context)
                        return bodyOutput
                    }
                }
            }
        }
        return Empty
    }

}

class ComptimeIrLoopInterpreter(
    override val element: IrLoop
) : ComptimeIrElementInterpreter<IrLoop> {

    override fun interpret(context: ComptimeInterpreterContext): ComptimeOutcome<*> =
        when (element) {
            is IrWhileLoop -> handleWhileLoop(context)
            is IrDoWhileLoop -> handleDoWhileLoop(context)
            else -> error("Unsupported IrLoop type")
        }

    private fun handleWhileLoop(context: ComptimeInterpreterContext): ComptimeOutcome<*> {
        while (true) {
            val conditionOutcome = element.condition.interpret(context)
            if (conditionOutcome.isControl) return conditionOutcome

            if (conditionOutcome.isBooleanFalseValue()) break

            element.body?.let { body ->
                val bodyOutcome = body.interpret(context)
                if (bodyOutcome.isControl) return bodyOutcome
            }
        }
        return Empty
    }

    private fun handleDoWhileLoop(context: ComptimeInterpreterContext): ComptimeOutcome<*> {
        do {
            element.body?.let { body ->
                val bodyOutcome = body.interpret(context)
                if (bodyOutcome.isControl) return bodyOutcome
            }

            val conditionOutcome = element.condition.interpret(context)
            if (conditionOutcome.isControl) return conditionOutcome

            if (conditionOutcome.isBooleanTrueValue()) break
        } while (true)
        return Empty
    }

}

class ComptimeIrConstInterpreter(
    override val element: IrConst<*>
) : ComptimeIrElementInterpreter<IrConst<*>> {

    override fun interpret(context: ComptimeInterpreterContext) =
        Value(element.toConstantValue())

}

class ComptimeIrGetValueInterpreter(
    override val element: IrGetValue
) : ComptimeIrElementInterpreter<IrGetValue> {

    override fun interpret(context: ComptimeInterpreterContext) =
        context.scopeStack[element.symbol.owner]
            ?.let { Value(it) }
            ?: Error(VariableNotDeclared(element, element.symbol.owner))

}

class ComptimeIrSetValueInterpreter(
    override val element: IrSetValue
) : ComptimeIrElementInterpreter<IrSetValue> {

    override fun interpret(context: ComptimeInterpreterContext): ComptimeOutcome<*> {
        val outcome = element.value.interpret(context)

        return when (outcome) {
            is Value -> {
                context.scopeStack.write(
                    name = element.symbol.owner,
                    value = outcome.value
                )

                Empty
            }

            Empty -> Error(ChildElementResultNotPresent(element, element.value))

            else -> outcome
        }
    }

}

class ComptimeIrVariableInterpreter(
    override val element: IrVariable
) : ComptimeIrElementInterpreter<IrVariable> {

    override fun interpret(context: ComptimeInterpreterContext): ComptimeOutcome<*> {
        val initializer = element.initializer
        if (initializer != null) {
            val outcome = initializer.interpret(context)

            return when (outcome) {
                is Value -> {
                    context.scopeStack.declare(
                        name = element,
                        value = outcome.value
                    )

                    Empty
                }

                Empty -> Error(ChildElementResultNotPresent(element, initializer))

                else -> outcome
            }
        } else {
            return Error(UninitializedVariable(element))
        }
    }

}

class ComptimeIrBlockBodyInterpreter(
    override val element: IrBlockBody
) : ComptimeIrElementInterpreter<IrBlockBody> {

    override fun interpret(context: ComptimeInterpreterContext): ComptimeOutcome<*> {
        context.scopeStack.scope {
            this@ComptimeIrBlockBodyInterpreter.element.statements.forEach {
                val outcome = it.interpret(context)
                if (outcome.isControl) return outcome
            }
        }
        return Empty
    }

}

class ComptimeIrBlockInterpreter(
    override val element: IrBlock
) : ComptimeIrElementInterpreter<IrBlock> {

    override fun interpret(context: ComptimeInterpreterContext): ComptimeOutcome<*> {
        val outcomes = mutableListOf<ComptimeOutcome<*>>()
        context.scopeStack.scope {
            this@ComptimeIrBlockInterpreter.element.statements.forEach {
                val outcome = it.interpret(context)
                if (outcome.isControl) return outcome

                outcomes.add(outcome)
            }
        }
        return outcomes.lastOrNull() ?: Empty
    }

}