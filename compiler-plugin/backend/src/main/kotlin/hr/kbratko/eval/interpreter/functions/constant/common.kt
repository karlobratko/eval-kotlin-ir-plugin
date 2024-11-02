package hr.kbratko.eval.interpreter.functions.constant

import arrow.core.right
import hr.kbratko.eval.interpreter.functions.*
import hr.kbratko.eval.types.*
import kotlin.collections.component1
import kotlin.collections.component2

fun ComptimeFunctionRegistry.registerEquals() {
    val notDecimalConstantType = ComptimeTypePredicate { it !is DecimalConstant }

    val implementation = ComptimeFunction { args ->
        val (a, b) = args
        BooleanConstant(a.value == b.value).right()
    }
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("equals"),
            signature = ComptimeFunctionSignature(listOf(notDecimalConstantType, ComptimeConstantType)),
            implementation = implementation
        ),
        ComptimeFunctionDefinition(
            name = FunctionName("EQEQ"),
            signature = ComptimeFunctionSignature(listOf(notDecimalConstantType, ComptimeConstantType)),
            implementation = implementation
        ),
        ComptimeFunctionDefinition(
            name = FunctionName("ieee754equals"),
            signature = ComptimeFunctionSignature(listOf(DecimalConstantType, ComptimeConstantType)),
            implementation = implementation
        ),
    )
}

fun ComptimeFunctionRegistry.registerToString() {
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toString"),
            signature = ComptimeFunctionSignature(listOf(ComptimeConstantType)),
            implementation = { args ->
                val receiver = args[0]
                StringConstant(
                    when (receiver) {
                        is BooleanConstant -> receiver.value.toString()
                        is CharConstant -> receiver.value.toString()
                        is ByteConstant -> receiver.value.toString()
                        is IntConstant -> receiver.value.toString()
                        is LongConstant -> receiver.value.toString()
                        is ShortConstant -> receiver.value.toString()
                        is UByteConstant -> receiver.value.toString()
                        is UIntConstant -> receiver.value.toString()
                        is ULongConstant -> receiver.value.toString()
                        is UShortConstant -> receiver.value.toString()
                        is DoubleConstant -> receiver.value.toString()
                        is FloatConstant -> receiver.value.toString()
                        is StringConstant -> receiver.value
                    }
                ).right()
            }
        )
    )
}

fun ComptimeFunctionRegistry.registerHashCode() {
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("hashCode"),
            signature = ComptimeFunctionSignature(listOf(ComptimeConstantType)),
            implementation = { args ->
                val receiver = args[0]
                IntConstant(
                    when (receiver) {
                        is BooleanConstant -> receiver.value.hashCode()
                        is CharConstant -> receiver.value.hashCode()
                        is ByteConstant -> receiver.value.hashCode()
                        is IntConstant -> receiver.value.hashCode()
                        is LongConstant -> receiver.value.hashCode()
                        is ShortConstant -> receiver.value.hashCode()
                        is UByteConstant -> receiver.value.hashCode()
                        is UIntConstant -> receiver.value.hashCode()
                        is ULongConstant -> receiver.value.hashCode()
                        is UShortConstant -> receiver.value.hashCode()
                        is DoubleConstant -> receiver.value.hashCode()
                        is FloatConstant -> receiver.value.hashCode()
                        is StringConstant -> receiver.value.hashCode()
                    }
                ).right()
            }
        )
    )
}