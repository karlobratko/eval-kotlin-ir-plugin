package hr.kbratko.eval

import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.ir.IrElement

sealed class EvalResult<out T> {
    data class Success<T>(val value: T) : EvalResult<T>()
    data class Failure(val error: EvalError) : EvalResult<Nothing>()
    object NoResult : EvalResult<Nothing>()

    // Helper methods for checking the result type
    fun isSuccess() = this is Success
    fun isFailure() = this is Failure
    fun isNoResult() = this === NoResult

    // Helper method for accessing the value or handling failure
    fun onSuccess(action: (T) -> Unit): EvalResult<T> = also { if (this is Success) action(value) }
    fun onFailure(action: (EvalError) -> Unit): EvalResult<T> = also { if (this is Failure) action(error) }
}

class EvaluationNode(
    val element: IrElement,
    private var _result: EvalResult<ConstantValue<*>> = EvalResult.NoResult,
    val childResults: MutableMap<IrElement, ConstantValue<*>> = mutableMapOf()
) {
    // Public getter for the result
    val result: EvalResult<ConstantValue<*>> get() = _result

    fun setResult(value: EvalResult<ConstantValue<*>>) {
        if (_result !is EvalResult.Failure) {
            _result = value
        }
    }

    // Set a successful result only if the node has no previous failure (fail-once rule)
    fun setSuccessResult(value: ConstantValue<*>) {
        setResult(EvalResult.Success(value))
    }

    // Set an error if no result or error has been set (fail-once rule)
    fun setErrorResult(error: EvalError) {
        setResult(EvalResult.Failure(error))
    }

    // Get the result of the last evaluated child, or NoResult if there are no children
    fun lastChildResult(): EvalResult<ConstantValue<*>> {
        return childResults.values.lastOrNull()?.let { EvalResult.Success(it) } ?: EvalResult.NoResult
    }
}

class EvaluationGraph {
    val stack = mutableListOf<EvaluationNode>()
    var root: EvaluationNode? = null
        private set

    fun push(expression: IrElement) {
        val newNode = EvaluationNode(expression)
        stack.add(newNode)

        if (stack.size == 1) {
            root = newNode
        }
    }

    fun pop(): EvaluationNode? {
        val completedNode = if (stack.isNotEmpty()) stack.removeAt(stack.size - 1) else null

        // If there's a parent node, propagate the child result if the parent has no result
        stack.lastOrNull()?.let { parent ->
            completedNode?.let { child ->
                child.result.onFailure { parent.setErrorResult(it) } // Propagate failure
                child.result.onSuccess { result ->
                    parent.childResults[child.element] = result
                }

                // Set the parent result to the last child's result if no explicit result was set
                if (parent.result !is EvalResult.Failure) {
                    parent.setResult(child.result)
                }
            }
        }

        return completedNode
    }

    // Set a successful result for the current node (top of the stack)
    fun setResultForCurrentNode(result: ConstantValue<*>) {
        stack.lastOrNull()?.setSuccessResult(result)
    }

    // Set an error for the current node (top of the stack)
    fun setErrorForCurrentNode(error: EvalError) {
        stack.lastOrNull()?.setErrorResult(error)
    }

    // Retrieve a child result by `IrElement` in the current frame
    fun getChildResult(expression: IrElement): EvalResult<ConstantValue<*>> {
        return stack.lastOrNull()?.childResults?.get(expression)?.let { EvalResult.Success(it) }
            ?: EvalResult.NoResult
    }

    // Retrieve the last child result in the current frame
    fun getLastChildResult(): EvalResult<ConstantValue<*>> {
        return stack.lastOrNull()?.lastChildResult() ?: EvalResult.NoResult
    }

    fun isFailure(): Boolean {
        return stack.lastOrNull()?.result?.isFailure() == true
    }
}
