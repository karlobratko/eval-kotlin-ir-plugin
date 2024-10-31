package hr.kbratko.eval.interpreter.functions.constant

import arrow.core.right
import hr.kbratko.eval.interpreter.functions.BooleanConstantType
import hr.kbratko.eval.interpreter.functions.ComptimeFunction
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionDefinition
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionSignature
import hr.kbratko.eval.interpreter.functions.FunctionName
import hr.kbratko.eval.types.BooleanConstant
import hr.kbratko.eval.types.IntConstant

fun hr.kbratko.eval.interpreter.functions.ComptimeFunctionRegistry.registerBooleanOperators() {
    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("compareTo"),
            signature = ComptimeFunctionSignature(listOf(BooleanConstantType, BooleanConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as BooleanConstant
                val other = args[1] as BooleanConstant
                IntConstant(receiver.value.compareTo(other.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greater"),
            signature = ComptimeFunctionSignature(listOf(BooleanConstantType, BooleanConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as BooleanConstant
                val other = args[1] as BooleanConstant
                BooleanConstant(receiver.value > other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greaterOrEqual"),
            signature = ComptimeFunctionSignature(listOf(BooleanConstantType, BooleanConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as BooleanConstant
                val other = args[1] as BooleanConstant
                BooleanConstant(receiver.value >= other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("less"),
            signature = ComptimeFunctionSignature(listOf(BooleanConstantType, BooleanConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as BooleanConstant
                val other = args[1] as BooleanConstant
                BooleanConstant(receiver.value < other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("lessOrEqual"),
            signature = ComptimeFunctionSignature(listOf(BooleanConstantType, BooleanConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as BooleanConstant
                val other = args[1] as BooleanConstant
                BooleanConstant(receiver.value <= other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("and"),
            signature = ComptimeFunctionSignature(listOf(BooleanConstantType, BooleanConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as BooleanConstant
                val other = args[1] as BooleanConstant
                BooleanConstant(receiver.value && other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("or"),
            signature = ComptimeFunctionSignature(listOf(BooleanConstantType, BooleanConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as BooleanConstant
                val other = args[1] as BooleanConstant
                BooleanConstant(receiver.value || other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("not"),
            signature = ComptimeFunctionSignature(listOf(BooleanConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as BooleanConstant
                BooleanConstant(!receiver.value).right()
            }
        )
    )
}