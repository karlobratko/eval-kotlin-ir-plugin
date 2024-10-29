package hr.kbratko.eval.interpreter

import hr.kbratko.eval.ComptimeError
import hr.kbratko.eval.interpreter.ComptimeOutcome.Value
import org.jetbrains.kotlin.constant.BooleanValue
import org.jetbrains.kotlin.constant.ConstantValue

sealed class ComptimeOutcome<out T> {
    data class Value<T>(val value: ConstantValue<T>) : ComptimeOutcome<T>()

    data object Empty : ComptimeOutcome<Nothing>()

    sealed class Control<out T> : ComptimeOutcome<T>() {
        data class Return<T>(val value: ConstantValue<T>) : Control<T>()

        data class Error(val error: ComptimeError) : Control<Nothing>()
    }

    val isReturn: Boolean get() = this is Control.Return
    val isError: Boolean get() = this is Control.Error
    val isControl: Boolean = isError or isReturn
}

fun ComptimeOutcome<*>.isBooleanTrueValue() =
    this is Value && this.value is BooleanValue && this.value.value == true

fun ComptimeOutcome<*>.isBooleanFalseValue() =
    this is Value && this.value is BooleanValue && this.value.value == false