package hr.kbratko.eval

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrCall

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
    override val description = "Function body not present ($element)"
}

class NoEvaluationResult(
    override val element: IrBody
) : ComptimeProducedError {
    override val description = "No evaluation result ($element)"
}

class ChildElementResultNotPresent(
    override val element: IrElement,
    child: IrElement,
) : ComptimeProducedError {
    override val description = "Child result not present ($element, $child)"
}

class UninitializedVariable(override val element: IrVariable) : ComptimeProducedError {
    override val description = "Uninitialized variable ($element)"
}

class VariableNotDeclared(override val element: IrElement, declaration: IrValueDeclaration) :
    ComptimeProducedError {
    override val description = "Variable not defined ($element, $declaration)"
}

class ArgumentsNotConstants(override val element: IrCall) : ComptimeProducedError {
    override val description = "Not all arguments are constants ($element)"
}

class ArgumentTypesNotPrimitive(override val element: IrCall) : ComptimeProducedError {
    override val description = "Not all arguments are primitives ($element)"
}

class ReturnTypeNotPrimitive(override val element: IrCall) : ComptimeProducedError {
    override val description = "Return type is not primitive ($element)"
}

class InsufficientNumberOfEvaluatedArguments(override val element: IrCall) : ComptimeProducedError {
    override val description = "Insufficient number of arguments ($element)"
}

class ValueArgumentsCouldNotBeEvaluated(override val element: IrCall) : ComptimeProducedError {
    override val description = "Arguments could not be evaluated ($element)"
}

class UnsupportedMethod(override val element: IrCall, methodName: String) : ComptimeProducedError {
    override val description = "Unsupported method $methodName ($element)"
}

class UnsupportedElementType(override val element: IrElement) : ComptimeProducedError {
    override val description = "Unsupported element type ($element)"
}