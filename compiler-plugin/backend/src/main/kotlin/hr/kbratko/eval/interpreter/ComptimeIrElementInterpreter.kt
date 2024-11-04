package hr.kbratko.eval.interpreter

import arrow.core.Either
import hr.kbratko.eval.interpreter.EvaluationOutcome.ControlFlow
import hr.kbratko.eval.interpreter.EvaluationOutcome.ControlFlow.EvaluationError
import hr.kbratko.eval.interpreter.EvaluationOutcome.ControlFlow.LoopControl.Break
import hr.kbratko.eval.interpreter.EvaluationOutcome.ControlFlow.LoopControl.Continue
import hr.kbratko.eval.interpreter.EvaluationOutcome.ControlFlow.Return
import hr.kbratko.eval.interpreter.EvaluationOutcome.EvaluationResult.ConstantResult
import hr.kbratko.eval.interpreter.EvaluationOutcome.EvaluationResult.NoResult
import hr.kbratko.eval.interpreter.functions.DefaultComptimeFunctionRegistry
import hr.kbratko.eval.interpreter.stack.DeclarationStack
import hr.kbratko.eval.isComptimeConstant
import hr.kbratko.eval.toComptimeConstant
import hr.kbratko.eval.types.*
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.util.explicitParametersCount
import java.lang.System.currentTimeMillis
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class ComptimeInterpreterContext(
    val scopeStack: DeclarationStack
)

sealed interface ComptimeIrElementInterpreter<E : IrElement> {
    val element: E

    fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome
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
        is IrComposite -> ComptimeIrCompositeInterpreter(this)
        is IrReturn -> ComptimeIrReturnInterpreter(this)
        is IrBreakContinue -> ComptimeIrBreakContinueInterpreter(this)
        is IrStringConcatenation -> ComptimeIrStringConcatenationInterpreter(this)
        else -> ComptimeDefaultInterpreter(this)
    } as ComptimeIrElementInterpreter<E>

fun IrElement.interpret(context: ComptimeInterpreterContext) = toInterpreter().interpret(context)

class ComptimeDefaultInterpreter(
    override val element: IrElement,
) : ComptimeIrElementInterpreter<IrElement> {

    override fun interpret(context: ComptimeInterpreterContext) = EvaluationError(UnsupportedElementType(element))

}

class ComptimeIrStringConcatenationInterpreter(
    override val element: IrStringConcatenation
) : ComptimeIrElementInterpreter<IrStringConcatenation> {

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome =
        element.arguments
            .map {
                when (val outcome = it.interpret(context)) {
                    is ConstantResult -> outcome
                    NoResult -> return EvaluationError(ChildElementResultNotPresent(element, it))
                    else -> return outcome
                }.value
            }
            .fold("") { a, b -> a + b.value }
            .let(::StringConstant)
            .let(::ConstantResult)

}

