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
import org.jetbrains.kotlin.ir.expressions.IrDoWhileLoop
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrLoop
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
                is IrBlock -> handleBlock(element)
                is IrVariable -> handleVariable(element)
                is IrSetValue -> handleSetValue(element)
                is IrGetValue -> handleGetValue(element)
                is IrConst<*> -> handleConst(element)
                is IrLoop -> handleLoop(element)
                is IrWhen -> handleWhen(element)
                is IrCall -> handleCall(element)
                is IrReturn -> handleReturn(element)
                else -> handleDefault(element)
            }
        }
    }

    private fun EvaluationScope.handleDefault(element: IrElement) {
        element.acceptChildrenVoid(this@ComptimeEvaluator)
        if (isControl) return
    }

    private fun EvaluationScope.handleReturn(element: IrReturn) {
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

    private fun EvaluationScope.handleWhen(element: IrWhen) {
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

    private fun EvaluationScope.handleLoop(element: IrLoop) {
        when (element) {
            is IrWhileLoop -> handleWhileLoop(element)
            is IrDoWhileLoop -> handleDoWhileLoop(element)
        }
    }

    private fun EvaluationScope.handleWhileLoop(element: IrWhileLoop) {
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
            } else break
        }
    }

    private fun EvaluationScope.handleDoWhileLoop(element: IrDoWhileLoop) {
        do {
            element.body?.let { body ->
                body.acceptVoid(this@ComptimeEvaluator)
                if (isControl) return
            }

            element.condition.acceptVoid(this@ComptimeEvaluator)
            if (isControl) return

            val outcome = childOutcomes[element.condition]
            if (outcome !is Outcome.Value ||
                outcome.value !is BooleanValue ||
                outcome.value.value == false
            ) break
        } while (true)
    }

    private fun EvaluationScope.handleConst(element: IrConst<*>) {
        setValue(element.toConstantValue())
    }

    private fun EvaluationScope.handleGetValue(element: IrGetValue) {
        val value = scopeStack[element.symbol.owner]
        if (value != null) {
            setValue(value)
        } else {
            fail(VariableNotDefined)
            return
        }
    }

    private fun EvaluationScope.handleSetValue(element: IrSetValue) {
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

    private fun EvaluationScope.handleBlock(element: IrBlock) {
        scopeStack.useScope(DeclarationScope(element)) {
            element.acceptChildrenVoid(this@ComptimeEvaluator)
            if (isControl) return

            updateOutcome(lastChildOutcome)
        }
    }

    private fun EvaluationScope.handleVariable(element: IrVariable) {
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

    private fun EvaluationScope.handleCall(call: IrCall) {
        call.acceptChildrenVoid(this@ComptimeEvaluator)
        if (isControl) return

        val function = call.symbol.owner

        val arguments = mutableListOf<ConstantValue<*>>()

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