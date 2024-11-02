package hr.kbratko.eval.interpreter

import arrow.core.Either
import arrow.core.left
import hr.kbratko.eval.interpreter.stack.DeclarationScope
import hr.kbratko.eval.interpreter.stack.DeclarationStack
import hr.kbratko.eval.types.ComptimeConstant
import hr.kbratko.eval.types.ComptimeError
import hr.kbratko.eval.types.ComptimeUnexpectedError
import hr.kbratko.eval.types.FunctionBodyNotPresent
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration

fun IrFunction.executeInComptime(arguments: Map<IrValueDeclaration, ComptimeConstant>): Either<ComptimeError, ComptimeConstant> {
    val body = body ?: return FunctionBodyNotPresent(this).left()

    return try {
        body.interpret(ComptimeInterpreterContext(DeclarationStack(DeclarationScope(arguments))))
            .toEither()
    } catch (cause: Throwable) {
        ComptimeUnexpectedError(cause).left()
    }
}