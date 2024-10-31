package hr.kbratko.eval.interpreter.functions.constant

import arrow.core.right
import hr.kbratko.eval.interpreter.functions.ComptimeFunction
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionDefinition
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionRegistry
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionSignature
import hr.kbratko.eval.interpreter.functions.FunctionName
import hr.kbratko.eval.interpreter.functions.ULongConstantType
import hr.kbratko.eval.interpreter.functions.UnsignedConstantType
import hr.kbratko.eval.interpreter.functions.UnsignedIntegerConstantType
import hr.kbratko.eval.types.BooleanConstant
import hr.kbratko.eval.types.ByteConstant
import hr.kbratko.eval.types.DoubleConstant
import hr.kbratko.eval.types.FloatConstant
import hr.kbratko.eval.types.IntConstant
import hr.kbratko.eval.types.LongConstant
import hr.kbratko.eval.types.ShortConstant
import hr.kbratko.eval.types.StringConstant
import hr.kbratko.eval.types.UByteConstant
import hr.kbratko.eval.types.UIntConstant
import hr.kbratko.eval.types.ULongConstant
import hr.kbratko.eval.types.UShortConstant
import hr.kbratko.eval.types.UnsignedConstant
import hr.kbratko.eval.types.UnsignedIntegerConstant

fun ComptimeFunctionRegistry.registerULongOperators() {
    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("compareTo"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType, UnsignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                val other = args[1] as UnsignedConstant
                IntConstant(
                    when (other) {
                        is UByteConstant -> receiver.value.compareTo(other.value)
                        is UShortConstant -> receiver.value.compareTo(other.value)
                        is UIntConstant -> receiver.value.compareTo(other.value)
                        is ULongConstant -> receiver.value.compareTo(other.value)
                    }
                ).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greater"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType, ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                val other = args[1] as ULongConstant
                BooleanConstant(receiver.value > other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greaterOrEqual"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType, ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                val other = args[1] as ULongConstant
                BooleanConstant(receiver.value >= other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("less"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType, ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                val other = args[1] as ULongConstant
                BooleanConstant(receiver.value < other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("lessOrEqual"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType, ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                val other = args[1] as ULongConstant
                BooleanConstant(receiver.value <= other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("plus"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType, UnsignedIntegerConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                val other = args[1] as UnsignedIntegerConstant
                when (other) {
                    is UByteConstant -> ULongConstant(receiver.value.plus(other.value))
                    is UShortConstant -> ULongConstant(receiver.value.plus(other.value))
                    is UIntConstant -> ULongConstant(receiver.value.plus(other.value))
                    is ULongConstant -> ULongConstant(receiver.value.plus(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("minus"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType, UnsignedIntegerConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                val other = args[1] as UnsignedIntegerConstant
                when (other) {
                    is UByteConstant -> ULongConstant(receiver.value.minus(other.value))
                    is UShortConstant -> ULongConstant(receiver.value.minus(other.value))
                    is UIntConstant -> ULongConstant(receiver.value.minus(other.value))
                    is ULongConstant -> ULongConstant(receiver.value.minus(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("times"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType, UnsignedIntegerConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                val other = args[1] as UnsignedIntegerConstant
                when (other) {
                    is UByteConstant -> ULongConstant(receiver.value.times(other.value))
                    is UShortConstant -> ULongConstant(receiver.value.times(other.value))
                    is UIntConstant -> ULongConstant(receiver.value.times(other.value))
                    is ULongConstant -> ULongConstant(receiver.value.times(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("div"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType, UnsignedIntegerConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                val other = args[1] as UnsignedIntegerConstant
                when (other) {
                    is UByteConstant -> ULongConstant(receiver.value.div(other.value))
                    is UShortConstant -> ULongConstant(receiver.value.div(other.value))
                    is UIntConstant -> ULongConstant(receiver.value.div(other.value))
                    is ULongConstant -> ULongConstant(receiver.value.div(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("rem"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType, UnsignedIntegerConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                val other = args[1] as UnsignedIntegerConstant
                when (other) {
                    is UByteConstant -> ULongConstant(receiver.value.rem(other.value))
                    is UShortConstant -> ULongConstant(receiver.value.rem(other.value))
                    is UIntConstant -> ULongConstant(receiver.value.rem(other.value))
                    is ULongConstant -> ULongConstant(receiver.value.rem(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("mod"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType, UnsignedIntegerConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                val other = args[1] as UnsignedIntegerConstant
                when (other) {
                    is UByteConstant -> UByteConstant(receiver.value.mod(other.value))
                    is UShortConstant -> UShortConstant(receiver.value.mod(other.value))
                    is UIntConstant -> UIntConstant(receiver.value.mod(other.value))
                    is ULongConstant -> ULongConstant(receiver.value.mod(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("inc"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                ULongConstant(receiver.value.inc()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("dec"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                ULongConstant(receiver.value.dec()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("and"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType, ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                val other = args[1] as ULongConstant
                ULongConstant(receiver.value.and(other.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("or"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType, ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                val other = args[1] as ULongConstant
                ULongConstant(receiver.value.or(other.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("xor"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType, ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                val other = args[1] as ULongConstant
                ULongConstant(receiver.value.xor(other.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("inv"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                ULongConstant(receiver.value.inv()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toByte"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                ByteConstant(receiver.value.toByte()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toShort"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                ShortConstant(receiver.value.toShort()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toInt"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                IntConstant(receiver.value.toInt()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toLong"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                LongConstant(receiver.value.toLong()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toUByte"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                UByteConstant(receiver.value.toUByte()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toUShort"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                UShortConstant(receiver.value.toUShort()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toUInt"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                UIntConstant(receiver.value.toUInt()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toULong"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                ULongConstant(receiver.value.toULong()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toFloat"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                FloatConstant(receiver.value.toFloat()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toDouble"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                DoubleConstant(receiver.value.toDouble()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toString"),
            signature = ComptimeFunctionSignature(listOf(ULongConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as ULongConstant
                StringConstant(receiver.value.toString()).right()
            }
        )
    )
}