package hr.kbratko.eval.interpreter.functions

import arrow.core.Either
import hr.kbratko.eval.types.ComptimeConstant
import hr.kbratko.eval.types.ComptimeError

data class FunctionName(val value: String)

fun GetterName(value: String) = FunctionName("<get-$value>")

data class ComptimeFunctionSignature(val argumentTypes: List<ComptimeTypePredicate>)

fun interface ComptimeFunction {
    operator fun invoke(arguments: List<ComptimeConstant>): Either<ComptimeError, ComptimeConstant>
}

data class ComptimeFunctionDefinition(
    val name: FunctionName,
    val signature: ComptimeFunctionSignature,
    val implementation: ComptimeFunction
)