package hr.kbratko.eval

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.util.dump

sealed interface ComptimeError {
    val description: String
}

class ComptimeUnexpectedError(cause: Throwable) : ComptimeError, Throwable(cause) {
    override val description: String = cause.message ?: "Unknown error"
}

sealed interface ComptimeProducedError : ComptimeError {
    val element: IrElement
}

class FunctionBodyNotPresent(override val element: IrFunction) : ComptimeProducedError {
    override val description = "Function body not present (${element.dump()})"
}

class NoEvaluationResult(
    override val element: IrBody
) : ComptimeProducedError {
    override val description = "No evaluation result (${element.dump()})"
}

class ChildElementResultNotPresent(
    override val element: IrElement,
    child: IrElement,
) : ComptimeProducedError {
    override val description = "Child result not present (${element.dump()}, $child)"
}

class UninitializedVariable(override val element: IrVariable) : ComptimeProducedError {
    override val description = "Uninitialized variable (${element.dump()})"
}

class VariableNotDeclared(override val element: IrElement, declaration: IrValueDeclaration) :
    ComptimeProducedError {
    override val description = "Variable not defined (${element.dump()}, $declaration)"
}

class ArgumentsNotConstants(override val element: IrCall) : ComptimeProducedError {
    override val description = "Not all arguments are constants (${element.dump()})"
}

class ArgumentTypesNotPrimitive(override val element: IrCall) : ComptimeProducedError {
    override val description = "Not all arguments are primitives (${element.dump()})"
}

class ReturnTypeNotPrimitive(override val element: IrCall) : ComptimeProducedError {
    override val description = "Return type is not primitive (${element.dump()})"
}

class InsufficientNumberOfEvaluatedArguments(override val element: IrCall) : ComptimeProducedError {
    override val description = "Insufficient number of arguments (${element.dump()})"
}

class ValueArgumentsCouldNotBeEvaluated(override val element: IrCall) : ComptimeProducedError {
    override val description = "Arguments could not be evaluated (${element.dump()})"
}

class UnsupportedMethod(override val element: IrCall, methodName: String) : ComptimeProducedError {
    override val description = "Unsupported method $methodName (${element.dump()})"
}

class UnsupportedElementType(override val element: IrElement) : ComptimeProducedError {
    override val description = "Unsupported element type (${element.dump()})"
}