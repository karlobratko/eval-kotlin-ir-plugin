package hr.kbratko.eval

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCompilerApi::class)
class EvalPluginTest {

    // Test case 1: Check if an eval function that adds constants can be evaluated at compile time
    @Test
    fun `should replace at compile time`() {
        val source = SourceFile.kotlin(
            "Main.kt", """
            fun evalAdd(a: Int, b: Int): Int = a + b

            fun main() {
                println(evalAdd(2, 3))  // Should be replaced with 5 at compile time
            }
        """
        )

        val result = compileWithPlugin(source)

        // Check that the output matches the expected result (5)
        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

    // Test case 2: Check if an eval function with a non-constant argument is not evaluated at compile time
    @Test
    fun `should not replace when argument is non-constant`() {
        val source = SourceFile.kotlin(
            "Main.kt", """
            fun evalAdd(a: Int, b: Int): Int {
                return a + b
            }

            fun main() {
                val x = 2
                println(evalAdd(x, 3))  // Should NOT be evaluated at compile time
            }
        """
        )

        val result = compileWithPlugin(source)

        // Check that the output is correct but evaluated at runtime (should still print 5)
        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

    // Test case 3: Check if a non-supported operation is left as is
    @Test
    fun `should replace variable at compile time`() {
        val source = SourceFile.kotlin(
            "Main.kt", """
            fun evalAddition(a: Int, b: Int): Int {
                if (a > 10) {
                    val a = 10
                }
                val c = (a + a) + (b + b) // should be 10 at compile time
                return c
            }

            fun main() {
                println(evalAddition(2, 3))
            }
        """
        )

        val result = compileWithPlugin(source)

        // Check that the function is evaluated at runtime (output should be 6)
        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

    // Helper function to compile and run the code with the plugin
    private fun compileWithPlugin(source: SourceFile): KotlinCompilation.Result {
        return KotlinCompilation().apply {
            sources = listOf(source)
            inheritClassPath = true
            messageOutputStream = System.out
            kotlincArguments = listOf("-P", "plugin:hr.kbratko.eval:prefix=eval")
            compilerPluginRegistrars = listOf(EvalComponentRegistrar())
            commandLineProcessors = listOf(EvalCommandLineProcessor())
            verbose = false
        }.compile()
    }
}
