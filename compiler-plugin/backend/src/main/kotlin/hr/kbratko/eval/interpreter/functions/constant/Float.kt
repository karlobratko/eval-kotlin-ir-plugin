package hr.kbratko.eval.interpreter.functions.constant

import arrow.core.right
import hr.kbratko.eval.interpreter.functions.ComptimeFunction
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionDefinition
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionRegistry
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionSignature
import hr.kbratko.eval.interpreter.functions.FloatConstantType
import hr.kbratko.eval.interpreter.functions.FunctionName
import hr.kbratko.eval.interpreter.functions.SignedConstantType
import hr.kbratko.eval.types.BooleanConstant
import hr.kbratko.eval.types.ByteConstant
import hr.kbratko.eval.types.CharConstant
import hr.kbratko.eval.types.DoubleConstant
import hr.kbratko.eval.types.FloatConstant
import hr.kbratko.eval.types.IntConstant
import hr.kbratko.eval.types.LongConstant
import hr.kbratko.eval.types.ShortConstant
import hr.kbratko.eval.types.SignedConstant
import hr.kbratko.eval.types.StringConstant

fun ComptimeFunctionRegistry.registerFloatOperators() {
    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("compareTo"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                val other = args[1] as SignedConstant
                IntConstant(
                    when (other) {
                        is ByteConstant -> receiver.value.compareTo(other.value)
                        is ShortConstant -> receiver.value.compareTo(other.value)
                        is IntConstant -> receiver.value.compareTo(other.value)
                        is LongConstant -> receiver.value.compareTo(other.value)
                        is FloatConstant -> receiver.value.compareTo(other.value)
                        is DoubleConstant -> receiver.value.compareTo(other.value)
                    }
                ).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greater"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType, FloatConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                val other = args[1] as FloatConstant
                BooleanConstant(receiver.value > other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greaterOrEqual"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType, FloatConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                val other = args[1] as FloatConstant
                BooleanConstant(receiver.value >= other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("less"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType, FloatConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                val other = args[1] as FloatConstant
                BooleanConstant(receiver.value < other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("lessOrEqual"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType, FloatConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                val other = args[1] as FloatConstant
                BooleanConstant(receiver.value <= other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("plus"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> FloatConstant(receiver.value.plus(other.value))
                    is ShortConstant -> FloatConstant(receiver.value.plus(other.value))
                    is IntConstant -> FloatConstant(receiver.value.plus(other.value))
                    is LongConstant -> FloatConstant(receiver.value.plus(other.value))
                    is FloatConstant -> FloatConstant(receiver.value.plus(other.value))
                    is DoubleConstant -> DoubleConstant(receiver.value.plus(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("minus"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> FloatConstant(receiver.value.minus(other.value))
                    is ShortConstant -> FloatConstant(receiver.value.minus(other.value))
                    is IntConstant -> FloatConstant(receiver.value.minus(other.value))
                    is LongConstant -> FloatConstant(receiver.value.minus(other.value))
                    is FloatConstant -> FloatConstant(receiver.value.minus(other.value))
                    is DoubleConstant -> DoubleConstant(receiver.value.minus(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("times"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> FloatConstant(receiver.value.times(other.value))
                    is ShortConstant -> FloatConstant(receiver.value.times(other.value))
                    is IntConstant -> FloatConstant(receiver.value.times(other.value))
                    is LongConstant -> FloatConstant(receiver.value.times(other.value))
                    is FloatConstant -> FloatConstant(receiver.value.times(other.value))
                    is DoubleConstant -> DoubleConstant(receiver.value.times(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("div"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> FloatConstant(receiver.value.div(other.value))
                    is ShortConstant -> FloatConstant(receiver.value.div(other.value))
                    is IntConstant -> FloatConstant(receiver.value.div(other.value))
                    is LongConstant -> FloatConstant(receiver.value.div(other.value))
                    is FloatConstant -> FloatConstant(receiver.value.div(other.value))
                    is DoubleConstant -> DoubleConstant(receiver.value.div(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("rem"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> FloatConstant(receiver.value.rem(other.value))
                    is ShortConstant -> FloatConstant(receiver.value.rem(other.value))
                    is IntConstant -> FloatConstant(receiver.value.rem(other.value))
                    is LongConstant -> FloatConstant(receiver.value.rem(other.value))
                    is FloatConstant -> FloatConstant(receiver.value.rem(other.value))
                    is DoubleConstant -> DoubleConstant(receiver.value.rem(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("inc"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                FloatConstant(receiver.value.inc()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("dec"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                FloatConstant(receiver.value.dec()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("unaryPlus"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                FloatConstant(receiver.value.unaryPlus()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("unaryMinus"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                FloatConstant(receiver.value.unaryMinus()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toChar"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                CharConstant(receiver.value.toChar()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toInt"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                IntConstant(receiver.value.toInt()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toLong"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                LongConstant(receiver.value.toLong()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toFloat"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                FloatConstant(receiver.value.toFloat()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toDouble"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                DoubleConstant(receiver.value.toDouble()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toString"),
            signature = ComptimeFunctionSignature(listOf(FloatConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as FloatConstant
                StringConstant(receiver.value.toString()).right()
            }
        )
    )
}