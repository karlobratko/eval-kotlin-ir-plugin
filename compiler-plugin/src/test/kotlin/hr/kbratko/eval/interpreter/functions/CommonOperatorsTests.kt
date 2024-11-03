package hr.kbratko.eval.interpreter.functions

import hr.kbratko.eval.*
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData

object CommonOperatorTests : ShouldSpec({
    context("equals") {
        withData(
            mapOf(
                "Boolean, Boolean" to OperatorTest(
                    source = """
                    fun evalTest(a: Boolean, b: Boolean): Boolean = a == b
    
                    fun main() {
                        val result = evalTest(true, true)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "Char, Char" to OperatorTest(
                    source = """
                    fun evalTest(a: Char, b: Char): Boolean = a == b
    
                    fun main() {
                        val result = evalTest('b', 'b')
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "String, String" to OperatorTest(
                    source = """
                    fun evalTest(a: String, b: String): Boolean = a == b
    
                    fun main() {
                        val result = evalTest("b", "b")
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "Byte, Byte" to OperatorTest(
                    source = """
                    fun evalTest(a: Byte, b: Byte): Boolean = a == b
    
                    fun main() {
                        val result = evalTest(1, 1)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "Short, Short" to OperatorTest(
                    source = """
                    fun evalTest(a: Short, b: Short): Boolean = a == b
    
                    fun main() {
                        val result = evalTest(1, 1)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "Int, Int" to OperatorTest(
                    source = """
                    fun evalTest(a: Int, b: Int): Boolean = a == b
    
                    fun main() {
                        val result = evalTest(1, 1)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "Long, Long" to OperatorTest(
                    source = """
                    fun evalTest(a: Long, b: Long): Boolean = a == b
    
                    fun main() {
                        val result = evalTest(1, 1)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "UByte, UByte" to OperatorTest(
                    source = """
                    fun evalTest(a: UByte, b: UByte): Boolean = a == b
    
                    fun main() {
                        val result = evalTest(1u, 1u)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "UShort, UShort" to OperatorTest(
                    source = """
                    fun evalTest(a: UShort, b: UShort): Boolean = a == b
    
                    fun main() {
                        val result = evalTest(1u, 1u)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "UInt, UInt" to OperatorTest(
                    source = """
                    fun evalTest(a: UInt, b: UInt): Boolean = a == b
    
                    fun main() {
                        val result = evalTest(1u, 1u)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "ULong, ULong" to OperatorTest(
                    source = """
                    fun evalTest(a: ULong, b: ULong): Boolean = a == b
    
                    fun main() {
                        val result = evalTest(1u, 1u)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "Float, Float" to OperatorTest(
                    source = """
                    fun evalTest(a: Float, b: Float): Boolean = a == b
    
                    fun main() {
                        val result = evalTest(1.0f, 1.0f)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "Double, Double" to OperatorTest(
                    source = """
                    fun evalTest(a: Double, b: Double): Boolean = a == b
    
                    fun main() {
                        val result = evalTest(1.0, 1.0)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                )
            )
        ) { it.test() }
    }

    context("hashCode") {
        withData(
            mapOf(
                "Boolean" to OperatorTest(
                    source = """
                    fun evalTest(a: Boolean): Int = a.hashCode()
    
                    fun main() {
                        val result = evalTest(true)
                    }
                    """,
                    listOf(
                        PushShort(1231),
                        StoreInteger(0)
                    ) atLine 4
                ),
                "Char" to OperatorTest(
                    source = """
                    fun evalTest(a: Char): Int = a.hashCode()
    
                    fun main() {
                        val result = evalTest('a')
                    }
                    """,
                    listOf(
                        PushByte('a'),
                        StoreInteger(0)
                    ) atLine 4
                ),
                "String" to OperatorTest(
                    source = """
                    fun evalTest(a: String): Int = a.hashCode()
    
                    fun main() {
                        val result = evalTest("a")
                    }
                    """,
                    listOf(
                        PushByte(97),
                        StoreInteger(0)
                    ) atLine 4
                ),
                "Byte" to OperatorTest(
                    source = """
                    fun evalTest(a: Byte): Int = a.hashCode()
    
                    fun main() {
                        val result = evalTest(1)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "Short" to OperatorTest(
                    source = """
                    fun evalTest(a: Short): Int = a.hashCode()
    
                    fun main() {
                        val result = evalTest(1)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "Int" to OperatorTest(
                    source = """
                    fun evalTest(a: Int): Int = a.hashCode()
    
                    fun main() {
                        val result = evalTest(1)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "Long" to OperatorTest(
                    source = """
                    fun evalTest(a: Long): Int = a.hashCode()
    
                    fun main() {
                        val result = evalTest(1)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "UByte" to OperatorTest(
                    source = """
                    fun evalTest(a: UByte): Int = a.hashCode()
    
                    fun main() {
                        val result = evalTest(1u)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "UShort" to OperatorTest(
                    source = """
                    fun evalTest(a: UShort): Int = a.hashCode()
    
                    fun main() {
                        val result = evalTest(1u)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "UInt" to OperatorTest(
                    source = """
                    fun evalTest(a: UInt): Int = a.hashCode()
    
                    fun main() {
                        val result = evalTest(1u)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "ULong" to OperatorTest(
                    source = """
                    fun evalTest(a: ULong): Int = a.hashCode()
    
                    fun main() {
                        val result = evalTest(1u)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 4
                ),
                "Float" to OperatorTest(
                    source = """
                    fun evalTest(a: Float): Int = a.hashCode()
    
                    fun main() {
                        val result = evalTest(1.0f)
                    }
                    """,
                    listOf(
                        LoadConstant(1065353216),
                        StoreInteger(0)
                    ) atLine 4,
                    printBytecode = true
                ),
                "Double" to OperatorTest(
                    source = """
                    fun evalTest(a: Double): Int = a.hashCode()
    
                    fun main() {
                        val result = evalTest(1.0)
                    }
                    """,
                    listOf(
                        LoadConstant(1072693248),
                        StoreInteger(0)
                    ) atLine 4,
                    printBytecode = true
                )
            )
        ) { it.test() }
    }

    context("toString") {
        withData(
            mapOf(
                "Boolean" to OperatorTest(
                    source = """
                    fun evalTest(a: Boolean): String = a.toString()
    
                    fun main() {
                        val result = evalTest(true)
                    }
                    """,
                    listOf(
                        LoadConstant("true"),
                        StoreReference(0)
                    ) atLine 4
                ),
                "Char" to OperatorTest(
                    source = """
                    fun evalTest(a: Char): String = a.toString()
    
                    fun main() {
                        val result = evalTest('b')
                    }
                    """,
                    listOf(
                        LoadConstant("b"),
                        StoreReference(0)
                    ) atLine 4
                ),
                "String" to OperatorTest(
                    source = """
                    fun evalTest(a: String): String = a.toString()
    
                    fun main() {
                        val result = evalTest("b")
                    }
                    """,
                    listOf(
                        LoadConstant("b"),
                        StoreReference(0)
                    ) atLine 4
                ),
                "Byte" to OperatorTest(
                    source = """
                    fun evalTest(a: Byte): String = a.toString()
    
                    fun main() {
                        val result = evalTest(1)
                    }
                    """,
                    listOf(
                        LoadConstant("1"),
                        StoreReference(0)
                    ) atLine 4
                ),
                "Short" to OperatorTest(
                    source = """
                    fun evalTest(a: Short): String = a.toString()
    
                    fun main() {
                        val result = evalTest(1)
                    }
                    """,
                    listOf(
                        LoadConstant("1"),
                        StoreReference(0)
                    ) atLine 4
                ),
                "Int" to OperatorTest(
                    source = """
                    fun evalTest(a: Int): String = a.toString()
    
                    fun main() {
                        val result = evalTest(1)
                    }
                    """,
                    listOf(
                        LoadConstant("1"),
                        StoreReference(0)
                    ) atLine 4
                ),
                "Long" to OperatorTest(
                    source = """
                    fun evalTest(a: Long): String = a.toString()
    
                    fun main() {
                        val result = evalTest(1)
                    }
                    """,
                    listOf(
                        LoadConstant("1"),
                        StoreReference(0)
                    ) atLine 4
                ),
                "UByte" to OperatorTest(
                    source = """
                    fun evalTest(a: UByte): String = a.toString()
    
                    fun main() {
                        val result = evalTest(1u)
                    }
                    """,
                    listOf(
                        LoadConstant("1"),
                        StoreReference(0)
                    ) atLine 4
                ),
                "UShort" to OperatorTest(
                    source = """
                    fun evalTest(a: UShort): String = a.toString()
    
                    fun main() {
                        val result = evalTest(1u)
                    }
                    """,
                    listOf(
                        LoadConstant("1"),
                        StoreReference(0)
                    ) atLine 4
                ),
                "UInt" to OperatorTest(
                    source = """
                    fun evalTest(a: UInt): String = a.toString()
    
                    fun main() {
                        val result = evalTest(1u)
                    }
                    """,
                    listOf(
                        LoadConstant("1"),
                        StoreReference(0)
                    ) atLine 4
                ),
                "ULong" to OperatorTest(
                    source = """
                    fun evalTest(a: ULong): String = a.toString()
    
                    fun main() {
                        val result = evalTest(1u)
                    }
                    """,
                    listOf(
                        LoadConstant("1"),
                        StoreReference(0)
                    ) atLine 4
                ),
                "Float" to OperatorTest(
                    source = """
                    fun evalTest(a: Float): String = a.toString()
    
                    fun main() {
                        val result = evalTest(1.0f)
                    }
                    """,
                    listOf(
                        LoadConstant("1.0"),
                        StoreReference(0)
                    ) atLine 4
                ),
                "Double" to OperatorTest(
                    source = """
                    fun evalTest(a: Double): String = a.toString()
    
                    fun main() {
                        val result = evalTest(1.0)
                    }
                    """,
                    listOf(
                        LoadConstant("1.0"),
                        StoreReference(0)
                    ) atLine 4
                )
            )
        ) { it.test() }
    }
})