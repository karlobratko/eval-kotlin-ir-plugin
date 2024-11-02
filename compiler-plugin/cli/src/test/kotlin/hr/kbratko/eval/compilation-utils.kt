package hr.kbratko.eval

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.SourceFile.Companion.kotlin
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.org.objectweb.asm.ClassReader
import org.jetbrains.org.objectweb.asm.Opcodes
import org.jetbrains.org.objectweb.asm.tree.*
import org.jetbrains.org.objectweb.asm.util.TraceClassVisitor
import java.io.File
import java.io.PrintStream
import java.io.PrintWriter

const val KOTLIN_FILE = "Test.kt"
const val CLASS_FILE = "TestKt.class"

@OptIn(ExperimentalCompilerApi::class)
fun kotlinCompilation(source: SourceFile) =
    KotlinCompilation().apply {
        sources = listOf(source)
        inheritClassPath = true
        compilerPluginRegistrars = listOf(EvalComponentRegistrar())
        commandLineProcessors = listOf(EvalCommandLineProcessor())
    }

@OptIn(ExperimentalCompilerApi::class)
fun KotlinCompilation.Result.getClassFile(name: String) = outputDirectory.resolve(name)

@OptIn(ExperimentalCompilerApi::class)
fun compileIntoClassFile(source: String): File =
    kotlinCompilation(kotlin(KOTLIN_FILE, source)).compile()
        .also { it.exitCode shouldBe ExitCode.OK }
        .getClassFile(CLASS_FILE)
        .also { it.exists().shouldBeTrue() }

fun ClassReader.toClassNode() =
    ClassNode().also {
        this.accept(it, 0)
    }

fun ClassReader.print(stream: PrintStream = System.out) {
    TraceClassVisitor(PrintWriter(stream)).also {
        this.accept(it, 0)
    }
}

fun MethodNode.getInstructionsForLine(targetLine: Int): List<AbstractInsnNode> =
    instructions
        .dropWhile { it !is LineNumberNode || it.line != targetLine }
        .drop(1)
        .takeWhile { it !is LineNumberNode && it !is LabelNode }

fun File.toClassReader() = ClassReader(readBytes())

fun ClassNode.getMainMethod(): MethodNode? =
    methods.find { it.name == "main" && it.desc == "()V" }

typealias Opcode = Int

interface Instruction {
    val opcode: Opcode
}

data class BasicInstruction(override val opcode: Opcode) : Instruction

val IntegerConstantM1 = BasicInstruction(Opcodes.ICONST_M1)
val IntegerConstant0 = BasicInstruction(Opcodes.ICONST_0)
val IntegerConstant1 = BasicInstruction(Opcodes.ICONST_1)
val IntegerConstant2 = BasicInstruction(Opcodes.ICONST_2)
val IntegerConstant3 = BasicInstruction(Opcodes.ICONST_3)
val IntegerConstant4 = BasicInstruction(Opcodes.ICONST_4)
val IntegerConstant5 = BasicInstruction(Opcodes.ICONST_5)

val LongConstant0 = BasicInstruction(Opcodes.LCONST_0)
val LongConstant1 = BasicInstruction(Opcodes.LCONST_1)

val FloatConstant0 = BasicInstruction(Opcodes.FCONST_0)
val FloatConstant1 = BasicInstruction(Opcodes.FCONST_1)
val FloatConstant2 = BasicInstruction(Opcodes.FCONST_2)

val DoubleConstant0 = BasicInstruction(Opcodes.DCONST_0)
val DoubleConstant1 = BasicInstruction(Opcodes.DCONST_1)

data class VariableInstruction(override val opcode: Opcode, val variable: Int) : Instruction

fun StoreInteger(variable: Int) = VariableInstruction(Opcodes.ISTORE, variable)

fun StoreLong(variable: Int) = VariableInstruction(Opcodes.LSTORE, variable)

fun StoreFloat(variable: Int) = VariableInstruction(Opcodes.FSTORE, variable)

fun StoreDouble(variable: Int) = VariableInstruction(Opcodes.DSTORE, variable)

fun StoreReference(variable: Int) = VariableInstruction(Opcodes.ASTORE, variable)

data class IntegerInstruction(override val opcode: Opcode, val value: Int) : Instruction

fun PushByte(value: Byte) = IntegerInstruction(Opcodes.BIPUSH, value.toInt())

fun PushByte(value: Char) = PushByte(value.code.toByte())

fun PushShort(value: Short) = IntegerInstruction(Opcodes.SIPUSH, value.toInt())

data class LoadConstantInstruction(override val opcode: Opcode, val constant: Any) : Instruction

fun LoadConstant(value: String) = LoadConstantInstruction(Opcodes.LDC, value)

fun LoadConstant(value: Int) = LoadConstantInstruction(Opcodes.LDC, value)

fun LoadConstant(value: Long) = LoadConstantInstruction(Opcodes.LDC, value)

fun LoadConstant(value: Float) = LoadConstantInstruction(Opcodes.LDC, value)

fun LoadConstant(value: Double) = LoadConstantInstruction(Opcodes.LDC, value)

private fun AbstractInsnNode.toInstruction(): Instruction =
    when (this) {
        is InsnNode -> BasicInstruction(this.opcode)
        is VarInsnNode -> VariableInstruction(this.opcode, this.`var`)
        is IntInsnNode -> IntegerInstruction(this.opcode, this.operand)
        is LdcInsnNode -> LoadConstantInstruction(this.opcode, this.cst)
        else -> error("Unsupported AbstractInsnNode type: $this")
    }

data class LineInstructions(
    val line: Int,
    val opcodes: List<Instruction>
)

infix fun List<Instruction>.atLine(line: Int) = LineInstructions(line, this)

data class OperatorTest(
    @Language("kotlin") val source: String,
    val instructions: LineInstructions,
    val printBytecode: Boolean = false,
)

fun OperatorTest.test() {
    compileIntoClassFile(source)
        .toClassReader()
        .also { if (printBytecode) it.print() }
        .toClassNode()
        .getMainMethod()
        .shouldNotBeNull()
        .getInstructionsForLine(instructions.line)
        .onEach { if (printBytecode) println(it) }
        .map(AbstractInsnNode::toInstruction)
        .shouldBeEqual(instructions.opcodes)
}