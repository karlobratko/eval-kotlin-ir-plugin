package hr.kbratko.eval.interpreter.functions.constant

import arrow.core.right
import hr.kbratko.eval.interpreter.functions.ComptimeFunction
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionDefinition
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionSignature
import hr.kbratko.eval.interpreter.functions.DoubleConstantType
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

fun hr.kbratko.eval.interpreter.functions.ComptimeFunctionRegistry.registerDoubleOperators() {
    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("compareTo"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
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
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType, DoubleConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                val other = args[1] as DoubleConstant
                BooleanConstant(receiver.value > other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greaterOrEqual"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType, DoubleConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                val other = args[1] as DoubleConstant
                BooleanConstant(receiver.value >= other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("less"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType, DoubleConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                val other = args[1] as DoubleConstant
                BooleanConstant(receiver.value < other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("lessOrEqual"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType, DoubleConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                val other = args[1] as DoubleConstant
                BooleanConstant(receiver.value <= other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("plus"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> DoubleConstant(receiver.value.plus(other.value))
                    is ShortConstant -> DoubleConstant(receiver.value.plus(other.value))
                    is IntConstant -> DoubleConstant(receiver.value.plus(other.value))
                    is LongConstant -> DoubleConstant(receiver.value.plus(other.value))
                    is FloatConstant -> DoubleConstant(receiver.value.plus(other.value))
                    is DoubleConstant -> DoubleConstant(receiver.value.plus(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("minus"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> DoubleConstant(receiver.value.minus(other.value))
                    is ShortConstant -> DoubleConstant(receiver.value.minus(other.value))
                    is IntConstant -> DoubleConstant(receiver.value.minus(other.value))
                    is LongConstant -> DoubleConstant(receiver.value.minus(other.value))
                    is FloatConstant -> DoubleConstant(receiver.value.minus(other.value))
                    is DoubleConstant -> DoubleConstant(receiver.value.minus(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("times"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> DoubleConstant(receiver.value.times(other.value))
                    is ShortConstant -> DoubleConstant(receiver.value.times(other.value))
                    is IntConstant -> DoubleConstant(receiver.value.times(other.value))
                    is LongConstant -> DoubleConstant(receiver.value.times(other.value))
                    is FloatConstant -> DoubleConstant(receiver.value.times(other.value))
                    is DoubleConstant -> DoubleConstant(receiver.value.times(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("div"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> DoubleConstant(receiver.value.div(other.value))
                    is ShortConstant -> DoubleConstant(receiver.value.div(other.value))
                    is IntConstant -> DoubleConstant(receiver.value.div(other.value))
                    is LongConstant -> DoubleConstant(receiver.value.div(other.value))
                    is FloatConstant -> DoubleConstant(receiver.value.div(other.value))
                    is DoubleConstant -> DoubleConstant(receiver.value.div(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("rem"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> DoubleConstant(receiver.value.rem(other.value))
                    is ShortConstant -> DoubleConstant(receiver.value.rem(other.value))
                    is IntConstant -> DoubleConstant(receiver.value.rem(other.value))
                    is LongConstant -> DoubleConstant(receiver.value.rem(other.value))
                    is FloatConstant -> DoubleConstant(receiver.value.rem(other.value))
                    is DoubleConstant -> DoubleConstant(receiver.value.rem(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("inc"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                DoubleConstant(receiver.value.inc()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("dec"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                DoubleConstant(receiver.value.dec()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("unaryPlus"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                DoubleConstant(receiver.value.unaryPlus()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("unaryMinus"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                DoubleConstant(receiver.value.unaryMinus()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toChar"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                CharConstant(receiver.value.toChar()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toInt"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                IntConstant(receiver.value.toInt()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toLong"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                LongConstant(receiver.value.toLong()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toFloat"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                FloatConstant(receiver.value.toFloat()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toDouble"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                DoubleConstant(receiver.value.toDouble()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toString"),
            signature = ComptimeFunctionSignature(listOf(DoubleConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as DoubleConstant
                StringConstant(receiver.value.toString()).right()
            }
        )
    )
}