package hr.kbratko.eval.interpreter.functions

import hr.kbratko.eval.types.*

fun interface ComptimeTypePredicate {
    fun matches(constant: ComptimeConstant): Boolean
}

val ComptimeConstantType = ComptimeTypePredicate { true }

val BooleanConstantType = ComptimeTypePredicate { it is BooleanConstant }

val CharConstantType = ComptimeTypePredicate { it is CharConstant }

val StringConstantType = ComptimeTypePredicate { it is StringConstant }

val NumericConstantType = ComptimeTypePredicate { it is NumericConstant }

val SignedConstantType = ComptimeTypePredicate { it is SignedConstant }

val IntegerConstantType = ComptimeTypePredicate { it is IntegerConstant }

val SignedIntegerConstantType = ComptimeTypePredicate { it is SignedIntegerConstant }

val ByteConstantType = ComptimeTypePredicate { it is ByteConstant }

val ShortConstantType = ComptimeTypePredicate { it is ShortConstant }

val IntConstantType = ComptimeTypePredicate { it is IntConstant }

val LongConstantType = ComptimeTypePredicate { it is LongConstant }

val UnsignedConstantType = ComptimeTypePredicate { it is UnsignedConstant }

val UnsignedIntegerConstantType = ComptimeTypePredicate { it is UnsignedIntegerConstant }

val UByteConstantType = ComptimeTypePredicate { it is UByteConstant }

val UShortConstantType = ComptimeTypePredicate { it is UShortConstant }

val UIntConstantType = ComptimeTypePredicate { it is UIntConstant }

val ULongConstantType = ComptimeTypePredicate { it is ULongConstant }

val DecimalConstantType = ComptimeTypePredicate { it is DecimalConstant }

val FloatConstantType = ComptimeTypePredicate { it is FloatConstant }

val DoubleConstantType = ComptimeTypePredicate { it is DoubleConstant }