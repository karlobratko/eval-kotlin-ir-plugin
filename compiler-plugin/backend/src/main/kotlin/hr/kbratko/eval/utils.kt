package hr.kbratko.eval

import org.jetbrains.kotlin.constant.BooleanValue
import org.jetbrains.kotlin.constant.ByteValue
import org.jetbrains.kotlin.constant.CharValue
import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.constant.DoubleValue
import org.jetbrains.kotlin.constant.FloatValue
import org.jetbrains.kotlin.constant.IntValue
import org.jetbrains.kotlin.constant.LongValue
import org.jetbrains.kotlin.constant.NullValue
import org.jetbrains.kotlin.constant.ShortValue
import org.jetbrains.kotlin.constant.StringValue
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.name.FqName

fun IrFunction.hasAnyOfAnnotations(annotations: List<String>) =
    annotations.any { hasAnnotation(FqName(it)) }

fun IrFunction.nameHasAnyOfPrefixes(prefixes: List<String>) =
    prefixes.any { name.asString().startsWith(it) }

fun IrCall.areAllArgumentsIrConst(): Boolean =
    valueArguments.filterIsInstance<IrConst<*>>().size == valueArgumentsCount

@Suppress("UNCHECKED_CAST")
fun <T> IrConst<T>.toConstantValue(): ConstantValue<T> = when (this.kind) {
    IrConstKind.Boolean -> BooleanValue(this.value as Boolean)
    IrConstKind.Byte -> ByteValue(this.value as Byte)
    IrConstKind.Int -> IntValue(this.value as Int)
    IrConstKind.Short -> ShortValue(this.value as Short)
    IrConstKind.Long -> LongValue(this.value as Long)
    IrConstKind.Float -> FloatValue(this.value as Float)
    IrConstKind.Double -> DoubleValue(this.value as Double)
    IrConstKind.Char -> CharValue(this.value as Char)
    IrConstKind.String -> StringValue(this.value as String)
    IrConstKind.Null -> NullValue
} as ConstantValue<T>

fun <T> ConstantValue<T>.toIrConst(irType: IrType, startOffset: Int, endOffset: Int) = when (this) {
    is BooleanValue -> IrConstImpl.boolean(startOffset, endOffset, irType, value)
    is ByteValue -> IrConstImpl.byte(startOffset, endOffset, irType, value)
    is IntValue -> IrConstImpl.int(startOffset, endOffset, irType, value)
    is ShortValue -> IrConstImpl.short(startOffset, endOffset, irType, value)
    is LongValue -> IrConstImpl.long(startOffset, endOffset, irType, value)
    is FloatValue -> IrConstImpl.float(startOffset, endOffset, irType, value)
    is DoubleValue -> IrConstImpl.double(startOffset, endOffset, irType, value)
    is CharValue -> IrConstImpl.char(startOffset, endOffset, irType, value)
    is StringValue -> IrConstImpl.string(startOffset, endOffset, irType, value)
    NullValue -> IrConstImpl.constNull(startOffset, endOffset, irType)
    else -> error("Unsupported constant $this")
}