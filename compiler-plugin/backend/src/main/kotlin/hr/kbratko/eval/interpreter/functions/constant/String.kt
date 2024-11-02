package hr.kbratko.eval.interpreter.functions.constant

import arrow.core.right
import hr.kbratko.eval.interpreter.functions.*
import hr.kbratko.eval.types.BooleanConstant
import hr.kbratko.eval.types.CharConstant
import hr.kbratko.eval.types.IntConstant
import hr.kbratko.eval.types.StringConstant

fun ComptimeFunctionRegistry.registerStringOperators() {
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("compareTo"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType, StringConstantType)),
            implementation = { args ->
                val receiver = args[0] as StringConstant
                val other = args[1] as StringConstant
                IntConstant(receiver.value.compareTo(other.value)).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greater"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType, StringConstantType)),
            implementation = { args ->
                val receiver = args[0] as StringConstant
                val other = args[1] as StringConstant
                BooleanConstant(receiver.value > other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greaterOrEqual"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType, StringConstantType)),
            implementation = { args ->
                val receiver = args[0] as StringConstant
                val other = args[1] as StringConstant
                BooleanConstant(receiver.value >= other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("less"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType, StringConstantType)),
            implementation = { args ->
                val receiver = args[0] as StringConstant
                val other = args[1] as StringConstant
                BooleanConstant(receiver.value < other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("lessOrEqual"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType, StringConstantType)),
            implementation = { args ->
                val receiver = args[0] as StringConstant
                val other = args[1] as StringConstant
                BooleanConstant(receiver.value <= other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("plus"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType, ComptimeConstantType)),
            implementation = { args ->
                val receiver = args[0] as StringConstant
                val other = args[1]
                StringConstant(receiver.value.plus(other.value)).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = GetterName("length"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType)),
            implementation = { args ->
                val receiver = args.first() as StringConstant
                IntConstant(receiver.value.length).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("get"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType, IntConstantType)),
            implementation = { args ->
                val receiver = args[0] as StringConstant
                val index = args[1] as IntConstant
                CharConstant(receiver.value[index.value]).right()
            }
        )
    )
}