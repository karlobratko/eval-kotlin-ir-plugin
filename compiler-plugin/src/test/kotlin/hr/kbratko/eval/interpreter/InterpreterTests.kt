package hr.kbratko.eval.interpreter

import hr.kbratko.eval.*
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData

object InterpreterTests : ShouldSpec({
    context("if expression") {
        withData(
            mapOf(
                "as statement" to OperatorTest(
                    source = """
                    fun evalTest(a: Int, b: Int): Int {
                        var max = a
                        if (a < b) max = b
                        return max
                    }
    
                    fun main() {
                        val result = evalTest(10, 11)
                    }
                    """,
                    listOf(
                        PushByte(11),
                        StoreInteger(0)
                    ) atLine 8
                ),
                "with else" to OperatorTest(
                    source = """
                    fun evalTest(a: Int, b: Int): Int {
                        var max = 0
                        if (a > b) {
                            max = a
                        } else {
                            max = b
                        }
                        return max
                    }
    
                    fun main() {
                        val result = evalTest(10, 11)
                    }
                    """,
                    listOf(
                        PushByte(11),
                        StoreInteger(0)
                    ) atLine 12
                ),
                "as expression" to OperatorTest(
                    source = """
                    fun evalTest(a: Int, b: Int): Int {
                        val max = if (a > b) a else b
                        return max
                    }
    
                    fun main() {
                        val result = evalTest(10, 11)
                    }
                    """,
                    listOf(
                        PushByte(11),
                        StoreInteger(0)
                    ) atLine 7
                ),
                "with else if" to OperatorTest(
                    source = """
                    fun evalTest(a: Int, b: Int): Int {
                        val maxLimit = 1
                        val maxOrLimit = if (maxLimit > a) maxLimit else if (a > b) a else b
                        return maxOrLimit
                    }
    
                    fun main() {
                        val result = evalTest(10, 11)
                    }
                    """,
                    listOf(
                        PushByte(11),
                        StoreInteger(0)
                    ) atLine 8
                ),
                "expression with block" to OperatorTest(
                    source = """
                    fun evalTest(a: Int, b: Int): Int {
                        var maxLimit = 1
                        val max = if (a > b) {
                            maxLimit = a
                            a
                        } else {
                            maxLimit = b
                            b
                        }
                        return max
                    }
    
                    fun main() {
                        val result = evalTest(10, 11)
                    }
                    """,
                    listOf(
                        PushByte(11),
                        StoreInteger(0)
                    ) atLine 14
                ),
            )
        ) { it.test() }
    }

    context("when expression") {
        withData(
            mapOf(
                "as statement" to OperatorTest(
                    source = """
                    fun evalTest(a: Int, b: Int): Int {
                        var max = a
                        when {
                            a > b -> max = a
                            else -> max = b
                        }
                        return max
                    }
    
                    fun main() {
                        val result = evalTest(10, 11)
                    }
                    """,
                    listOf(
                        PushByte(11),
                        StoreInteger(0)
                    ) atLine 11
                ),
                "as expression" to OperatorTest(
                    source = """
                    fun evalTest(a: Int): Int {
                        return when (a) {
                            0, 1 -> 1
                            else -> 0
                        }
                    }
    
                    fun main() {
                        val result = evalTest(0)
                    }
                    """,
                    listOf(
                        IntegerConstant1,
                        StoreInteger(0)
                    ) atLine 9
                ),
                "expression with block" to OperatorTest(
                    source = """
                    fun evalTest(a: Int, b: Int): Int {
                        var maxLimit = 1
                        val max = when  {
                            a > b -> {
                                maxLimit = a
                                a
                            }
                            else -> {
                                maxLimit = b
                                b
                            }
                        }
                        return max
                    }
    
                    fun main() {
                        val result = evalTest(10, 11)
                    }
                    """,
                    listOf(
                        PushByte(11),
                        StoreInteger(0)
                    ) atLine 17
                )
            )
        ) { it.test() }
    }

    context("(do) while loop") {
        withData(
            mapOf(
                "simple while" to OperatorTest(
                    source = """
                    fun evalTest(a: Int, b: Int): Int {
                        var i = a
                        while (i < b) {
                            i++
                        }
                        return i
                    }
    
                    fun main() {
                        val result = evalTest(10, 11)
                    }
                    """,
                    listOf(
                        PushByte(11),
                        StoreInteger(0)
                    ) atLine 10
                ),
                "simple do while" to OperatorTest(
                    source = """
                    fun evalTest(a: Int, b: Int): Int {
                        var i = a
                        do {
                            i++
                        } while (i <= b)
                        return i
                    }
    
                    fun main() {
                        val result = evalTest(10, 11)
                    }
                    """,
                    listOf(
                        PushByte(11),
                        StoreInteger(0)
                    ) atLine 10
                )
            )
        ) { it.test() }
    }

    context("returns and jumps") {
        withData(
            mapOf(
                "return" to OperatorTest(
                    source = """
                    fun evalTest(a: Int, b: Int): Int {
                        var i = 0
                        while (true) {
                            if (i == 10) return i
                            i++
                        }
                        return i
                    }
    
                    fun main() {
                        val result = evalTest(10, 11)
                    }
                    """,
                    listOf(
                        PushByte(10),
                        StoreInteger(0)
                    ) atLine 11
                ),
                "break" to OperatorTest(
                    source = """
                    fun evalTest(a: Int, b: Int): Int {
                        var i = 0
                        while (true) {
                            if (i == 10) break
                            i++
                        }
                        return i
                    }
    
                    fun main() {
                        val result = evalTest(10, 11)
                    }
                    """,
                    listOf(
                        PushByte(10),
                        StoreInteger(0)
                    ) atLine 11
                ),
                "break with label" to OperatorTest(
                    source = """
                    fun evalTest(a: Int, b: Int): Int {
                        var i = 0
                        outer@ while (true) {
                            var j = 0
                            while (j < 5) {
                                if (i == 10) break@outer
                                j++
                            }
                            i++
                        }
                        return i
                    }
    
                    fun main() {
                        val result = evalTest(10, 11)
                    }
                    """,
                    listOf(
                        PushByte(10),
                        StoreInteger(0)
                    ) atLine 15
                ),
                "continue" to OperatorTest(
                    source = """
                    fun evalTest(a: Int, b: Int): Int {
                        var i = 0
                        while (true) {
                            i++
                            if (i < 10) continue
                            break
                        }
                        return i
                    }
    
                    fun main() {
                        val result = evalTest(10, 11)
                    }
                    """,
                    listOf(
                        PushByte(10),
                        StoreInteger(0)
                    ) atLine 12
                ),
                "continue with label" to OperatorTest(
                    source = """
                    fun evalTest(a: Int, b: Int): Int {
                        var i = 0
                        outer@ while (true) {
                            i++
                            var j = 0
                            while (j < 5) {
                                j++
                                if (i < 10) continue@outer
                            }
                            break
                        }
                        return i
                    }
    
                    fun main() {
                        val result = evalTest(10, 11)
                    }
                    """,
                    listOf(
                        PushByte(10),
                        StoreInteger(0)
                    ) atLine 16
                )
            )
        ) { it.test() }
    }
})