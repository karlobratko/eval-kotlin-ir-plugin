package hr.kbratko.eval.interpreter.functions

import hr.kbratko.eval.*
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData

object StringOperatorTests : ShouldSpec({
    withData(
        mapOf(
            "compareTo" to OperatorTest(
                source = """
                fun evalTest(a: String, b: String): Int = a.compareTo(b)

                fun main() {
                    val result = evalTest("a", "b")
                }
                """,
                listOf(
                    IntegerConstantM1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "greater" to OperatorTest(
                source = """
                fun evalTest(a: String, b: String): Boolean = a > b

                fun main() {
                    val result = evalTest("a", "b")
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            ),
            "greaterOrEqual" to OperatorTest(
                source = """
                fun evalTest(a: String, b: String): Boolean = a >= b

                fun main() {
                    val result = evalTest("a", "b")
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            ),
            "less" to OperatorTest(
                source = """
                fun evalTest(a: String, b: String): Boolean = a < b

                fun main() {
                    val result = evalTest("a", "b")
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "lessOrEqual" to OperatorTest(
                source = """
                fun evalTest(a: String, b: String): Boolean = a <= b

                fun main() {
                    val result = evalTest("a", "b")
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "plus(String)" to OperatorTest(
                source = """
                fun evalTest(a: String, b: String): String = a + b

                fun main() {
                    val result = evalTest("a", "b")
                }
                """,
                listOf(
                    LoadConstant("ab"),
                    StoreReference(0)
                ) atLine 4
            ),
            "length" to OperatorTest(
                source = """
                fun evalTest(a: String): Int = a.length

                fun main() {
                    val result = evalTest("a")
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "get" to OperatorTest(
                source = """
                fun evalTest(a: String, b: Int): Char = a[b]

                fun main() {
                    val result = evalTest("ab", 1)
                }
                """,
                listOf(
                    PushByte('b'),
                    StoreInteger(0)
                ) atLine 4
            )
        )
    ) { it.test() }
})