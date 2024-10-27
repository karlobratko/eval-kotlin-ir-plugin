package hr.kbratko.eval.stack

import hr.kbratko.eval.EvalError
import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.ir.IrElement

sealed class Outcome<out T> {
    data class Value<T>(val value: T) : Outcome<T>()

    data object Empty : Outcome<Nothing>()

    sealed class Control<out T> : Outcome<T>() {
        data class Return<T>(val value: T) : Control<T>()

        data class Error(val error: EvalError) : Control<Nothing>()
    }

    val isValue: Boolean get() = this is Value
    val isEmpty: Boolean get() = this is Value
    val isReturn: Boolean get() = this is Control.Return
    val isError: Boolean get() = this is Control.Error
    val isControl: Boolean = isError or isReturn
}

class EvaluationScope(override val element: IrElement) : ElementScope<IrElement> {
    var outcome: Outcome<ConstantValue<*>> = Outcome.Empty
        private set

    private val _childOutcomes = mutableMapOf<IrElement, Outcome<ConstantValue<*>>>()
    val childOutcomes: Map<IrElement, Outcome<ConstantValue<*>>> get() = _childOutcomes

    fun updateOutcome(newOutcome: Outcome<ConstantValue<*>>) {
        // set outcome only if it is not control
        if (!outcome.isControl) {
            outcome = newOutcome
        }
    }

    fun fail(error: EvalError) {
        updateOutcome(Outcome.Control.Error(error))
    }

    fun setValue(value: ConstantValue<*>) {
        updateOutcome(Outcome.Value(value))
    }

    fun setReturn(value: ConstantValue<*>) {
        updateOutcome(Outcome.Control.Return(value))
    }

    fun recordChildOutcome(element: IrElement, outcome: Outcome<ConstantValue<*>>) {
        _childOutcomes.put(element, outcome)
    }

    val lastChildOutcome: Outcome<ConstantValue<*>>
        get() = _childOutcomes.values.lastOrNull() ?: Outcome.Empty

    val isControl: Boolean get() = outcome.isReturn or outcome.isError
}

class EvaluationStack : ElementScopeStack<EvaluationScope>() {
    private var _root: EvaluationScope? = null
    val root: EvaluationScope get() = _root ?: error("Root EvaluationScope not initialized")

    override fun push(scope: EvaluationScope): EvaluationScope? {
        // push only if parent outcome is not Control
        peek()?.let { parentScope ->
            if (parentScope.isControl) {
                return null
            }
        }

        // set root to initial value
        if (_root == null) {
            _root = scope
        }

        return super.push(scope)
    }

    override fun pop(): EvaluationScope? {
        val childScope = super.pop()

        peek()?.let { parentScope ->
            childScope?.let { childScope ->
                // push child outcome to parent
                parentScope.recordChildOutcome(childScope.element, childScope.outcome)

                // if child outcome is control set parent outcome to control
                if (childScope.isControl) {
                    parentScope.updateOutcome(childScope.outcome)
                }
            }
        }

        return childScope
    }
}
