package hr.kbratko.eval.interpreter.functions.constant

import arrow.core.right
import hr.kbratko.eval.interpreter.functions.ComptimeConstantType
import hr.kbratko.eval.interpreter.functions.ComptimeFunction
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionDefinition
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionRegistry
import hr.kbratko.eval.interpreter.functions.ComptimeFunctionSignature
import hr.kbratko.eval.interpreter.functions.FunctionName
import hr.kbratko.eval.interpreter.functions.GetterName
import hr.kbratko.eval.interpreter.functions.IntConstantType
import hr.kbratko.eval.interpreter.functions.StringConstantType
import hr.kbratko.eval.types.BooleanConstant
import hr.kbratko.eval.types.CharConstant
import hr.kbratko.eval.types.IntConstant
import hr.kbratko.eval.types.StringConstant

fun ComptimeFunctionRegistry.registerStringOperators() {
    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("compareTo"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType, StringConstantType)),
            implementation = ComptimeFunction { args ->
                val (receiver, other) = args as List<StringConstant>
                IntConstant(receiver.value.compareTo(other.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greater"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType, StringConstantType)),
            implementation = ComptimeFunction { args ->
                val (receiver, other) = args as List<StringConstant>
                BooleanConstant(receiver.value > other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("greaterOrEqual"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType, StringConstantType)),
            implementation = ComptimeFunction { args ->
                val (receiver, other) = args as List<StringConstant>
                BooleanConstant(receiver.value >= other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("less"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType, StringConstantType)),
            implementation = ComptimeFunction { args ->
                val (receiver, other) = args as List<StringConstant>
                BooleanConstant(receiver.value < other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("lessOrEqual"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType, StringConstantType)),
            implementation = ComptimeFunction { args ->
                val (receiver, other) = args as List<StringConstant>
                BooleanConstant(receiver.value <= other.value).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("plus"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType, ComptimeConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as StringConstant
                val other = args[1]
                StringConstant(receiver.value.plus(other.value)).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = GetterName("length"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args.first() as StringConstant
                IntConstant(receiver.value.length).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("get"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType, IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as StringConstant
                val index = args[1] as IntConstant
                CharConstant(receiver.value[index.value]).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("subSequence"),
            signature = ComptimeFunctionSignature(listOf(StringConstantType, IntConstantType, IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as StringConstant
                val startIndex = args[1] as IntConstant
                val endIndex = args[1] as IntConstant
                StringConstant(receiver.value.subSequence(startIndex.value, endIndex.value).toString()).right()
            }
        )
    )

    @Suppress("UNCHECKED_CAST")
    register(
        ComptimeFunctionDefinition(
            name = FunctionName("toString"),
            signature = ComptimeFunctionSignature(listOf(IntConstantType)),
            implementation = ComptimeFunction { args ->
                val receiver = args[0] as IntConstant
                StringConstant(receiver.value.toString()).right()
            }
        )
    )
}