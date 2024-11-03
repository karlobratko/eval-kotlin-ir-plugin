package hr.kbratko.eval.interpreter.functions

import hr.kbratko.eval.*
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData

object FloatOperatorTests : ShouldSpec({
    withData(
        mapOf(
            "compareTo" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Float): Int = a.compareTo(b)

                fun main() {
                    val result = evalTest(0f, 1f)
                }
                """,
                listOf(
                    IntegerConstantM1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "greater" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Float): Boolean = a > b

                fun main() {
                    val result = evalTest(0f, 1f)
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            ),
            "greaterOrEqual" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Float): Boolean = a >= b

                fun main() {
                    val result = evalTest(0f, 1f)
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            ),
            "less" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Float): Boolean = a < b

                fun main() {
                    val result = evalTest(0f, 1f)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "lessOrEqual" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Float): Boolean = a <= b

                fun main() {
                    val result = evalTest(0f, 1f)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "plus(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Byte): Float = a + b

                fun main() {
                    val result = evalTest(10f, 11)
                }
                """,
                listOf(
                    LoadConstant(21f),
                    StoreFloat(0)
                ) atLine 4
            ),
            "plus(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Long): Float = a + b

                fun main() {
                    val result = evalTest(10f, 11)
                }
                """,
                listOf(
                    LoadConstant(21f),
                    StoreFloat(0)
                ) atLine 4
            ),
            "plus(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Double): Double = a + b

                fun main() {
                    val result = evalTest(10f, 11.0)
                }
                """,
                listOf(
                    LoadConstant(21.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "minus(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Byte): Float = a - b

                fun main() {
                    val result = evalTest(21f, 11)
                }
                """,
                listOf(
                    LoadConstant(10f),
                    StoreFloat(0)
                ) atLine 4
            ),
            "minus(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Long): Float = a - b

                fun main() {
                    val result = evalTest(21f, 11)
                }
                """,
                listOf(
                    LoadConstant(10f),
                    StoreFloat(0)
                ) atLine 4
            ),
            "minus(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Double): Double = a - b

                fun main() {
                    val result = evalTest(21f, 11.0)
                }
                """,
                listOf(
                    LoadConstant(10.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "times(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Byte): Float = a * b

                fun main() {
                    val result = evalTest(10f, 11)
                }
                """,
                listOf(
                    LoadConstant(110f),
                    StoreFloat(0)
                ) atLine 4
            ),
            "times(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Long): Float = a * b

                fun main() {
                    val result = evalTest(10f, 11)
                }
                """,
                listOf(
                    LoadConstant(110f),
                    StoreFloat(0)
                ) atLine 4
            ),
            "times(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Double): Double = a * b

                fun main() {
                    val result = evalTest(10f, 11.0)
                }
                """,
                listOf(
                    LoadConstant(110.0),
                    StoreDouble(0)
                ) atLine 4
            ),
            "div(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Byte): Float = a / b

                fun main() {
                    val result = evalTest(21f, 10)
                }
                """,
                listOf(
                    LoadConstant(2.1f),
                    StoreFloat(0)
                ) atLine 4
            ),
            "div(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Long): Float = a / b

                fun main() {
                    val result = evalTest(21f, 10)
                }
                """,
                listOf(
                    LoadConstant(2.1f),
                    StoreFloat(0)
                ) atLine 4
            ),
            "div(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Double): Double = a / b

                fun main() {
                    val result = evalTest(21f, 10.0)
                }
                """,
                listOf(
                    LoadConstant(2.1),
                    StoreDouble(0)
                ) atLine 4
            ),
            "rem(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Byte): Float = a.rem(b)

                fun main() {
                    val result = evalTest(21f, 10)
                }
                """,
                listOf(
                    FloatConstant1,
                    StoreFloat(0)
                ) atLine 4
            ),
            "rem(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Long): Float = a.rem(b)

                fun main() {
                    val result = evalTest(21f, 10)
                }
                """,
                listOf(
                    FloatConstant1,
                    StoreFloat(0)
                ) atLine 4
            ),
            "rem(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Double): Double = a.rem(b)

                fun main() {
                    val result = evalTest(21f, 10.0)
                }
                """,
                listOf(
                    DoubleConstant1,
                    StoreDouble(0)
                ) atLine 4
            ),
            "mod(Byte)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Byte): Float = a % b

                fun main() {
                    val result = evalTest(21f, 10)
                }
                """,
                listOf(
                    FloatConstant1,
                    StoreFloat(0)
                ) atLine 4
            ),
            "mod(Long)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Long): Float = a % b

                fun main() {
                    val result = evalTest(21f, 10)
                }
                """,
                listOf(
                    FloatConstant1,
                    StoreFloat(0)
                ) atLine 4
            ),
            "mod(Double)" to OperatorTest(
                source = """
                fun evalTest(a: Float, b: Double): Double = a % b

                fun main() {
                    val result = evalTest(21f, 10.0)
                }
                """,
                listOf(
                    DoubleConstant1,
                    StoreDouble(0)
                ) atLine 4
            ),
            "inc" to OperatorTest(
                source = """
                fun evalTest(a: Float): Float = a.inc()

                fun main() {
                    val result = evalTest(1f)
                }
                """,
                listOf(
                    FloatConstant2,
                    StoreFloat(0)
                ) atLine 4
            ),
            "dec" to OperatorTest(
                source = """
                fun evalTest(a: Float): Float = a.dec()

                fun main() {
                    val result = evalTest(1f)
                }
                """,
                listOf(
                    FloatConstant0,
                    StoreFloat(0)
                ) atLine 4
            ),
            "unaryPlus" to OperatorTest(
                source = """
                fun evalTest(a: Float): Float = +a

                fun main() {
                    val result = evalTest(-1f)
                }
                """,
                listOf(
                    LoadConstant(-1f),
                    StoreFloat(0)
                ) atLine 4
            ),
            "unaryMinus" to OperatorTest(
                source = """
                fun evalTest(a: Float): Float = -a

                fun main() {
                    val result = evalTest(1f)
                }
                """,
                listOf(
                    LoadConstant(-1f),
                    StoreFloat(0)
                ) atLine 4
            ),
            "toInt" to OperatorTest(
                source = """
                fun evalTest(a: Float): Int = a.toInt()

                fun main() {
                    val result = evalTest(1f)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "toLong" to OperatorTest(
                source = """
                fun evalTest(a: Float): Long = a.toLong()

                fun main() {
                    val result = evalTest(1f)
                }
                """,
                listOf(
                    LongConstant1,
                    StoreLong(0)
                ) atLine 4
            ),
            "toFloat" to OperatorTest(
                source = """
                fun evalTest(a: Float): Float = a.toFloat()

                fun main() {
                    val result = evalTest(1f)
                }
                """,
                listOf(
                    FloatConstant1,
                    StoreFloat(0)
                ) atLine 4
            ),
            "toDouble" to OperatorTest(
                source = """
                fun evalTest(a: Float): Double = a.toDouble()

                fun main() {
                    val result = evalTest(1f)
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