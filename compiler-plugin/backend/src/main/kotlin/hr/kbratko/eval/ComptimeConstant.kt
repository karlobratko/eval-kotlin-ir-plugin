package hr.kbratko.eval

sealed interface ComptimeConstant {
    val value: Any
}

fun ComptimeConstant.isConstantTrue() = this is BooleanConstant && this.value == true

fun ComptimeConstant.isConstantFalse() = this is BooleanConstant && this.value == false

data class BooleanConstant(override val value: Boolean) : ComptimeConstant

data class CharConstant(override val value: Char) : ComptimeConstant

data class StringConstant(override val value: String) : ComptimeConstant

sealed interface NumericConstant : ComptimeConstant

sealed interface IntegerConstant : NumericConstant

data class ByteConstant(override val value: Byte) : IntegerConstant

data class ShortConstant(override val value: Short) : IntegerConstant

data class IntConstant(override val value: Int) : IntegerConstant

data class LongConstant(override val value: Long) : IntegerConstant

sealed interface UnsignedConstant : NumericConstant

data class UByteConstant(override val value: UByte) : UnsignedConstant

data class UShortConstant(override val value: UShort) : UnsignedConstant

data class UIntConstant(override val value: UInt) : UnsignedConstant

data class ULongConstant(override val value: ULong) : UnsignedConstant

sealed interface DecimalConstant : NumericConstant

data class FloatConstant(override val value: Float) : DecimalConstant

data class DoubleConstant(override val value: Double) : DecimalConstant
