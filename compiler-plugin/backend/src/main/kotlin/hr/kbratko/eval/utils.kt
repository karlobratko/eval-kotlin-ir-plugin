package hr.kbratko.eval

import hr.kbratko.eval.types.BooleanConstant
import hr.kbratko.eval.types.ByteConstant
import hr.kbratko.eval.types.CharConstant
import hr.kbratko.eval.types.ComptimeConstant
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
import org.jetbrains.kotlin.builtins.PrimitiveType
import org.jetbrains.kotlin.builtins.UnsignedType
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.getPrimitiveType
import org.jetbrains.kotlin.ir.types.getUnsignedType
import org.jetbrains.kotlin.ir.types.isNullable
import org.jetbrains.kotlin.ir.types.isPrimitiveType
import org.jetbrains.kotlin.ir.types.isString
import org.jetbrains.kotlin.ir.types.isUnsignedType
import org.jetbrains.kotlin.ir.util.allParameters
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.name.FqName

fun IrFunction.hasAnyOfAnnotations(annotations: List<String>) =
    annotations.any { hasAnnotation(FqName(it)) }

fun IrFunction.nameHasAnyOfPrefixes(prefixes: List<String>) =
    prefixes.any { name.asString().startsWith(it) }

fun IrCall.getAllConstArguments(): List<ComptimeConstant> =
    valueArguments.mapNotNull {
        when (it) {
            is IrConst<*> -> it.toComptimeConstant()
            is IrCall -> it.getComptimeConstant()
            else -> null
        }
    }

fun IrCall.getComptimeConstant(): ComptimeConstant? {
    if (origin != IrStatementOrigin.GET_PROPERTY) return null

    val property = symbol.owner.correspondingPropertySymbol?.owner
    if (property == null || !property.isConst) return null

    val initializer = property.backingField?.initializer?.expression as? IrConst<*>
    return initializer?.toComptimeConstant()
}

fun IrType.isComptimeConstant(): Boolean = isPrimitiveType() || isString()

fun IrFunction.allParameterTypesAreComptimeConstants(): Boolean =
    this.allParameters.all { it.type.isComptimeConstant() }

fun IrCall.returnTypeIsComptimeConstant(): Boolean =
    symbol.owner.returnType.isComptimeConstant()

fun IrConst<*>.toComptimeConstant(): ComptimeConstant? {
    if (this.type.isNullable()) return null

    if (this.type.isPrimitiveType()) {
        if (this.type.isUnsignedType()) {
            return when (this.type.getUnsignedType()) {
                UnsignedType.UBYTE -> UByteConstant(this.value as UByte)
                UnsignedType.USHORT -> UShortConstant(this.value as UShort)
                UnsignedType.UINT -> UIntConstant(this.value as UInt)
                UnsignedType.ULONG -> ULongConstant(this.value as ULong)
                null -> null
            }
        }
        return when (this.type.getPrimitiveType()) {
            PrimitiveType.BOOLEAN -> BooleanConstant(this.value as Boolean)
            PrimitiveType.CHAR -> CharConstant(this.value as Char)
            PrimitiveType.BYTE -> ByteConstant(this.value as Byte)
            PrimitiveType.SHORT -> ShortConstant(this.value as Short)
            PrimitiveType.INT -> IntConstant(this.value as Int)
            PrimitiveType.LONG -> LongConstant(this.value as Long)
            PrimitiveType.FLOAT -> FloatConstant(this.value as Float)
            PrimitiveType.DOUBLE -> DoubleConstant(this.value as Double)
            null -> null
        }
    }
    if (this.type.isString()) return StringConstant(this.value as String)

    return null
}

fun ComptimeConstant.toIrConst(irType: IrType, startOffset: Int, endOffset: Int): IrConst<*> = when (this) {
    is BooleanConstant -> IrConstImpl.boolean(startOffset, endOffset, irType, value)
    is CharConstant -> IrConstImpl.char(startOffset, endOffset, irType, value)
    is ByteConstant -> IrConstImpl.byte(startOffset, endOffset, irType, value)
    is ShortConstant -> IrConstImpl.short(startOffset, endOffset, irType, value)
    is IntConstant -> IrConstImpl.int(startOffset, endOffset, irType, value)
    is LongConstant -> IrConstImpl.long(startOffset, endOffset, irType, value)
    is UByteConstant -> IrConstImpl(startOffset, endOffset, irType, IrConstKind.Byte, value.toByte())
    is UShortConstant -> IrConstImpl.short(startOffset, endOffset, irType, value.toShort())
    is UIntConstant -> IrConstImpl.int(startOffset, endOffset, irType, value.toInt())
    is ULongConstant -> IrConstImpl.long(startOffset, endOffset, irType, value.toLong())
    is FloatConstant -> IrConstImpl.float(startOffset, endOffset, irType, value)
    is DoubleConstant -> IrConstImpl.double(startOffset, endOffset, irType, value)
    is StringConstant -> IrConstImpl.string(startOffset, endOffset, irType, value)
}