package hr.kbratko.eval.interpreter.functions.constant

import arrow.core.right
import hr.kbratko.eval.interpreter.functions.*
import hr.kbratko.eval.types.BooleanConstant
import hr.kbratko.eval.types.CharConstant
import hr.kbratko.eval.types.IntConstant
import hr.kbratko.eval.types.StringConstant

fun ComptimeFunctionRegistry.registerCharOperators() {
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("compareTo"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, CharConstantType)),
            implementation = { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as CharConstant
                IntConstant(receiver.value.compareTo(other.value)).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greater"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, CharConstantType)),
            implementation = { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as CharConstant
                BooleanConstant(receiver.value > other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greaterOrEqual"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, CharConstantType)),
            implementation = { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as CharConstant
                BooleanConstant(receiver.value >= other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("less"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, CharConstantType)),
            implementation = { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as CharConstant
                BooleanConstant(receiver.value < other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("lessOrEqual"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, CharConstantType)),
            implementation = { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as CharConstant
                BooleanConstant(receiver.value <= other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("plus"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, IntConstantType)),
            implementation = { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as IntConstant
                CharConstant(receiver.value.plus(other.value)).right()
            }
        ),
        ComptimeFunctionDefinition(
            name = FunctionName("plus"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, StringConstantType)),
            implementation = { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as StringConstant
                StringConstant(receiver.value.plus(other.value)).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("minus"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, CharConstantType)),
            implementation = { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as CharConstant
                IntConstant(receiver.value.minus(other.value)).right()
            }
        ),
        ComptimeFunctionDefinition(
            name = FunctionName("minus"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, IntConstantType)),
            implementation = { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as IntConstant
                CharConstant(receiver.value.minus(other.value)).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("inc"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType)),
            implementation = { args ->
                val receiver = args[0] as CharConstant
                CharConstant(receiver.value.inc()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("dec"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType)),
            implementation = { args ->
                val receiver = args[0] as CharConstant
                CharConstant(receiver.value.dec()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toChar"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType)),
            implementation = { args ->
                val receiver = args[0] as CharConstant
                CharConstant(receiver.value).right()
            }
        )
    )
}