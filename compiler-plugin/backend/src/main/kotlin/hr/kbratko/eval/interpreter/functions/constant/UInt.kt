package hr.kbratko.eval.interpreter.functions.constant

import arrow.core.right
import hr.kbratko.eval.interpreter.functions.*
import hr.kbratko.eval.types.*

fun ComptimeFunctionRegistry.registerUIntOperators() {
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("compareTo"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType, UnsignedConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
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

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greater"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType, UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                val other = args[1] as UIntConstant
                BooleanConstant(receiver.value > other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greaterOrEqual"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType, UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                val other = args[1] as UIntConstant
                BooleanConstant(receiver.value >= other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("less"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType, UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                val other = args[1] as UIntConstant
                BooleanConstant(receiver.value < other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("lessOrEqual"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType, UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                val other = args[1] as UIntConstant
                BooleanConstant(receiver.value <= other.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("plus"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType, UnsignedIntegerConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
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

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("minus"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType, UnsignedIntegerConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
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

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("times"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType, UnsignedIntegerConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
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

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("div"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType, UnsignedIntegerConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
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

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("rem"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType, UnsignedIntegerConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
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

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("mod"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType, UnsignedIntegerConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
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

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("inc"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                UIntConstant(receiver.value.inc()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("dec"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                UIntConstant(receiver.value.dec()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("and"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType, UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                val other = args[1] as UIntConstant
                UIntConstant(receiver.value.and(other.value)).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("or"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType, UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                val other = args[1] as UIntConstant
                UIntConstant(receiver.value.or(other.value)).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("xor"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType, UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                val other = args[1] as UIntConstant
                UIntConstant(receiver.value.xor(other.value)).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("inv"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                UIntConstant(receiver.value.inv()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toByte"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                ByteConstant(receiver.value.toByte()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toShort"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                ShortConstant(receiver.value.toShort()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toInt"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                IntConstant(receiver.value.toInt()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toLong"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                LongConstant(receiver.value.toLong()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toUByte"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                UByteConstant(receiver.value.toUByte()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toUShort"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                UShortConstant(receiver.value.toUShort()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toUInt"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                UIntConstant(receiver.value).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toULong"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                ULongConstant(receiver.value.toULong()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toFloat"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                FloatConstant(receiver.value.toFloat()).right()
            }
        )
    )

    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toDouble"),
            signature = ComptimeFunctionSignature(listOf(UIntConstantType)),
            implementation = { args ->
                val receiver = args[0] as UIntConstant
                DoubleConstant(receiver.value.toDouble()).right()
            }
        )
    )
}