package hr.kbratko.eval.interpreter.functions

import arrow.core.Either
import arrow.core.left
import hr.kbratko.eval.interpreter.functions.constant.registerBooleanOperators
import hr.kbratko.eval.interpreter.functions.constant.registerByteOperators
import hr.kbratko.eval.interpreter.functions.constant.registerCharOperators
import hr.kbratko.eval.interpreter.functions.constant.registerDoubleOperators
import hr.kbratko.eval.interpreter.functions.constant.registerEquals
import hr.kbratko.eval.interpreter.functions.constant.registerFloatOperators
import hr.kbratko.eval.interpreter.functions.constant.registerIntOperators
import hr.kbratko.eval.interpreter.functions.constant.registerLongOperators
import hr.kbratko.eval.interpreter.functions.constant.registerReferenceEquals
import hr.kbratko.eval.interpreter.functions.constant.registerShortOperators
import hr.kbratko.eval.interpreter.functions.constant.registerStringOperators
import hr.kbratko.eval.interpreter.functions.constant.registerUByteOperators
import hr.kbratko.eval.interpreter.functions.constant.registerUIntOperators
import hr.kbratko.eval.interpreter.functions.constant.registerULongOperators
import hr.kbratko.eval.interpreter.functions.constant.registerUShortOperators
import hr.kbratko.eval.types.ComptimeConstant
import hr.kbratko.eval.types.ComptimeError
import hr.kbratko.eval.types.UnsupportedMethod

class ComptimeFunctionRegistry {
    constructor(body: ComptimeFunctionRegistry.() -> Unit) {
        this.body()
    }

    private val operations: MutableMap<String, MutableMap<ComptimeFunctionSignature, ComptimeFunction>> = mutableMapOf()

    fun register(vararg comptimeFunctions: ComptimeFunctionDefinition) {
        comptimeFunctions.forEach { (name, parameterTypes, operation) ->
            operations.computeIfAbsent(name.value) { mutableMapOf() }[parameterTypes] = operation
        }
    }

    fun execute(name: String, arguments: List<ComptimeConstant>): Either<ComptimeError, ComptimeConstant> =
        operations[name]
            ?.firstNotNullOfOrNull { (signature, function) ->
                if (signature.argumentTypes.size == arguments.size &&
                    signature.argumentTypes.zip(arguments).all { (type, constant) -> type.matches(constant) }
                ) function
                else null
            }
            ?.invoke(arguments)
            ?: UnsupportedMethod(name, arguments).left()
}

val DefaultComptimeFunctionRegistry = ComptimeFunctionRegistry {
    registerEquals()
    registerReferenceEquals()

    registerBooleanOperators()
    registerByteOperators()
    registerIntOperators()
    registerShortOperators()
    registerLongOperators()

    registerUByteOperators()
    registerUShortOperators()
    registerUIntOperators()
    registerULongOperators()

    registerFloatOperators()
    registerDoubleOperators()

    registerCharOperators()
    registerStringOperators()
}