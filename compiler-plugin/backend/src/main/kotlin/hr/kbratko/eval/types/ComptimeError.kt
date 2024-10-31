package hr.kbratko.eval.types

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.util.dump

sealed interface ComptimeError {
    val description: String
}

class ComptimeUnexpectedError(cause: Throwable) : ComptimeError, Throwable(cause) {
    override val description: String = cause.message ?: "Unknown error"
}

sealed interface ComptimeProducedError : ComptimeError

class FunctionBodyNotPresent(element: IrFunction) : ComptimeProducedError {
    override val description = "Function body not present (${element.dump()})"
}

data object EmptyEvaluationOutcome : ComptimeProducedError {
    override val description = "Comptime evaluation produced no result"
}

class ChildElementResultNotPresent(element: IrElement, child: IrElement) : ComptimeProducedError {
    override val description = "Child result not present (${element.dump()}, $child)"
}

class UninitializedVariable(element: IrVariable) : ComptimeProducedError {
    override val description = "Uninitialized variable (${element.dump()})"
}

class VariableNotDeclared(element: IrElement, declaration: IrValueDeclaration) :
    ComptimeProducedError {
    override val description = "Variable not defined (${element.dump()}, $declaration)"
}

class ArgumentsNotConstants(element: IrCall) : ComptimeProducedError {
    override val description = "Not all arguments are constants (${element.dump()})"
}

class ArgumentTypesNotPrimitive(element: IrCall) : ComptimeProducedError {
    override val description = "Not all arguments are primitives (${element.dump()})"
}

class ReturnTypeNotPrimitive(element: IrCall) : ComptimeProducedError {
    override val description = "Return type is not primitive (${element.dump()})"
}

class InsufficientNumberOfEvaluatedArguments(element: IrCall) : ComptimeProducedError {
    override val description = "Insufficient number of arguments (${element.dump()})"
}

class ValueArgumentsCouldNotBeEvaluated(element: IrCall) : ComptimeProducedError {
    override val description = "Arguments could not be evaluated (${element.dump()})"
}

class UnsupportedMethod(methodName: String, arguments: List<ComptimeConstant>) : ComptimeProducedError {
    override val description = "Unsupported method $methodName ($arguments)"
}

class UnsupportedElementType(element: IrElement) : ComptimeProducedError {
    override val description = "Unsupported element type (${element.dump()})"
}

class ValueIsNotComptimeConstant(element: IrElement) : ComptimeProducedError {
    override val description = "Value is not comptime constant (${element.dump()})"
}