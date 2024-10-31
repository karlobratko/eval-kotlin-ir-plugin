package hr.kbratko.eval.interpreter.functions.constant

import arrow.core.right
import hr.kbratko.eval.interpreter.functions.ComptimeFunction
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionDefinition
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionRegistry
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionSignature
import hr.kbratko.eval.interpreter.functions.FunctionName
import hr.kbratko.eval.interpreter.functions.UShortConstantType
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

fun ComptimeFunctionRegistry.registerUShortOperators() {
    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("compareTo"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType, UnsignedConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
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
            signature = ComptimeFunctionSignature(listOf(UShortConstantType, UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                val other = args[1] as UShortConstant
                BooleanConstant(receiver.value > other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greaterOrEqual"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType, UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                val other = args[1] as UShortConstant
                BooleanConstant(receiver.value >= other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("less"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType, UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                val other = args[1] as UShortConstant
                BooleanConstant(receiver.value < other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("lessOrEqual"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType, UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                val other = args[1] as UShortConstant
                BooleanConstant(receiver.value <= other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("plus"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType, UnsignedIntegerConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                val other = args[1] as UnsignedIntegerConstant
                when (other) {
                    is UByteConstant -> UIntConstant(receiver.value.plus(other.value))
                    is UShortConstant -> UIntConstant(receiver.value.plus(other.value))
                    is UIntConstant -> UIntConstant(receiver.value.plus(other.value))
                    is ULongConstant -> ULongConstant(receiver.value.plus(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("minus"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType, UnsignedIntegerConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                val other = args[1] as UnsignedIntegerConstant
                when (other) {
                    is UByteConstant -> UIntConstant(receiver.value.minus(other.value))
                    is UShortConstant -> UIntConstant(receiver.value.minus(other.value))
                    is UIntConstant -> UIntConstant(receiver.value.minus(other.value))
                    is ULongConstant -> ULongConstant(receiver.value.minus(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("times"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType, UnsignedIntegerConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                val other = args[1] as UnsignedIntegerConstant
                when (other) {
                    is UByteConstant -> UIntConstant(receiver.value.times(other.value))
                    is UShortConstant -> UIntConstant(receiver.value.times(other.value))
                    is UIntConstant -> UIntConstant(receiver.value.times(other.value))
                    is ULongConstant -> ULongConstant(receiver.value.times(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("div"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType, UnsignedIntegerConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                val other = args[1] as UnsignedIntegerConstant
                when (other) {
                    is UByteConstant -> UIntConstant(receiver.value.div(other.value))
                    is UShortConstant -> UIntConstant(receiver.value.div(other.value))
                    is UIntConstant -> UIntConstant(receiver.value.div(other.value))
                    is ULongConstant -> ULongConstant(receiver.value.div(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("rem"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType, UnsignedIntegerConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                val other = args[1] as UnsignedIntegerConstant
                when (other) {
                    is UByteConstant -> UIntConstant(receiver.value.rem(other.value))
                    is UShortConstant -> UIntConstant(receiver.value.rem(other.value))
                    is UIntConstant -> UIntConstant(receiver.value.rem(other.value))
                    is ULongConstant -> ULongConstant(receiver.value.rem(other.value))
                }.right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("mod"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType, UnsignedIntegerConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
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
            signature = ComptimeFunctionSignature(listOf(UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                UShortConstant(receiver.value.inc()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("dec"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                UShortConstant(receiver.value.dec()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("and"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType, UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                val other = args[1] as UShortConstant
                UShortConstant(receiver.value.and(other.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("or"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType, UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                val other = args[1] as UShortConstant
                UShortConstant(receiver.value.or(other.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("xor"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType, UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                val other = args[1] as UShortConstant
                UShortConstant(receiver.value.xor(other.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("inv"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                UShortConstant(receiver.value.inv()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toByte"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                ByteConstant(receiver.value.toByte()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toShort"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                ShortConstant(receiver.value.toShort()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toInt"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                IntConstant(receiver.value.toInt()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toLong"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                LongConstant(receiver.value.toLong()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toUByte"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                UByteConstant(receiver.value.toUByte()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toUShort"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                UShortConstant(receiver.value.toUShort()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toUInt"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                UIntConstant(receiver.value.toUInt()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toULong"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                ULongConstant(receiver.value.toULong()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toFloat"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                FloatConstant(receiver.value.toFloat()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toDouble"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                DoubleConstant(receiver.value.toDouble()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toString"),
            signature = ComptimeFunctionSignature(listOf(UShortConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as UShortConstant
                StringConstant(receiver.value.toString()).right()
            }
        )
    )
}