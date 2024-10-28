package hr.kbratko.eval

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.jetbrains.kotlin.constant.BooleanValue
import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.constant.IntValue
import org.jetbrains.kotlin.constant.StringValue
import kotlin.reflect.KClass

fun interface ComptimeFunction {
    operator fun invoke(arguments: List<ConstantValue<*>>): Either<EvalError, ConstantValue<*>>
}

open class ComptimeFunctionRegistry {
    private val operations: MutableMap<String, MutableMap<List<KClass<out ConstantValue<*>>>, ComptimeFunction>> =
        mutableMapOf()

    // Register a new operation for a function name with expected parameter types
    fun register(name: String, parameterTypes: List<KClass<out ConstantValue<*>>>, operation: ComptimeFunction) {
        operations.getOrPut(name) { mutableMapOf() }
            .put(parameterTypes, operation)
    }

    // Find a method operation based on name and argument types
    fun findOperation(name: String, arguments: List<ConstantValue<*>>): ComptimeFunction? {
        return operations[name]?.get(arguments.map { it::class })
    }
}

object DefaultComptimeFunctionRegistry : ComptimeFunctionRegistry() {
    init {
        // Equality and Inequality
        // `equals` and `notEquals` should work for Int, Boolean, and String.
        @Suppress("UNCHECKED_CAST")
        register("equals", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            BooleanValue(a.value == b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("notEquals", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            BooleanValue(a.value != b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("equals", listOf(BooleanValue::class, BooleanValue::class)) { args ->
            val (a, b) = args as List<BooleanValue>
            BooleanValue(a.value == b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("notEquals", listOf(BooleanValue::class, BooleanValue::class)) { args ->
            val (a, b) = args as List<BooleanValue>
            BooleanValue(a.value != b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("equals", listOf(StringValue::class, StringValue::class)) { args ->
            val (a, b) = args as List<StringValue>
            BooleanValue(a.value == b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("notEquals", listOf(StringValue::class, StringValue::class)) { args ->
            val (a, b) = args as List<StringValue>
            BooleanValue(a.value != b.value).right()
        }

        // Comparison operators for Int and String
        @Suppress("UNCHECKED_CAST")
        register("compareTo", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            IntValue(a.value.compareTo(b.value)).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("compareTo", listOf(StringValue::class, StringValue::class)) { args ->
            val (a, b) = args as List<StringValue>
            IntValue(a.value.compareTo(b.value)).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("greater", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            BooleanValue(a.value > b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("greater", listOf(StringValue::class, StringValue::class)) { args ->
            val (a, b) = args as List<StringValue>
            BooleanValue(a.value > b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("greaterOrEqual", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            BooleanValue(a.value >= b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("greaterOrEqual", listOf(StringValue::class, StringValue::class)) { args ->
            val (a, b) = args as List<StringValue>
            BooleanValue(a.value >= b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("less", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            BooleanValue(a.value < b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("less", listOf(StringValue::class, StringValue::class)) { args ->
            val (a, b) = args as List<StringValue>
            BooleanValue(a.value < b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("lessOrEqual", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            BooleanValue(a.value <= b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("lessOrEqual", listOf(StringValue::class, StringValue::class)) { args ->
            val (a, b) = args as List<StringValue>
            BooleanValue(a.value <= b.value).right()
        }

        // Arithmetic methods for Int
        @Suppress("UNCHECKED_CAST")
        register("plus", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            IntValue(a.value + b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("inc", listOf(IntValue::class)) { args ->
            val (a) = args as List<IntValue>
            IntValue(a.value.inc()).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("dec", listOf(IntValue::class)) { args ->
            val (a) = args as List<IntValue>
            IntValue(a.value.dec()).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("minus", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            IntValue(a.value - b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("times", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            IntValue(a.value * b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("div", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            if (b.value == 0) {
                DivisionByZero.left()
            } else {
                IntValue(a.value / b.value).right()
            }
        }

        // Logical operations for Boolean
        @Suppress("UNCHECKED_CAST")
        register("and", listOf(BooleanValue::class, BooleanValue::class)) { args ->
            val (a, b) = args as List<BooleanValue>
            BooleanValue(a.value && b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("or", listOf(BooleanValue::class, BooleanValue::class)) { args ->
            val (a, b) = args as List<BooleanValue>
            BooleanValue(a.value || b.value).right()
        }

        @Suppress("UNCHECKED_CAST")
        register("not", listOf(BooleanValue::class)) { args ->
            val (a) = args as List<BooleanValue>
            BooleanValue(!a.value).right()
        }

        // String concatenation using "plus"
        @Suppress("UNCHECKED_CAST")
        register("plus", listOf(StringValue::class, StringValue::class)) { args ->
            val (a, b) = args as List<StringValue>
            StringValue(a.value + b.value).right()
        }

    }
}