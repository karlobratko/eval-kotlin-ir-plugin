package hr.kbratko.eval.interpreter.functions

import hr.kbratko.eval.*
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData

object UShortOperatorTests : ShouldSpec({
    withData(
        mapOf(
            "compareTo" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: UShort): Int = a.compareTo(b)

                fun main() {
                    val result = evalTest(0u, 1u)
                }
                """,
                listOf(
                    IntegerConstantM1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "greater" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: UShort): Boolean = a > b

                fun main() {
                    val result = evalTest(0u, 1u)
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            ),
            "greaterOrEqual" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: UShort): Boolean = a >= b

                fun main() {
                    val result = evalTest(0u, 1u)
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            ),
            "less" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: UShort): Boolean = a < b

                fun main() {
                    val result = evalTest(0u, 1u)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "lessOrEqual" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: UShort): Boolean = a <= b

                fun main() {
                    val result = evalTest(0u, 1u)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "plus(UByte)" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: UByte): UInt = a + b

                fun main() {
                    val result = evalTest(10u, 11u)
                }
                """,
                listOf(
                    PushByte(21),
                    StoreInteger(0)
                ) atLine 4
            ),
            "plus(ULong)" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: ULong): ULong = a + b

                fun main() {
                    val result = evalTest(10u, 11u)
                }
                """,
                listOf(
                    LoadConstant(21L),
                    StoreLong(0)
                ) atLine 4
            ),
            "minus(UByte)" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: UByte): UInt = a - b

                fun main() {
                    val result = evalTest(21u, 11u)
                }
                """,
                listOf(
                    PushByte(10),
                    StoreInteger(0)
                ) atLine 4
            ),
            "minus(ULong)" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: ULong): ULong = a - b

                fun main() {
                    val result = evalTest(21u, 11u)
                }
                """,
                listOf(
                    LoadConstant(10L),
                    StoreLong(0)
                ) atLine 4
            ),
            "times(UByte)" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: UByte): UInt = a * b

                fun main() {
                    val result = evalTest(10u, 11u)
                }
                """,
                listOf(
                    PushByte(110),
                    StoreInteger(0)
                ) atLine 4
            ),
            "times(ULong)" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: ULong): ULong = a * b

                fun main() {
                    val result = evalTest(10u, 11u)
                }
                """,
                listOf(
                    LoadConstant(110L),
                    StoreLong(0)
                ) atLine 4
            ),
            "div(UByte)" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: UByte): UInt = a / b

                fun main() {
                    val result = evalTest(21u, 10u)
                }
                """,
                listOf(
                    IntegerConstant2,
                    StoreInteger(0)
                ) atLine 4
            ),
            "div(ULong)" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: ULong): ULong = a / b

                fun main() {
                    val result = evalTest(21u, 10u)
                }
                """,
                listOf(
                    LoadConstant(2L),
                    StoreLong(0)
                ) atLine 4
            ),
            "rem(UByte)" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: UByte): UInt = a.rem(b)

                fun main() {
                    val result = evalTest(21u, 10u)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "rem(ULong)" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: ULong): ULong = a.rem(b)

                fun main() {
                    val result = evalTest(21u, 10u)
                }
                """,
                listOf(
                    LongConstant1,
                    StoreLong(0)
                ) atLine 4
            ),
            "mod(UByte)" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: UByte): UInt = a % b

                fun main() {
                    val result = evalTest(21u, 10u)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "mod(ULong)" to OperatorTest(
                source = """
                fun evalTest(a: UShort, b: ULong): ULong = a % b

                fun main() {
                    val result = evalTest(21u, 10u)
                }
                """,
                listOf(
                    LongConstant1,
                    StoreLong(0)
                ) atLine 4
            ),
            "inc" to OperatorTest(
                source = """
                fun evalTest(a: UShort): UShort = a.inc()

                fun main() {
                    val result = evalTest(1u)
                }
                """,
                listOf(
                    IntegerConstant2,
                    StoreInteger(0)
                ) atLine 4
            ),
            "dec" to OperatorTest(
                source = """
                fun evalTest(a: UShort): UShort = a.dec()

                fun main() {
                    val result = evalTest(1u)
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            ),
            "toByte" to OperatorTest(
                source = """
                fun evalTest(a: UShort): Byte = a.toByte()

                fun main() {
                    val result = evalTest(1u)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "toShort" to OperatorTest(
                source = """
                fun evalTest(a: UShort): Short = a.toShort()

                fun main() {
                    val result = evalTest(1u)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "toInt" to OperatorTest(
                source = """
                fun evalTest(a: UShort): Int = a.toInt()

                fun main() {
                    val result = evalTest(1u)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "toLong" to OperatorTest(
                source = """
                fun evalTest(a: UShort): Long = a.toLong()

                fun main() {
                    val result = evalTest(1u)
                }
                """,
                listOf(
                    LongConstant1,
                    StoreLong(0)
                ) atLine 4
            ),
            "toUByte" to OperatorTest(
                source = """
                fun evalTest(a: UShort): UByte = a.toUByte()

                fun main() {
                    val result = evalTest(1u)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "toUShort" to OperatorTest(
                source = """
                fun evalTest(a: UShort): UShort = a.toUShort()

                fun main() {
                    val result = evalTest(1u)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "toUInt" to OperatorTest(
                source = """
                fun evalTest(a: UShort): UInt = a.toUInt()

                fun main() {
                    val result = evalTest(1u)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "toULong" to OperatorTest(
                source = """
                fun evalTest(a: UShort): ULong = a.toULong()

                fun main() {
                    val result = evalTest(1u)
                }
                """,
                listOf(
                    LongConstant1,
                    StoreLong(0)
                ) atLine 4
            ),
            "toFloat" to OperatorTest(
                source = """
                fun evalTest(a: UShort): Float = a.toFloat()

                fun main() {
                    val result = evalTest(1u)
                }
                """,
                listOf(
                    FloatConstant1,
                    StoreFloat(0)
                ) atLine 4
            ),
            "toDouble" to OperatorTest(
                source = """
                fun evalTest(a: UShort): Double = a.toDouble()

                fun main() {
                    val result = evalTest(1u)
                }
                """,
                listOf(
                    DoubleConstant1,
                    StoreDouble(0)
                ) atLine 4
            )
        )
    ) { it.test() }
})