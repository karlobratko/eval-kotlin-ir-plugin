package hr.kbratko.eval.interpreter

import arrow.core.Either
import arrow.core.left
import hr.kbratko.eval.ComptimeConstant
import hr.kbratko.eval.ComptimeError
import hr.kbratko.eval.ComptimeUnexpectedError
import hr.kbratko.eval.FunctionBodyNotPresent
import hr.kbratko.eval.interpreter.stack.DeclarationScope
import hr.kbratko.eval.interpreter.stack.DeclarationStack
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration

fun IrFunction.comptimeCall(arguments: Map<IrValueDeclaration, ComptimeConstant>): Either<ComptimeError, ComptimeConstant> {
    val body = body
    if (body == null) {
        return FunctionBodyNotPresent(this).left()
    }

    return try {
        body.interpret(ComptimeInterpreterContext(DeclarationStack(DeclarationScope(arguments))))
            .toEither()
    } catch (cause: Throwable) {
        ComptimeUnexpectedError(cause).left()
    }
}