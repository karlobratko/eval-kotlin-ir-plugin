package hr.kbratko.eval

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.jetbrains.kotlin.constant.BooleanValue
import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.constant.IntValue
import org.jetbrains.kotlin.name.Name

sealed interface MethodEvaluator {
    operator fun invoke(name: Name, arguments: List<ConstantValue<*>>): Either<EvalError, ConstantValue<*>>
}

class IntMethodEvaluator : MethodEvaluator {
    override fun invoke(name: Name, arguments: List<ConstantValue<*>>): Either<EvalError, ConstantValue<*>> {
        require(arguments.size == 2) { "Currently only two parameters are supported" }
        val argument1 = arguments[0]
        val argument2 = arguments[1]
        require(argument1 is IntValue && argument2 is IntValue) { "Currently only Int parameters are supported" }

        return when (name.asString()) {
            "plus" -> IntValue(argument1.value + argument2.value).right()
            "minus" -> IntValue(argument1.value - argument2.value).right()
            "times" -> IntValue(argument1.value * argument2.value).right()
            "div" -> IntValue(argument1.value / argument2.value).right()
            else -> UnsupportedMethod.left()
        }
    }
}

class BooleanMethodEvaluator : MethodEvaluator {
    override fun invoke(name: Name, arguments: List<ConstantValue<*>>): Either<EvalError, ConstantValue<*>> {
        require(arguments.size == 2) { "Currently only two parameters are supported" }
        val argument1 = arguments[0]
        val argument2 = arguments[1]
        require(argument1 is BooleanValue && argument2 is BooleanValue) { "Currently only Boolean parameters are supported" }

        return when (name.asString()) {
            "and" -> BooleanValue(argument1.value and argument2.value).right()
            "or" -> BooleanValue(argument1.value or argument2.value).right()
            // "not" -> BooleanValue(argument1.value.argument2.value()
            "xor" -> BooleanValue(argument1.value xor argument2.value).right()
            else -> UnsupportedMethod.left()
        }
    }
}