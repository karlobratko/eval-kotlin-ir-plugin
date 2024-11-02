package hr.kbratko.eval.types

sealed interface ComptimeConstant {
    val value: Any
}

data class BooleanConstant(override val value: Boolean) : ComptimeConstant

data class CharConstant(override val value: Char) : ComptimeConstant

data class StringConstant(override val value: String) : ComptimeConstant

sealed interface NumericConstant : ComptimeConstant

sealed interface SignedConstant : NumericConstant

sealed interface IntegerConstant : NumericConstant

sealed interface SignedIntegerConstant : IntegerConstant, SignedConstant

data class ByteConstant(override val value: Byte) : SignedIntegerConstant

data class ShortConstant(override val value: Short) : SignedIntegerConstant

data class IntConstant(override val value: Int) : SignedIntegerConstant

data class LongConstant(override val value: Long) : SignedIntegerConstant

sealed interface UnsignedConstant : NumericConstant

sealed interface UnsignedIntegerConstant : IntegerConstant, UnsignedConstant

data class UByteConstant(override val value: UByte) : UnsignedIntegerConstant

data class UShortConstant(override val value: UShort) : UnsignedIntegerConstant

data class UIntConstant(override val value: UInt) : UnsignedIntegerConstant

data class ULongConstant(override val value: ULong) : UnsignedIntegerConstant

sealed interface DecimalConstant : SignedConstant

data class FloatConstant(override val value: Float) : DecimalConstant

data class DoubleConstant(override val value: Double) : DecimalConstant

fun ComptimeConstant.isConstantTrue() = this is BooleanConstant && this.value

fun ComptimeConstant.isConstantFalse() = this is BooleanConstant && !this.value