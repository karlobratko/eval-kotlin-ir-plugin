package hr.kbratko.eval.interpreter.functions

import hr.kbratko.eval.*
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData

object CharOperatorTests : ShouldSpec({
    withData(
        mapOf(
            "compareTo" to OperatorTest(
                source = """
                fun evalTest(a: Char, b: Char): Int = a.compareTo(b)

                fun main() {
                    val result = evalTest('a', 'b')
                }
                """,
                listOf(
                    IntegerConstantM1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "greater" to OperatorTest(
                source = """
                fun evalTest(a: Char, b: Char): Boolean = a > b

                fun main() {
                    val result = evalTest('a', 'b')
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            ),
            "greaterOrEqual" to OperatorTest(
                source = """
                fun evalTest(a: Char, b: Char): Boolean = a >= b

                fun main() {
                    val result = evalTest('a', 'b')
                }
                """,
                listOf(
                    IntegerConstant0,
                    StoreInteger(0)
                ) atLine 4
            ),
            "less" to OperatorTest(
                source = """
                fun evalTest(a: Char, b: Char): Boolean = a < b

                fun main() {
                    val result = evalTest('a', 'b')
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "lessOrEqual" to OperatorTest(
                source = """
                fun evalTest(a: Char, b: Char): Boolean = a <= b

                fun main() {
                    val result = evalTest('a', 'b')
                }
                """,
                listOf(
                    IntegerConstant1,
                    StoreInteger(0)
                ) atLine 4
            ),
            "plus(Int)" to OperatorTest(
                source = """
                fun evalTest(a: Char, b: Int): Char = a + b

                fun main() {
                    val result = evalTest('a', 1)
                }
                """,
                listOf(
                    PushByte('b'),
                    StoreInteger(0)
                ) atLine 4
            ),
            "plus(String)" to OperatorTest(
                source = """
                fun evalTest(a: Char, b: String): String = a + b

                fun main() {
                    val result = evalTest('a', "b")
                }
                """,
                listOf(
                    LoadConstant("ab"),
                    StoreReference(0)
                ) atLine 4
            ),
            "minus(Char)" to OperatorTest(
                source = """
                fun evalTest(a: Char, b: Char): Int = a - b

                fun main() {
                    val result = evalTest('z', 'a')
                }
                """,
                listOf(
                    PushByte(25),
                    StoreInteger(0)
                ) atLine 4
            ),
            "minus(Int)" to OperatorTest(
                source = """
                fun evalTest(a: Char, b: Int): Char = a - b

                fun main() {
                    val result = evalTest('b', 1)
                }
                """,
                listOf(
                    PushByte('a'),
                    StoreInteger(0)
                ) atLine 4
            ),
            "inc" to OperatorTest(
                source = """
                fun evalTest(a: Char): Char = a.inc()

                fun main() {
                    val result = evalTest('a')
                }
                """,
                listOf(
                    PushByte('b'),
                    StoreInteger(0)
                ) atLine 4
            ),
            "dec" to OperatorTest(
                source = """
                fun evalTest(a: Char): Char = a.dec()

                fun main() {
                    val result = evalTest('b')
                }
                """,
                listOf(
                    PushByte('a'),
                    StoreInteger(0)
                ) atLine 4
            ),
            "toChar" to OperatorTest(
                source = """
                fun evalTest(a: Char): Char = a.toChar()

                fun main() {
                    val result = evalTest('b')
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