package hr.kbratko.eval.interpreter.functions

import arrow.core.Either
import arrow.core.left
import hr.kbratko.eval.interpreter.functions.constant.*
import hr.kbratko.eval.types.ComptimeConstant
import hr.kbratko.eval.types.ComptimeError
import hr.kbratko.eval.types.UnsupportedMethod

class ComptimeFunctionRegistry(body: ComptimeFunctionRegistry.() -> Unit) {
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

    init {
        this.body()
    }
}

val DefaultComptimeFunctionRegistry = ComptimeFunctionRegistry {
    registerEquals()
    registerToString()
    registerHashCode()

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