package hr.kbratko.eval.interpreter

import hr.kbratko.eval.BooleanConstant
import hr.kbratko.eval.ByteConstant
import hr.kbratko.eval.CharConstant
import hr.kbratko.eval.ComptimeConstant
import hr.kbratko.eval.DecimalConstant
import hr.kbratko.eval.DoubleConstant
import hr.kbratko.eval.FloatConstant
import hr.kbratko.eval.IntConstant
import hr.kbratko.eval.IntegerConstant
import hr.kbratko.eval.LongConstant
import hr.kbratko.eval.NumericConstant
import hr.kbratko.eval.ShortConstant
import hr.kbratko.eval.StringConstant
import hr.kbratko.eval.UByteConstant
import hr.kbratko.eval.UIntConstant
import hr.kbratko.eval.ULongConstant
import hr.kbratko.eval.UShortConstant
import hr.kbratko.eval.UnsignedConstant

fun interface ComptimeType {
    fun matches(constant: ComptimeConstant): Boolean
}

val ComptimeConstantType = ComptimeType { true }

val BooleanConstantType = ComptimeType { it is BooleanConstant }

val CharConstantType = ComptimeType { it is CharConstant }

val StringConstantType = ComptimeType { it is StringConstant }

val NumericConstantType = ComptimeType { it is NumericConstant }

val IntegerConstantType = ComptimeType { it is IntegerConstant }

val ByteConstantType = ComptimeType { it is ByteConstant }

val ShortConstantType = ComptimeType { it is ShortConstant }

val IntConstantType = ComptimeType { it is IntConstant }

val LongConstantType = ComptimeType { it is LongConstant }

val UnsignedConstantType = ComptimeType { it is UnsignedConstant }

val UByteConstantType = ComptimeType { it is UByteConstant }

val UShortConstantType = ComptimeType { it is UShortConstant }

val UIntConstantType = ComptimeType { it is UIntConstant }

val ULongConstantType = ComptimeType { it is ULongConstant }

val DecimalConstantType = ComptimeType { it is DecimalConstant }

val FloatConstantType = ComptimeType { it is FloatConstant }

val DoubleConstantType = ComptimeType { it is DoubleConstant }