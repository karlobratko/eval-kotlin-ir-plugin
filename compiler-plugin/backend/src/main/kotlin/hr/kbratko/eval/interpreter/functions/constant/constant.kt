package hr.kbratko.eval.interpreter.functions.constant

import arrow.core.right
import hr.kbratko.eval.interpreter.functions.ComptimeConstantType
import hr.kbratko.eval.interpreter.functions.ComptimeFunction
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionDefinition
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionRegistry
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionSignature
import hr.kbratko.eval.interpreter.functions.FunctionName
import hr.kbratko.eval.types.BooleanConstant
import kotlin.collections.component1
import kotlin.collections.component2

fun ComptimeFunctionRegistry.registerEquals() {
    val signature = ComptimeFunctionSignature(listOf(ComptimeConstantType, ComptimeConstantType))
    val implementation = ComptimeFunction { args ->
        val (a, b) = args
        BooleanConstant(a.value == b.value).right()
    }
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("equals"),
            signature = signature,
            implementation = implementation
        ),
        ComptimeFunctionDefinition(
            name = FunctionName("EQEQ"),
            signature = signature,
            implementation = implementation
        )
    )
}

fun ComptimeFunctionRegistry.registerReferenceEquals() {
    val signature = ComptimeFunctionSignature(listOf(ComptimeConstantType, ComptimeConstantType))
    val implementation = ComptimeFunction { args ->
        val (a, b) = args
        BooleanConstant(a.value === b.value).right()
    }
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("EQEQEQ"),
            signature = signature,
            implementation = implementation
        )
    )
}