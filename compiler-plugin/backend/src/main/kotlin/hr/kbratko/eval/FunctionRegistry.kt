package hr.kbratko.eval

import org.jetbrains.kotlin.constant.BooleanValue
import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.constant.IntValue
import org.jetbrains.kotlin.constant.StringValue
import kotlin.reflect.KClass

fun interface ComptimeFunction {
    operator fun invoke(arguments: List<ConstantValue<*>>): EvalResult<ConstantValue<*>>
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
        register("equals", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            EvalResult.Success(BooleanValue(a.value == b.value))
        }

        register("notEquals", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            EvalResult.Success(BooleanValue(a.value != b.value))
        }

        register("equals", listOf(BooleanValue::class, BooleanValue::class)) { args ->
            val (a, b) = args as List<BooleanValue>
            EvalResult.Success(BooleanValue(a.value == b.value))
        }

        register("notEquals", listOf(BooleanValue::class, BooleanValue::class)) { args ->
            val (a, b) = args as List<BooleanValue>
            EvalResult.Success(BooleanValue(a.value != b.value))
        }

        register("equals", listOf(StringValue::class, StringValue::class)) { args ->
            val (a, b) = args as List<StringValue>
            EvalResult.Success(BooleanValue(a.value == b.value))
        }

        register("notEquals", listOf(StringValue::class, StringValue::class)) { args ->
            val (a, b) = args as List<StringValue>
            EvalResult.Success(BooleanValue(a.value != b.value))
        }

        // Comparison operators for Int and String
        register("compareTo", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            EvalResult.Success(IntValue(a.value.compareTo(b.value)))
        }

        register("compareTo", listOf(StringValue::class, StringValue::class)) { args ->
            val (a, b) = args as List<StringValue>
            EvalResult.Success(IntValue(a.value.compareTo(b.value)))
        }

        register("greater", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            EvalResult.Success(BooleanValue(a.value > b.value))
        }

        register("greater", listOf(StringValue::class, StringValue::class)) { args ->
            val (a, b) = args as List<StringValue>
            EvalResult.Success(BooleanValue(a.value > b.value))
        }

        register("greaterOrEqual", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            EvalResult.Success(BooleanValue(a.value >= b.value))
        }

        register("greaterOrEqual", listOf(StringValue::class, StringValue::class)) { args ->
            val (a, b) = args as List<StringValue>
            EvalResult.Success(BooleanValue(a.value >= b.value))
        }

        register("less", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            EvalResult.Success(BooleanValue(a.value < b.value))
        }

        register("less", listOf(StringValue::class, StringValue::class)) { args ->
            val (a, b) = args as List<StringValue>
            EvalResult.Success(BooleanValue(a.value < b.value))
        }

        register("lessOrEqual", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            EvalResult.Success(BooleanValue(a.value <= b.value))
        }

        register("lessOrEqual", listOf(StringValue::class, StringValue::class)) { args ->
            val (a, b) = args as List<StringValue>
            EvalResult.Success(BooleanValue(a.value <= b.value))
        }

        // Arithmetic methods for Int
        register("plus", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            EvalResult.Success(IntValue(a.value + b.value))
        }

        register("minus", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            EvalResult.Success(IntValue(a.value - b.value))
        }

        register("times", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            EvalResult.Success(IntValue(a.value * b.value))
        }

        register("div", listOf(IntValue::class, IntValue::class)) { args ->
            val (a, b) = args as List<IntValue>
            if (b.value == 0) {
                EvalResult.Failure(DivisionByZero)
            } else {
                EvalResult.Success(IntValue(a.value / b.value))
            }
        }

        // Logical operations for Boolean
        register("and", listOf(BooleanValue::class, BooleanValue::class)) { args ->
            val (a, b) = args as List<BooleanValue>
            EvalResult.Success(BooleanValue(a.value && b.value))
        }

        register("or", listOf(BooleanValue::class, BooleanValue::class)) { args ->
            val (a, b) = args as List<BooleanValue>
            EvalResult.Success(BooleanValue(a.value || b.value))
        }

        register("not", listOf(BooleanValue::class)) { args ->
            val (a) = args as List<BooleanValue>
            EvalResult.Success(BooleanValue(!a.value))
        }

        // String concatenation using "plus"
        register("plus", listOf(StringValue::class, StringValue::class)) { args ->
            val (a, b) = args as List<StringValue>
            EvalResult.Success(StringValue(a.value + b.value))
        }

    }
}