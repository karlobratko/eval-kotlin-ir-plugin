package hr.kbratko.eval.interpreter.functions.constant

import arrow.core.right
import hr.kbratko.eval.interpreter.functions.ByteConstantType
import hr.kbratko.eval.interpreter.functions.CharConstantType
import hr.kbratko.eval.interpreter.functions.ComptimeFunction
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionDefinition
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionRegistry
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionSignature
import hr.kbratko.eval.interpreter.functions.FunctionName
import hr.kbratko.eval.interpreter.functions.IntConstantType
import hr.kbratko.eval.types.BooleanConstant
import hr.kbratko.eval.types.ByteConstant
import hr.kbratko.eval.types.CharConstant
import hr.kbratko.eval.types.DoubleConstant
import hr.kbratko.eval.types.FloatConstant
import hr.kbratko.eval.types.IntConstant
import hr.kbratko.eval.types.LongConstant
import hr.kbratko.eval.types.ShortConstant
import hr.kbratko.eval.types.StringConstant

fun ComptimeFunctionRegistry.registerCharOperators() {
    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("compareTo"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, CharConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as CharConstant
                IntConstant(receiver.value.compareTo(other.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greater"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, CharConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as CharConstant
                BooleanConstant(receiver.value > other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greaterOrEqual"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, CharConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as CharConstant
                BooleanConstant(receiver.value >= other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("less"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, CharConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as CharConstant
                BooleanConstant(receiver.value < other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("lessOrEqual"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, CharConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as CharConstant
                BooleanConstant(receiver.value <= other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("plus"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as IntConstant
                CharConstant(receiver.value.plus(other.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("minus"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, CharConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as CharConstant
                IntConstant(receiver.value.minus(other.value)).right()
            }
        ),
        ComptimeFunctionDefinition(
            name = FunctionName("minus"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType, IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                val other = args[1] as IntConstant
                CharConstant(receiver.value.minus(other.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("inc"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                CharConstant(receiver.value.inc()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("dec"),
            signature = ComptimeFunctionSignature(listOf(CharConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                CharConstant(receiver.value.dec()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toByte"),
            signature = ComptimeFunctionSignature(listOf(ByteConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                ByteConstant(receiver.value.toByte()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toChar"),
            signature = ComptimeFunctionSignature(listOf(ByteConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                CharConstant(receiver.value.toChar()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toShort"),
            signature = ComptimeFunctionSignature(listOf(ByteConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                ShortConstant(receiver.value.toShort()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toInt"),
            signature = ComptimeFunctionSignature(listOf(ByteConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                IntConstant(receiver.value.toInt()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toLong"),
            signature = ComptimeFunctionSignature(listOf(ByteConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                LongConstant(receiver.value.toLong()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toFloat"),
            signature = ComptimeFunctionSignature(listOf(ByteConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                FloatConstant(receiver.value.toFloat()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toDouble"),
            signature = ComptimeFunctionSignature(listOf(ByteConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                DoubleConstant(receiver.value.toDouble()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toString"),
            signature = ComptimeFunctionSignature(listOf(ByteConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as CharConstant
                StringConstant(receiver.value.toString()).right()
            }
        )
    )
}