package hr.kbratko.eval.interpreter

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import hr.kbratko.eval.interpreter.EvaluationOutcome.ControlFlow.EvaluationError
import hr.kbratko.eval.interpreter.EvaluationOutcome.ControlFlow.Return
import hr.kbratko.eval.interpreter.EvaluationOutcome.EvaluationResult.ConstantResult
import hr.kbratko.eval.types.ComptimeConstant
import hr.kbratko.eval.types.ComptimeError
import hr.kbratko.eval.types.EmptyEvaluationOutcome
import hr.kbratko.eval.types.isConstantFalse
import hr.kbratko.eval.types.isConstantTrue
import org.jetbrains.kotlin.ir.expressions.IrLoop
import org.jetbrains.kotlin.ir.symbols.IrReturnTargetSymbol

sealed interface EvaluationOutcome {

    sealed interface EvaluationResult : EvaluationOutcome {
        data class ConstantResult(val value: ComptimeConstant) : EvaluationResult
        data object NoResult : EvaluationResult
    }

    sealed interface ControlFlow : EvaluationOutcome {

        data class Return(
            val value: ComptimeConstant,
            val target: IrReturnTargetSymbol
        ) : ControlFlow

        sealed interface LoopControl : ControlFlow {
            val loop: IrLoop
            val label: String?

            data class Break(override val loop: IrLoop, override val label: String? = null) : LoopControl
            data class Continue(override val loop: IrLoop, override val label: String? = null) : LoopControl

            fun matches(element: IrLoop): Boolean = label == element.label || loop == element
        }

        data class EvaluationError(val error: ComptimeError) : ControlFlow
    }

    val isControlFlow: Boolean get() = this is ControlFlow

}

fun EvaluationOutcome.isBooleanTrueResult() =
    this is ConstantResult && this.value.isConstantTrue()

fun EvaluationOutcome.isBooleanFalseResult() =
    this is ConstantResult && this.value.isConstantFalse()

fun EvaluationOutcome.toEither(): Either<ComptimeError, ComptimeConstant> =
    when (this) {
        is ConstantResult -> value.right()
        is Return -> value.right()
        is EvaluationError -> error.left()
        else -> EmptyEvaluationOutcome.left()
    }