package hr.kbratko.eval.interpreter.functions

import hr.kbratko.eval.*
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData

object BooleanOperatorTests : ShouldSpec({
    withData(
        mapOf(
            "compareTo" to OperatorTest(
                source = """
                fun evalTest(a: Boolean, b: Boolean): Int = a.compareTo(b)

                fun main() {
                    val result = evalTest(true, false)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "greater" to OperatorTest(
                source = """
                fun evalTest(a: Boolean, b: Boolean): Boolean = a > b

                fun main() {
                    val result = evalTest(true, false)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "greaterOrEqual" to OperatorTest(
                source = """
                fun evalTest(a: Boolean, b: Boolean): Boolean = a >= b

                fun main() {
                    val result = evalTest(true, false)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "less" to OperatorTest(
                source = """
                fun evalTest(a: Boolean, b: Boolean): Boolean = a < b

                fun main() {
                    val result = evalTest(true, false)
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            ),
            "lessOrEqual" to OperatorTest(
                source = """
                fun evalTest(a: Boolean, b: Boolean): Boolean = a <= b

                fun main() {
                    val result = evalTest(true, false)
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            ),
            "and" to OperatorTest(
                source = """
                fun evalTest(a: Boolean, b: Boolean): Boolean = a && b

                fun main() {
                    val result = evalTest(true, false)
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            ),
            "and" to OperatorTest(
                source = """
                fun evalTest(a: Boolean, b: Boolean): Boolean = a || b

                fun main() {
                    val result = evalTest(true, false)
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "not" to OperatorTest(
                source = """
                fun evalTest(a: Boolean): Boolean = !a

                fun main() {
                    val result = evalTest(true)
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            )
        )
    ) { it.test() }
})