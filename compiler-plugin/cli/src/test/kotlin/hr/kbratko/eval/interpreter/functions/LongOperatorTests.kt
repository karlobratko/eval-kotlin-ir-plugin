package hr.kbratko.eval.interpreter.functions

import hr.kbratko.eval.*
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData

object LongOperatorTests : ShouldSpec({
    withData(
        mapOf(
            "compareTo" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Long): Int = a.compareTo(b)

                fun main() {
                    val result = evalTest(0, 1)
                }
                """,
                listOf(
                    IntegerConstantM1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "greater" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Long): Boolean = a > b

                fun main() {
                    val result = evalTest(0, 1)
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            ),
            "greaterOrEqual" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Long): Boolean = a >= b

                fun main() {
                    val result = evalTest(0, 1)
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            ),
            "less" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Long): Boolean = a < b

                fun main() {
                    val result = evalTest(0, 1)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "lessOrEqual" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Long): Boolean = a <= b

                fun main() {
                    val result = evalTest(0, 1)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "plus(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Byte): Long = a + b

                fun main() {
                    val result = evalTest(10, 11)
                }
                """,
                listOf(
                    LoadConstant(21),
                    StoreLong(0)
                ) atLine 4
            ),
            "plus(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Long): Long = a + b

                fun main() {
                    val result = evalTest(10, 11)
                }
                """,
                listOf(
                    LoadConstant(21),
                    StoreLong(0)
                ) atLine 4
            ),
            "plus(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Double): Double = a + b

                fun main() {
                    val result = evalTest(10, 11.0)
                }
                """,
                listOf(
                    LoadConstant(21.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "minus(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Byte): Long = a - b

                fun main() {
                    val result = evalTest(21, 11)
                }
                """,
                listOf(
                    LoadConstant(10),
                    StoreLong(0)
                ) atLine 4
            ),
            "minus(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Long): Long = a - b

                fun main() {
                    val result = evalTest(21, 11)
                }
                """,
                listOf(
                    LoadConstant(10),
                    StoreLong(0)
                ) atLine 4
            ),
            "minus(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Double): Double = a - b

                fun main() {
                    val result = evalTest(21, 11.0)
                }
                """,
                listOf(
                    LoadConstant(10.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "times(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Byte): Long = a * b

                fun main() {
                    val result = evalTest(10, 11)
                }
                """,
                listOf(
                    LoadConstant(110),
                    StoreLong(0)
                ) atLine 4
            ),
            "times(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Long): Long = a * b

                fun main() {
                    val result = evalTest(10, 11)
                }
                """,
                listOf(
                    LoadConstant(110),
                    StoreLong(0)
                ) atLine 4
            ),
            "times(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Double): Double = a * b

                fun main() {
                    val result = evalTest(10, 11.0)
                }
                """,
                listOf(
                    LoadConstant(110.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "div(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Byte): Long = a / b

                fun main() {
                    val result = evalTest(21, 10)
                }
                """,
                listOf(
                    LoadConstant(2),
                    StoreLong(0)
                ) atLine 4
            ),
            "div(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Long): Long = a / b

                fun main() {
                    val result = evalTest(21, 10)
                }
                """,
                listOf(
                    LoadConstant(2),
                    StoreLong(0)
                ) atLine 4
            ),
            "div(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Double): Double = a / b

                fun main() {
                    val result = evalTest(21, 10.0)
                }
                """,
                listOf(
                    LoadConstant(2.1),
                    StoreDouble(0)
                ) atLine 4
            ),
            "rem(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Byte): Long = a.rem(b)

                fun main() {
                    val result = evalTest(21, 10)
                }
                """,
                listOf(
                    LongConstant1,
                    StoreLong(0)
                ) atLine 4
            ),
            "rem(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Long): Long = a.rem(b)

                fun main() {
                    val result = evalTest(21, 10)
                }
                """,
                listOf(
                    LongConstant1,
                    StoreLong(0)
                ) atLine 4
            ),
            "rem(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Double): Double = a.rem(b)

                fun main() {
                    val result = evalTest(21, 10.0)
                }
                """,
                listOf(
                    DoubleConstant1,
                    StoreDouble(0)
                ) atLine 4
            ),
            "mod(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Byte): Long = a % b

                fun main() {
                    val result = evalTest(21, 10)
                }
                """,
                listOf(
                    LongConstant1,
                    StoreLong(0)
                ) atLine 4
            ),
            "mod(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Long): Long = a % b

                fun main() {
                    val result = evalTest(21, 10)
                }
                """,
                listOf(
                    LongConstant1,
                    StoreLong(0)
                ) atLine 4
            ),
            "mod(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Long, b: Double): Double = a % b

                fun main() {
                    val result = evalTest(21, 10.0)
                }
                """,
                listOf(
                    DoubleConstant1,
                    StoreDouble(0)
                ) atLine 4
            ),
            "inc" to OperatorTest(
                source = """
                fun evalTest(a: Long): Long = a.inc()

                fun main() {
                    val result = evalTest(1)
                }
                """,
                listOf(
                    LoadConstant(2),
                    StoreLong(0)
                ) atLine 4
            ),
            "dec" to OperatorTest(
                source = """
                fun evalTest(a: Long): Long = a.dec()

                fun main() {
                    val result = evalTest(1)
                }
                """,
                listOf(
                    LongConstant0,
                    StoreLong(0)
                ) atLine 4
            ),
            "unaryPlus" to OperatorTest(
                source = """
                fun evalTest(a: Long): Long = +a

                fun main() {
                    val result = evalTest(-1)
                }
                """,
                listOf(
                    LoadConstant(-1),
                    StoreLong(0)
                ) atLine 4
            ),
            "unaryMinus" to OperatorTest(
                source = """
                fun evalTest(a: Long): Long = -a

                fun main() {
                    val result = evalTest(1)
                }
                """,
                listOf(
                    LoadConstant(-1),
                    StoreLong(0)
                ) atLine 4
            ),
            "toByte" to OperatorTest(
                source = """
                fun evalTest(a: Long): Byte = a.toByte()

                fun main() {
                    val result = evalTest(1)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "toShort" to OperatorTest(
                source = """
                fun evalTest(a: Long): Short = a.toShort()

                fun main() {
                    val result = evalTest(1)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "toInt" to OperatorTest(
                source = """
                fun evalTest(a: Long): Int = a.toInt()

                fun main() {
                    val result = evalTest(1)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "toLong" to OperatorTest(
                source = """
                fun evalTest(a: Long): Long = a.toLong()

                fun main() {
                    val result = evalTest(1)
                }
                """,
                listOf(
                    LongConstant1,
                    StoreLong(0)
                ) atLine 4
            ),
            "toUByte" to OperatorTest(
                source = """
                fun evalTest(a: Long): UByte = a.toUByte()

                fun main() {
                    val result = evalTest(1)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "toUShort" to OperatorTest(
                source = """
                fun evalTest(a: Long): UShort = a.toUShort()

                fun main() {
                    val result = evalTest(1)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "toUInt" to OperatorTest(
                source = """
                fun evalTest(a: Long): UInt = a.toUInt()

                fun main() {
                    val result = evalTest(1)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "toULong" to OperatorTest(
                source = """
                fun evalTest(a: Long): ULong = a.toULong()

                fun main() {
                    val result = evalTest(1)
                }
                """,
                listOf(
                    LongConstant1,
                    StoreLong(0)
                ) atLine 4
            ),
            "toFloat" to OperatorTest(
                source = """
                fun evalTest(a: Long): Float = a.toFloat()

                fun main() {
                    val result = evalTest(1)
                }
                """,
                listOf(
                    FloatConstant1,
                    StoreFloat(0)
                ) atLine 4
            ),
            "toDouble" to OperatorTest(
                source = """
                fun evalTest(a: Long): Double = a.toDouble()

                fun main() {
                    val result = evalTest(1)
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