class ComptimeIrBreakContinueInterpreter(
    override val element: IrBreakContinue
) : ComptimeIrElementInterpreter<IrBreakContinue> {

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome {
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

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome {
        return when (val outcome = element.value.interpret(context)) {
            is ConstantResult -> Return(outcome.value, element.returnTargetSymbol)
            NoResult -> EvaluationError(ChildElementResultNotPresent(element, element.value))
            else -> outcome
        }
    }

}

// TODO: extend behaviour by inspecting operator and typeOperand types, do type cast
//       This will allow us to support type casting and is statements.
class ComptimeIrTypeOperatorCallInterpreter(
    override val element: IrTypeOperatorCall
) : ComptimeIrElementInterpreter<IrTypeOperatorCall> {

    override fun interpret(context: ComptimeInterpreterContext) =
        element.argument.interpret(context)

}

class ComptimeIrCompositeInterpreter(
    override val element: IrComposite
) : ComptimeIrElementInterpreter<IrComposite> {

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome {
        this.element.statements.forEach {
            val outcome = it.interpret(context)
            if (outcome.isControlFlow) return outcome
        }
        return NoResult
    }

}

class ComptimeIrCallInterpreter(
    override val element: IrCall
) : ComptimeIrElementInterpreter<IrCall> {

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome {
        val function = element.symbol.owner

        val arguments = mutableListOf<ComptimeConstant>()

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
        } else {
            val extensionReceiver = element.extensionReceiver
            if (extensionReceiver != null) {
                val receiverOutcome = extensionReceiver.interpret(context)

                arguments.add(
                    when (receiverOutcome) {
                        is ConstantResult -> receiverOutcome.value
                        NoResult -> return EvaluationError(ChildElementResultNotPresent(element, extensionReceiver))
                        else -> return receiverOutcome
                    }
                )
            }
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

        if (arguments.isEmpty() || arguments.size != function.explicitParametersCount) {
            return EvaluationError(InsufficientNumberOfEvaluatedArguments(element))
        }

        if (!function.returnType.isComptimeConstant()) {
            return EvaluationError(UnsupportedMethod(function.name.asString(), arguments))
        }

        val result = DefaultComptimeFunctionRegistry.execute(
            name = function.name.asString(),
            arguments = arguments,
        )
        return when (result) {
            is Either.Left -> EvaluationError(result.value)
            is Either.Right -> ConstantResult(result.value)
        }
    }

}

class ComptimeIrWhenInterpreter(
    override val element: IrWhen
) : ComptimeIrElementInterpreter<IrWhen> {

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome {
        element.branches.forEach { branch ->
            branch.condition.let { condition ->
                val conditionOutput = condition.interpret(context)
                if (conditionOutput.isControlFlow) return conditionOutput

                if (conditionOutput.isBooleanTrueResult()) {
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

/**
 * TODO: Complexity-Tracking Approach
 *  Introduce a general-purpose complexity tracker for ComptimeInterpreterContext that could monitor nested loop depth,
 *  recursion depth, and complexity of the code. Each iteration would add to a “complexity score,” and if it exceeds a
 *  threshold, the loop or the entire computation stops.
 */
class ComptimeIrLoopInterpreter(
    override val element: IrLoop,
    private val maxIterations: Int = 1000,
    private val maxDuration: Duration = 5.seconds
) : ComptimeIrElementInterpreter<IrLoop> {

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome =
        when (element) {
            is IrWhileLoop -> handleWhileLoop(context)
            is IrDoWhileLoop -> handleDoWhileLoop(context)
            else -> NoResult
        }

    private fun handleWhileLoop(context: ComptimeInterpreterContext): EvaluationOutcome {
        var iterations = 0
        val startTime = currentTimeMillis()

        while (true) {
            iterations++
            if (iterations > maxIterations)
                return EvaluationError(LoopIterationExceededError(element, maxIterations))

            if (currentTimeMillis() - startTime > maxDuration.inWholeMilliseconds)
                return EvaluationError(LoopTimeoutError(element, maxDuration))

            val conditionOutcome = element.condition.interpret(context)
            if (conditionOutcome.isControlFlow) return conditionOutcome

            if (conditionOutcome.isBooleanFalseResult()) break

            val body = element.body
            if (body != null) {
                when (val bodyOutcome = body.interpret(context)) {
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

    private fun handleDoWhileLoop(context: ComptimeInterpreterContext): EvaluationOutcome {
        var iterations = 0
        val startTime = currentTimeMillis()

        do {
            iterations++
            if (iterations > maxIterations)
                return EvaluationError(LoopIterationExceededError(element, maxIterations))

            if (currentTimeMillis() - startTime > maxDuration.inWholeMilliseconds)
                return EvaluationError(LoopTimeoutError(element, maxDuration))

            val body = element.body
            if (body != null) {
                when (val bodyOutcome = body.interpret(context)) {
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

            if (conditionOutcome.isBooleanTrueResult()) break
        } while (true)
        return NoResult
    }

}

class ComptimeIrConstInterpreter(
    override val element: IrConst<*>
) : ComptimeIrElementInterpreter<IrConst<*>> {

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome {
        val constant = element.toComptimeConstant()
        return if (constant != null) ConstantResult(constant)
        else EvaluationError(ValueIsNotComptimeConstant(element))
    }

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

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome {
        return when (val outcome = element.value.interpret(context)) {
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

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome {
        val initializer = element.initializer
        if (initializer != null) {
            return when (val outcome = initializer.interpret(context)) {
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

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome {
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

    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome {
        val outcomes = mutableListOf<EvaluationOutcome>()
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