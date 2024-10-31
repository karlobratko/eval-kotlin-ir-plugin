package hr.kbratko.eval.interpreter.functions

import hr.kbratko.eval.types.BooleanConstant
import hr.kbratko.eval.types.ByteConstant
import hr.kbratko.eval.types.CharConstant
import hr.kbratko.eval.types.ComptimeConstant
import hr.kbratko.eval.types.DecimalConstant
import hr.kbratko.eval.types.DoubleConstant
import hr.kbratko.eval.types.FloatConstant
import hr.kbratko.eval.types.IntConstant
import hr.kbratko.eval.types.IntegerConstant
import hr.kbratko.eval.types.LongConstant
import hr.kbratko.eval.types.NumericConstant
import hr.kbratko.eval.types.ShortConstant
import hr.kbratko.eval.types.SignedConstant
import hr.kbratko.eval.types.SignedIntegerConstant
import hr.kbratko.eval.types.StringConstant
import hr.kbratko.eval.types.UByteConstant
import hr.kbratko.eval.types.UIntConstant
import hr.kbratko.eval.types.ULongConstant
import hr.kbratko.eval.types.UShortConstant
import hr.kbratko.eval.types.UnsignedConstant
import hr.kbratko.eval.types.UnsignedIntegerConstant

fun interface ComptimeType {
    fun matches(constant: ComptimeConstant): Boolean
}

val ComptimeConstantType = ComptimeType { true }

val BooleanConstantType = ComptimeType { it is BooleanConstant }

val CharConstantType = ComptimeType { it is CharConstant }

val StringConstantType = ComptimeType { it is StringConstant }

val NumericConstantType = ComptimeType { it is NumericConstant }

val SignedConstantType = ComptimeType { it is SignedConstant }

val IntegerConstantType = ComptimeType { it is IntegerConstant }

val SignedIntegerConstantType = ComptimeType { it is SignedIntegerConstant }

val ByteConstantType = ComptimeType { it is ByteConstant }

val ShortConstantType = ComptimeType { it is ShortConstant }

val IntConstantType = ComptimeType { it is IntConstant }

val LongConstantType = ComptimeType { it is LongConstant }

val UnsignedConstantType = ComptimeType { it is UnsignedConstant }

val UnsignedIntegerConstantType = ComptimeType { it is UnsignedIntegerConstant }

val UByteConstantType = ComptimeType { it is UByteConstant }

val UShortConstantType = ComptimeType { it is UShortConstant }

val UIntConstantType = ComptimeType { it is UIntConstant }

val ULongConstantType = ComptimeType { it is ULongConstant }

val DecimalConstantType = ComptimeType { it is DecimalConstant }

val FloatConstantType = ComptimeType { it is FloatConstant }

val DoubleConstantType = ComptimeType { it is DoubleConstant }