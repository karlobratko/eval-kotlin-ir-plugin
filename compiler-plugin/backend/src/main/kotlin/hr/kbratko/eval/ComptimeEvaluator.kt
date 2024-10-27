package hr.kbratko.eval

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import hr.kbratko.eval.EvalResult.Success
import org.jetbrains.kotlin.constant.BooleanValue
import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.constant.IntValue
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBlock
import org.jetbrains.kotlin.ir.expressions.IrBranch
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrWhen
import org.jetbrains.kotlin.ir.util.explicitParametersCount
import org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid
import org.jetbrains.kotlin.ir.visitors.acceptVoid

class ComptimeEvaluator(call: IrCall) : IrElementVisitorVoid {
    private val scopeStack: ScopeStack
    private val evaluationGraph = EvaluationGraph()

    init {
        val function = call.symbol.owner
        val arguments = call.valueArguments.filterIsInstance<IrConst<*>>().map { it.toConstantValue() }
        val baseScope = Scope(function.valueParameters.zip(arguments.map { IntValue(it.value as Int) }).toMap())
        scopeStack = ScopeStack(baseScope)
    }

    val result: Either<EvalError, ConstantValue<*>>
        get() = evaluationGraph.root?.let {
            when (val result = it.result) {
                EvalResult.NoResult -> ResultNotPresent.left()
                is EvalResult.Failure -> result.error.left()
                is EvalResult.Success -> result.value.right()
            }
        } ?: ResultNotPresent.left()

    override fun visitElement(element: IrElement) {
        if (evaluationGraph.isFailure()) return
        evaluationGraph.push(element)
        try {
            when (element) {
                is IrBlock -> {
                    scopeStack.enterScope()
                    try {
                        element.acceptChildrenVoid(this@ComptimeEvaluator)
                        if (evaluationGraph.isFailure()) return
                    } finally {
                        scopeStack.leaveScope()
                    }
                }

                is IrVariable -> {
                    element.acceptChildrenVoid(this@ComptimeEvaluator)
                    if (evaluationGraph.isFailure()) return

                    // we can be sure that here we want last child result
                    evaluationGraph.getLastChildResult()
                        .onSuccess {
                            scopeStack[element] = it
                        }
                }

                is IrGetValue -> {
                    val owner = element.symbol.owner
                    scopeStack[owner]
                        .onNone {
                            evaluationGraph.setErrorForCurrentNode(ResultNotPresent)
                        }
                        .onSome {
                            evaluationGraph.setResultForCurrentNode(it)
                        }
                }

                is IrConst<*> -> {
                    evaluationGraph.setResultForCurrentNode(element.toConstantValue())
                }

                is IrWhen -> {
                    element.branches.forEach {
                        it.acceptVoid(this)
                        if (evaluationGraph.isFailure()) return

                        val result = evaluationGraph.getChildResult(it)
                        if (result.isSuccess()) return
                    }
                }

                is IrBranch -> {
                    element.condition.let { condition ->
                        condition.acceptVoid(this)
                        if (evaluationGraph.isFailure()) return

                        val result = evaluationGraph.getChildResult(condition)
                        if (result is Success && result.value is BooleanValue && result.value.value == true) {
                            element.result.let { result ->
                                result.acceptVoid(this)
                                if (evaluationGraph.isFailure()) return
                            }
                        }
                    }
                }

                is IrCall -> {
                    element.acceptChildrenVoid(this@ComptimeEvaluator)
                    if (evaluationGraph.isFailure()) return

                    handleCall(element)
                }

                else -> {
                    element.acceptChildrenVoid(this@ComptimeEvaluator)
                    if (evaluationGraph.isFailure()) return
                }
            }
        } finally {
            evaluationGraph.pop()
        }
    }

    fun handleCall(call: IrCall) {
        val function = call.symbol.owner

        val arguments = mutableListOf<ConstantValue<*>>()

        val dispatchReceiver = call.dispatchReceiver
        if (dispatchReceiver != null) {
            val receiverResult = dispatchReceiver.let { evaluationGraph.getChildResult(it) }
            val receiver = when (receiverResult) {
                is EvalResult.Success -> receiverResult.value

                is EvalResult.Failure -> {
                    evaluationGraph.setErrorForCurrentNode(receiverResult.error)
                    return
                }

                EvalResult.NoResult -> {
                    evaluationGraph.setErrorForCurrentNode(DispatcherCouldNotBeEvaluated)
                    return
                }
            }
            arguments.add(receiver)
        }

        val notNullArguments = call.valueArguments.filterNotNull()
        if (notNullArguments.size != call.valueArguments.size) {
            evaluationGraph.setErrorForCurrentNode(ArgumentsCouldNotBeEvaluated)
            return
        }

        arguments.addAll(notNullArguments.map { evaluationGraph.getChildResult(it) }
            .map { argument ->
                when (argument) {
                    is EvalResult.Success -> argument.value

                    is EvalResult.Failure -> {
                        evaluationGraph.setErrorForCurrentNode(argument.error)
                        return
                    }

                    EvalResult.NoResult -> {
                        evaluationGraph.setErrorForCurrentNode(ArgumentsCouldNotBeEvaluated)
                        return
                    }
                }
            }
        )

        if (arguments.isEmpty()) {
            evaluationGraph.setErrorForCurrentNode(InsufficientNumberOfArguments)
            return
        }

        if (arguments.size != function.explicitParametersCount) {
            evaluationGraph.setErrorForCurrentNode(InsufficientNumberOfArguments)
            return
        }

        // Find and invoke the appropriate operation
        val operation = DefaultComptimeFunctionRegistry.findOperation(function.name.asString(), arguments)
        if (operation == null) {
            evaluationGraph.setErrorForCurrentNode(UnsupportedMethod)
            return
        }

        val result = operation(arguments)
        when (result) {
            is EvalResult.Success -> evaluationGraph.setResultForCurrentNode(result.value)
            is EvalResult.Failure -> evaluationGraph.setErrorForCurrentNode(result.error)
            EvalResult.NoResult -> evaluationGraph.setErrorForCurrentNode(ArgumentsCouldNotBeEvaluated)
        }
    }
}