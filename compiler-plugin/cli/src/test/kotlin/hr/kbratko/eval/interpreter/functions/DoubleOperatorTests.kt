package hr.kbratko.eval.interpreter.functions

import hr.kbratko.eval.*
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData

object DoubleOperatorTests : ShouldSpec({
    withData(
        mapOf(
            "compareTo" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Double): Int = a.compareTo(b)

                fun main() {
                    val result = evalTest(0.0, 1.0)
                }
                """,
                listOf(
                    IntegerConstantM1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "greater" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Double): Boolean = a > b

                fun main() {
                    val result = evalTest(0.0, 1.0)
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            ),
            "greaterOrEqual" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Double): Boolean = a >= b

                fun main() {
                    val result = evalTest(0.0, 1.0)
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            ),
            "less" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Double): Boolean = a < b

                fun main() {
                    val result = evalTest(0.0, 1.0)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "lessOrEqual" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Double): Boolean = a <= b

                fun main() {
                    val result = evalTest(0.0, 1.0)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "plus(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Byte): Double = a + b

                fun main() {
                    val result = evalTest(10.0, 11)
                }
                """,
                listOf(
                    LoadConstant(21.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "plus(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Long): Double = a + b

                fun main() {
                    val result = evalTest(10.0, 11)
                }
                """,
                listOf(
                    LoadConstant(21.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "plus(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Double): Double = a + b

                fun main() {
                    val result = evalTest(10.0, 11.0)
                }
                """,
                listOf(
                    LoadConstant(21.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "minus(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Byte): Double = a - b

                fun main() {
                    val result = evalTest(21.0, 11)
                }
                """,
                listOf(
                    LoadConstant(10.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "minus(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Long): Double = a - b

                fun main() {
                    val result = evalTest(21.0, 11)
                }
                """,
                listOf(
                    LoadConstant(10.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "minus(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Double): Double = a - b

                fun main() {
                    val result = evalTest(21.0, 11.0)
                }
                """,
                listOf(
                    LoadConstant(10.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "times(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Byte): Double = a * b

                fun main() {
                    val result = evalTest(10.0, 11)
                }
                """,
                listOf(
                    LoadConstant(110.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "times(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Long): Double = a * b

                fun main() {
                    val result = evalTest(10.0, 11)
                }
                """,
                listOf(
                    LoadConstant(110.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "times(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Double): Double = a * b

                fun main() {
                    val result = evalTest(10.0, 11.0)
                }
                """,
                listOf(
                    LoadConstant(110.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "div(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Byte): Double = a / b

                fun main() {
                    val result = evalTest(21.0, 10)
                }
                """,
                listOf(
                    LoadConstant(2.1),
                    StoreDouble(0)
                ) atLine 4
            ),
            "div(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Long): Double = a / b

                fun main() {
                    val result = evalTest(21.0, 10)
                }
                """,
                listOf(
                    LoadConstant(2.1),
                    StoreDouble(0)
                ) atLine 4
            ),
            "div(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Double): Double = a / b

                fun main() {
                    val result = evalTest(21.0, 10.0)
                }
                """,
                listOf(
                    LoadConstant(2.1),
                    StoreDouble(0)
                ) atLine 4
            ),
            "rem(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Byte): Double = a.rem(b)

                fun main() {
                    val result = evalTest(21.0, 10)
                }
                """,
                listOf(
                    DoubleConstant1,
                    StoreDouble(0)
                ) atLine 4
            ),
            "rem(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Long): Double = a.rem(b)

                fun main() {
                    val result = evalTest(21.0, 10)
                }
                """,
                listOf(
                    DoubleConstant1,
                    StoreDouble(0)
                ) atLine 4
            ),
            "rem(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Double): Double = a.rem(b)

                fun main() {
                    val result = evalTest(21.0, 10.0)
                }
                """,
                listOf(
                    DoubleConstant1,
                    StoreDouble(0)
                ) atLine 4
            ),
            "mod(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Byte): Double = a % b

                fun main() {
                    val result = evalTest(21.0, 10)
                }
                """,
                listOf(
                    DoubleConstant1,
                    StoreDouble(0)
                ) atLine 4
            ),
            "mod(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Long): Double = a % b

                fun main() {
                    val result = evalTest(21.0, 10)
                }
                """,
                listOf(
                    DoubleConstant1,
                    StoreDouble(0)
                ) atLine 4
            ),
            "mod(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Double, b: Double): Double = a % b

                fun main() {
                    val result = evalTest(21.0, 10.0)
                }
                """,
                listOf(
                    DoubleConstant1,
                    StoreDouble(0)
                ) atLine 4
            ),
            "inc" to OperatorTest(
                source = """
                fun evalTest(a: Double): Double = a.inc()

                fun main() {
                    val result = evalTest(1.0)
                }
                """,
                listOf(
                    LoadConstant(2.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "dec" to OperatorTest(
                source = """
                fun evalTest(a: Double): Double = a.dec()

                fun main() {
                    val result = evalTest(1.0)
                }
                """,
                listOf(
                    DoubleConstant0,
                    StoreDouble(0)
                ) atLine 4
            ),
            "unaryPlus" to OperatorTest(
                source = """
                fun evalTest(a: Double): Double = +a

                fun main() {
                    val result = evalTest(-1.0)
                }
                """,
                listOf(
                    LoadConstant(-1.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "unaryMinus" to OperatorTest(
                source = """
                fun evalTest(a: Double): Double = -a

                fun main() {
                    val result = evalTest(1.0)
                }
                """,
                listOf(
                    LoadConstant(-1.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "toInt" to OperatorTest(
                source = """
                fun evalTest(a: Double): Int = a.toInt()

                fun main() {
                    val result = evalTest(1.0)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "toLong" to OperatorTest(
                source = """
                fun evalTest(a: Double): Long = a.toLong()

                fun main() {
                    val result = evalTest(1.0)
                }
                """,
                listOf(
                    LongConstant1,
                    StoreLong(0)
                ) atLine 4
            ),
            "toFloat" to OperatorTest(
                source = """
                fun evalTest(a: Double): Float = a.toFloat()

                fun main() {
                    val result = evalTest(1.0)
                }
                """,
                listOf(
                    FloatConstant1,
                    StoreFloat(0)
                ) atLine 4
            ),
            "toDouble" to OperatorTest(
                source = """
                fun evalTest(a: Double): Double = a.toDouble()

                fun main() {
                    val result = evalTest(1.0)
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