package hr.kbratko.eval.interpreter.functions.constant

import arrow.core.right
import hr.kbratko.eval.interpreter.functions.*
import hr.kbratko.eval.types.*

fun ComptimeFunctionRegistry.registerLongOperators() {
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("compareTo"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType, SignedConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
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

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greater"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType, LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                val other = args[1] as LongConstant
                BooleanConstant(receiver.value > other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greaterOrEqual"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType, LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                val other = args[1] as LongConstant
                BooleanConstant(receiver.value >= other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("less"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType, LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                val other = args[1] as LongConstant
                BooleanConstant(receiver.value < other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("lessOrEqual"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType, LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                val other = args[1] as LongConstant
                BooleanConstant(receiver.value <= other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("plus"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType, SignedConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> LongConstant(receiver.value.plus(other.value))
                    is ShortConstant -> LongConstant(receiver.value.plus(other.value))
                    is IntConstant -> LongConstant(receiver.value.plus(other.value))
                    is LongConstant -> LongConstant(receiver.value.plus(other.value))
                    is FloatConstant -> FloatConstant(receiver.value.plus(other.value))
                    is DoubleConstant -> DoubleConstant(receiver.value.plus(other.value))
                }.right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("minus"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType, SignedConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> LongConstant(receiver.value.minus(other.value))
                    is ShortConstant -> LongConstant(receiver.value.minus(other.value))
                    is IntConstant -> LongConstant(receiver.value.minus(other.value))
                    is LongConstant -> LongConstant(receiver.value.minus(other.value))
                    is FloatConstant -> FloatConstant(receiver.value.minus(other.value))
                    is DoubleConstant -> DoubleConstant(receiver.value.minus(other.value))
                }.right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("times"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType, SignedConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> LongConstant(receiver.value.times(other.value))
                    is ShortConstant -> LongConstant(receiver.value.times(other.value))
                    is IntConstant -> LongConstant(receiver.value.times(other.value))
                    is LongConstant -> LongConstant(receiver.value.times(other.value))
                    is FloatConstant -> FloatConstant(receiver.value.times(other.value))
                    is DoubleConstant -> DoubleConstant(receiver.value.times(other.value))
                }.right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("div"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType, SignedConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> LongConstant(receiver.value.div(other.value))
                    is ShortConstant -> LongConstant(receiver.value.div(other.value))
                    is IntConstant -> LongConstant(receiver.value.div(other.value))
                    is LongConstant -> LongConstant(receiver.value.div(other.value))
                    is FloatConstant -> FloatConstant(receiver.value.div(other.value))
                    is DoubleConstant -> DoubleConstant(receiver.value.div(other.value))
                }.right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("rem"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType, SignedConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> LongConstant(receiver.value.rem(other.value))
                    is ShortConstant -> LongConstant(receiver.value.rem(other.value))
                    is IntConstant -> LongConstant(receiver.value.rem(other.value))
                    is LongConstant -> LongConstant(receiver.value.rem(other.value))
                    is FloatConstant -> FloatConstant(receiver.value.rem(other.value))
                    is DoubleConstant -> DoubleConstant(receiver.value.rem(other.value))
                }.right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("mod"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType, SignedIntegerConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                val other = args[1] as SignedIntegerConstant
                when (other) {
                    is ByteConstant -> ByteConstant(receiver.value.mod(other.value))
                    is ShortConstant -> ShortConstant(receiver.value.mod(other.value))
                    is IntConstant -> IntConstant(receiver.value.mod(other.value))
                    is LongConstant -> LongConstant(receiver.value.mod(other.value))
                }.right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("inc"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                LongConstant(receiver.value.inc()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("dec"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                LongConstant(receiver.value.dec()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("unaryPlus"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                LongConstant(receiver.value.unaryPlus()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("unaryMinus"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                LongConstant(receiver.value.unaryMinus()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toByte"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                ByteConstant(receiver.value.toByte()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toShort"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                ShortConstant(receiver.value.toShort()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toInt"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                IntConstant(receiver.value.toInt()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toLong"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                LongConstant(receiver.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toUByte"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                UByteConstant(receiver.value.toUByte()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toUShort"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                UShortConstant(receiver.value.toUShort()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toUInt"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                UIntConstant(receiver.value.toUInt()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toULong"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                ULongConstant(receiver.value.toULong()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toFloat"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                FloatConstant(receiver.value.toFloat()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toDouble"),
            signature = ComptimeFunctionSignature(listOf(LongConstantType)),
            implementation = { args ->
                val receiver = args[0] as LongConstant
                DoubleConstant(receiver.value.toDouble()).right()
            }
        )
    )
}