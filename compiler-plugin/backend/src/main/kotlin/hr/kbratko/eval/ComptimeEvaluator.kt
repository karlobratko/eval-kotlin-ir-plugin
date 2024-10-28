package hr.kbratko.eval

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import hr.kbratko.eval.stack.DeclarationScope
import hr.kbratko.eval.stack.DeclarationStack
import hr.kbratko.eval.stack.EvaluationScope
import hr.kbratko.eval.stack.EvaluationStack
import hr.kbratko.eval.stack.Outcome
import org.jetbrains.kotlin.constant.BooleanValue
import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.expressions.IrSetValue
import org.jetbrains.kotlin.ir.expressions.IrWhen
import org.jetbrains.kotlin.ir.expressions.IrWhileLoop
import org.jetbrains.kotlin.ir.util.explicitParametersCount
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid

class ComptimeEvaluator(call: IrCall) : IrElementVisitorVoid {
    private val scopeStack: DeclarationStack
    private val evaluationStack = EvaluationStack()

    init {
        val function = call.symbol.owner
        val parameters = function.valueParameters
        val arguments = call.valueArguments.filterIsInstance<IrConst<*>>().map { it.toConstantValue() }
        scopeStack = DeclarationStack(
            baseScope = DeclarationScope(
                element = call,
                initVariables = parameters.zip(arguments).toMap()
            )
        )
    }

    val result: Either<EvalError, ConstantValue<*>>
        get() = evaluationStack.root.outcome.let {
            when (it) {
                is Outcome.Value -> it.value.right()
                Outcome.Empty -> ChildElementResultNotPresent.left()
                is Outcome.Control.Error -> it.error.left()
                is Outcome.Control.Return -> it.value.right()
            }
        }

    override fun visitElement(element: IrElement) {
        evaluationStack.useScope(EvaluationScope(element)) {
            when (element) {
                is IrBlock -> {
                    scopeStack.useScope(DeclarationScope(element)) {
                        element.acceptChildrenVoid(this@ComptimeEvaluator)
                        if (isControl) return

                        updateOutcome(lastChildOutcome)
                    }
                }

                is IrVariable -> {
                    val initializer = element.initializer
                    if (initializer != null) {
                        initializer.acceptVoid(this@ComptimeEvaluator)

                        scopeStack.declare(
                            name = element,
                            value = childOutcomes[initializer].let {
                                when (it) {
                                    is Outcome.Value -> it.value

                                    Outcome.Empty -> {
                                        fail(ChildElementResultNotPresent)
                                        return
                                    }

                                    else -> return
                                }
                            }
                        )
                    } else {
                        fail(UninitializedVariable)
                        return
                    }
                }

                is IrSetValue -> {
                    element.value.acceptVoid(this@ComptimeEvaluator)

                    scopeStack.write(
                        name = element.symbol.owner,
                        childOutcomes[element.value].let {
                            when (it) {
                                is Outcome.Value -> it.value

                                Outcome.Empty -> {
                                    fail(ChildElementResultNotPresent)
                                    return
                                }

                                else -> return
                            }
                        }
                    )
                }

                is IrGetValue -> {
                    val value = scopeStack[element.symbol.owner]
                    if (value != null) {
                        setValue(value)
                    } else {
                        fail(VariableNotDefined)
                        return
                    }
                }

                is IrConst<*> -> {
                    setValue(element.toConstantValue())
                }

                is IrWhileLoop -> {
                    var counter = 0
                    while (true) {
                        element.condition.acceptVoid(this@ComptimeEvaluator)
                        if (isControl) return

                        val outcome = childOutcomes[element.condition]
                        if (outcome is Outcome.Value &&
                            outcome.value is BooleanValue &&
                            outcome.value.value == true
                        ) {
                            element.body?.let { body ->
                                body.acceptVoid(this@ComptimeEvaluator)
                                if (isControl) return
                            }

                            println(counter)
                            counter++
                        } else break
                    }
                }

                is IrWhen -> {
                    element.branches.forEach { branch ->
                        branch.condition.let { condition ->
                            condition.acceptVoid(this@ComptimeEvaluator)
                            if (isControl) return

                            val outcome = childOutcomes[condition]
                            if (outcome is Outcome.Value &&
                                outcome.value is BooleanValue &&
                                outcome.value.value == true
                            ) {
                                branch.result.let { body ->
                                    body.acceptVoid(this@ComptimeEvaluator)
                                    if (isControl) return

                                    // TODO: value should be present, maybe we could enforce with error(...)
                                    val result = childOutcomes[body]
                                    if (result != null) {
                                        when (result) {
                                            is Outcome.Value -> {
                                                setValue(result.value)
                                                return
                                            }

                                            else -> return
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                is IrCall -> {
                    element.acceptChildrenVoid(this@ComptimeEvaluator)
                    if (isControl) return

                    handleCall(element)
                }

                is IrReturn -> {
                    val valueExpression = element.value
                    valueExpression.acceptVoid(this@ComptimeEvaluator)

                    childOutcomes[valueExpression].let {
                        when (it) {
                            is Outcome.Value -> {
                                setReturn(it.value)
                                return
                            }

                            Outcome.Empty -> {
                                fail(ChildElementResultNotPresent)
                                return
                            }

                            else -> return
                        }
                    }
                }

                else -> {
                    element.acceptChildrenVoid(this@ComptimeEvaluator)
                    if (isControl) return
                }
            }
        }
    }

    fun handleCall(call: IrCall) {
        val function = call.symbol.owner

        val arguments = mutableListOf<ConstantValue<*>>()

        evaluationStack.peek()?.apply {
            val dispatchReceiver = call.dispatchReceiver
            if (dispatchReceiver != null) {
                val receiverOutcome = dispatchReceiver.let { childOutcomes[it] }

                if (receiverOutcome != null) {
                    val receiver = when (receiverOutcome) {
                        is Outcome.Value -> receiverOutcome.value

                        Outcome.Empty -> {
                            fail(ChildElementResultNotPresent)
                            return
                        }

                        else -> error("Should not happen!")
                    }
                    arguments.add(receiver)
                }
            }

            val notNullArguments = call.valueArguments.filterNotNull()
            if (notNullArguments.size != call.valueArguments.size) {
                fail(ArgumentsCouldNotBeEvaluated)
                return
            }

            val argumentOutcomes = notNullArguments.map { childOutcomes[it] }.filterNotNull()
            if (argumentOutcomes.size != call.valueArguments.size) {
                fail(ArgumentsCouldNotBeEvaluated)
                return
            }

            arguments.addAll(argumentOutcomes
                .map { argument ->
                    when (argument) {
                        is Outcome.Value -> argument.value

                        Outcome.Empty -> {
                            fail(ArgumentsCouldNotBeEvaluated)
                            return
                        }

                        else -> error("Should not happen!")
                    }
                }
            )

            if (arguments.isEmpty()) {
                fail(InsufficientNumberOfArguments)
                return
            }

            if (arguments.size != function.explicitParametersCount) {
                fail(InsufficientNumberOfArguments)
                return
            }

            val operation = DefaultComptimeFunctionRegistry.findOperation(function.name.asString(), arguments)
            if (operation == null) {
                fail(UnsupportedMethod)
                return
            }

            val result = operation(arguments)
            when (result) {
                is Either.Left -> {
                    fail(result.value)
                    return
                }

                is Either.Right -> {
                    setValue(result.value)
                }
            }
        }
    }
}