package hr.kbratko.eval.interpreter.functions.constant

import arrow.core.right
import hr.kbratko.eval.interpreter.functions.*
import hr.kbratko.eval.types.BooleanConstant
import hr.kbratko.eval.types.IntConstant

fun ComptimeFunctionRegistry.registerBooleanOperators() {
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("compareTo"),
            signature = ComptimeFunctionSignature(listOf(BooleanConstantType, BooleanConstantType)),
            implementation = { args ->
                val receiver = args[0] as BooleanConstant
                val other = args[1] as BooleanConstant
                IntConstant(receiver.value.compareTo(other.value)).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greater"),
            signature = ComptimeFunctionSignature(listOf(BooleanConstantType, BooleanConstantType)),
            implementation = { args ->
                val receiver = args[0] as BooleanConstant
                val other = args[1] as BooleanConstant
                BooleanConstant(receiver.value > other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greaterOrEqual"),
            signature = ComptimeFunctionSignature(listOf(BooleanConstantType, BooleanConstantType)),
            implementation = { args ->
                val receiver = args[0] as BooleanConstant
                val other = args[1] as BooleanConstant
                BooleanConstant(receiver.value >= other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("less"),
            signature = ComptimeFunctionSignature(listOf(BooleanConstantType, BooleanConstantType)),
            implementation = { args ->
                val receiver = args[0] as BooleanConstant
                val other = args[1] as BooleanConstant
                BooleanConstant(receiver.value < other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("lessOrEqual"),
            signature = ComptimeFunctionSignature(listOf(BooleanConstantType, BooleanConstantType)),
            implementation = { args ->
                val receiver = args[0] as BooleanConstant
                val other = args[1] as BooleanConstant
                BooleanConstant(receiver.value <= other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("and"),
            signature = ComptimeFunctionSignature(listOf(BooleanConstantType, BooleanConstantType)),
            implementation = { args ->
                val receiver = args[0] as BooleanConstant
                val other = args[1] as BooleanConstant
                BooleanConstant(receiver.value && other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("or"),
            signature = ComptimeFunctionSignature(listOf(BooleanConstantType, BooleanConstantType)),
            implementation = { args ->
                val receiver = args[0] as BooleanConstant
                val other = args[1] as BooleanConstant
                BooleanConstant(receiver.value || other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("not"),
            signature = ComptimeFunctionSignature(listOf(BooleanConstantType)),
            implementation = { args ->
                val receiver = args[0] as BooleanConstant
                BooleanConstant(!receiver.value).right()
            }
        )
    )
}