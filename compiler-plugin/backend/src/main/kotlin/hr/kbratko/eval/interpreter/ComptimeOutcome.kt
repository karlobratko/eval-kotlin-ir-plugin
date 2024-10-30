package hr.kbratko.eval.interpreter

import hr.kbratko.eval.ComptimeError
import hr.kbratko.eval.interpreter.EvaluationOutcome.EvaluationResult.ConstantResult
import org.jetbrains.kotlin.constant.BooleanValue
import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.ir.expressions.IrLoop
import org.jetbrains.kotlin.ir.symbols.IrReturnTargetSymbol

sealed interface EvaluationOutcome<out T> {

    sealed interface EvaluationResult<out T> : EvaluationOutcome<T> {
        data class ConstantResult<T>(val value: ConstantValue<T>) : EvaluationResult<T>
        data object NoResult : EvaluationResult<Nothing>
    }

    sealed interface ControlFlow<out T> : EvaluationOutcome<T> {

        data class Return<T>(val value: ConstantValue<T>, val target: IrReturnTargetSymbol) : ControlFlow<T>

        sealed interface LoopControl : ControlFlow<Nothing> {
            val loop: IrLoop
            val label: String?

            data class Break(override val loop: IrLoop, override val label: String? = null) : LoopControl
            data class Continue(override val loop: IrLoop, override val label: String? = null) : LoopControl

            fun matches(element: IrLoop): Boolean = label == element.label || loop == element
        }

        data class EvaluationError(val error: ComptimeError) : ControlFlow<Nothing>
    }

    val isControlFlow: Boolean get() = this is ControlFlow<*>

}


fun EvaluationOutcome<*>.isBooleanTrueValue() =
    this is ConstantResult && this.value is BooleanValue && this.value.value == true

fun EvaluationOutcome<*>.isBooleanFalseValue() =
    this is ConstantResult && this.value is BooleanValue && this.value.value == false