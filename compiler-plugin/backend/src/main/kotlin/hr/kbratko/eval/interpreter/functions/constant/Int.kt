package hr.kbratko.eval.interpreter.functions.constant

import arrow.core.right
import hr.kbratko.eval.interpreter.functions.ComptimeFunction
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionDefinition
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionRegistry
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionSignature
import hr.kbratko.eval.interpreter.functions.FunctionName
import hr.kbratko.eval.interpreter.functions.IntConstantType
import hr.kbratko.eval.interpreter.functions.SignedConstantType
import hr.kbratko.eval.interpreter.functions.SignedIntegerConstantType
import hr.kbratko.eval.types.BooleanConstant
import hr.kbratko.eval.types.ByteConstant
import hr.kbratko.eval.types.CharConstant
import hr.kbratko.eval.types.DoubleConstant
import hr.kbratko.eval.types.FloatConstant
import hr.kbratko.eval.types.IntConstant
import hr.kbratko.eval.types.LongConstant
import hr.kbratko.eval.types.ShortConstant
import hr.kbratko.eval.types.SignedConstant
import hr.kbratko.eval.types.SignedIntegerConstant
import hr.kbratko.eval.types.StringConstant
import hr.kbratko.eval.types.UByteConstant
import hr.kbratko.eval.types.UIntConstant
import hr.kbratko.eval.types.ULongConstant
import hr.kbratko.eval.types.UShortConstant

fun ComptimeFunctionRegistry.registerIntOperators() {
    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("compareTo"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
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
            signature = ComptimeFunctionSignature(listOf(IntConstantType, IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                val other = args[1] as IntConstant
                BooleanConstant(receiver.value > other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greaterOrEqual"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType, IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                val other = args[1] as IntConstant
                BooleanConstant(receiver.value >= other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("less"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType, IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                val other = args[1] as IntConstant
                BooleanConstant(receiver.value < other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("lessOrEqual"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType, IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                val other = args[1] as IntConstant
                BooleanConstant(receiver.value <= other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("plus"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> IntConstant(receiver.value.plus(other.value))
                    is ShortConstant -> IntConstant(receiver.value.plus(other.value))
                    is IntConstant -> IntConstant(receiver.value.plus(other.value))
                    is LongConstant -> LongConstant(receiver.value.plus(other.value))
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
            signature = ComptimeFunctionSignature(listOf(IntConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> IntConstant(receiver.value.minus(other.value))
                    is ShortConstant -> IntConstant(receiver.value.minus(other.value))
                    is IntConstant -> IntConstant(receiver.value.minus(other.value))
                    is LongConstant -> LongConstant(receiver.value.minus(other.value))
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
            signature = ComptimeFunctionSignature(listOf(IntConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> IntConstant(receiver.value.times(other.value))
                    is ShortConstant -> IntConstant(receiver.value.times(other.value))
                    is IntConstant -> IntConstant(receiver.value.times(other.value))
                    is LongConstant -> LongConstant(receiver.value.times(other.value))
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
            signature = ComptimeFunctionSignature(listOf(IntConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> IntConstant(receiver.value.div(other.value))
                    is ShortConstant -> IntConstant(receiver.value.div(other.value))
                    is IntConstant -> IntConstant(receiver.value.div(other.value))
                    is LongConstant -> LongConstant(receiver.value.div(other.value))
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
            signature = ComptimeFunctionSignature(listOf(IntConstantType, SignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                val other = args[1] as SignedConstant
                when (other) {
                    is ByteConstant -> IntConstant(receiver.value.rem(other.value))
                    is ShortConstant -> IntConstant(receiver.value.rem(other.value))
                    is IntConstant -> IntConstant(receiver.value.rem(other.value))
                    is LongConstant -> LongConstant(receiver.value.rem(other.value))
                    is FloatConstant -> FloatConstant(receiver.value.rem(other.value))
                    is DoubleConstant -> DoubleConstant(receiver.value.rem(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("mod"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType, SignedIntegerConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
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

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("inc"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                IntConstant(receiver.value.inc()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("dec"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                IntConstant(receiver.value.dec()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("unaryPlus"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                IntConstant(receiver.value.unaryPlus()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("unaryMinus"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                IntConstant(receiver.value.unaryMinus()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("shl"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType, IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                val bitCount = args[1] as IntConstant
                IntConstant(receiver.value.shl(bitCount.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("shr"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType, IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                val bitCount = args[1] as IntConstant
                IntConstant(receiver.value.shr(bitCount.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("ushr"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType, IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                val bitCount = args[1] as IntConstant
                IntConstant(receiver.value.ushr(bitCount.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("and"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType, IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                val other = args[1] as IntConstant
                IntConstant(receiver.value.and(other.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("or"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType, IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                val other = args[1] as IntConstant
                IntConstant(receiver.value.or(other.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("xor"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType, IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                val other = args[1] as IntConstant
                IntConstant(receiver.value.xor(other.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("inv"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                IntConstant(receiver.value.inv()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toByte"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                ByteConstant(receiver.value.toByte()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toChar"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                CharConstant(receiver.value.toChar()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toShort"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                ShortConstant(receiver.value.toShort()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toInt"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                IntConstant(receiver.value.toInt()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toLong"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                LongConstant(receiver.value.toLong()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toUByte"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                UByteConstant(receiver.value.toUByte()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toUShort"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                UShortConstant(receiver.value.toUShort()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toUInt"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                UIntConstant(receiver.value.toUInt()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toULong"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                ULongConstant(receiver.value.toULong()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toFloat"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                FloatConstant(receiver.value.toFloat()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toDouble"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                DoubleConstant(receiver.value.toDouble()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toString"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                StringConstant(receiver.value.toString()).right()
            }
        )
    )
}