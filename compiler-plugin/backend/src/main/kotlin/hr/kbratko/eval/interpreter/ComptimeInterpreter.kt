package hr.kbratko.eval.interpreter

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import hr.kbratko.eval.ComptimeError
import hr.kbratko.eval.ComptimeUnexpectedError
import hr.kbratko.eval.NoEvaluationResult
import hr.kbratko.eval.interpreter.EvaluationOutcome.ControlFlow.EvaluationError
import hr.kbratko.eval.interpreter.EvaluationOutcome.ControlFlow.Return
import hr.kbratko.eval.interpreter.EvaluationOutcome.EvaluationResult.ConstantResult
import hr.kbratko.eval.interpreter.stack.DeclarationScope
import hr.kbratko.eval.interpreter.stack.DeclarationStack
import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.IrBody

class ComptimeInterpreter(
    parameterValues: Map<IrValueDeclaration, ConstantValue<*>>,
    private val body: IrBody
) {
    private val scopeStack = DeclarationStack(
        baseScope = DeclarationScope(parameterValues)
    )

    fun interpret(): Either<ComptimeError, ConstantValue<*>> {
        return try {
            body.interpret(ComptimeInterpreterContext(scopeStack)).let {
                when (it) {
                    is ConstantResult -> it.value.right()
                    is Return -> it.value.right()
                    is EvaluationError -> it.error.left()
                    else -> NoEvaluationResult(body).left()
                }
            }
        } catch (cause: Throwable) {
            ComptimeUnexpectedError(cause).left()
        }
    }
}