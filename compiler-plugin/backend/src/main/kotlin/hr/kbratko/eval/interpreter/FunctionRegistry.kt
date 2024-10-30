package hr.kbratko.eval.interpreter

import arrow.core.Either
import arrow.core.right
import hr.kbratko.eval.ComptimeError
import org.jetbrains.kotlin.constant.BooleanValue
import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.constant.IntValue
import org.jetbrains.kotlin.constant.StringValue
import kotlin.reflect.KClass

data class ComptimeFunctionSignature(val argumentTypes: List<KClass<out ConstantValue<*>>>)

fun interface ComptimeFunction {
    operator fun invoke(arguments: List<ConstantValue<*>>): Either<ComptimeError, ConstantValue<*>>
}

data class ComptimeFunctionDefinition(
    val signature: ComptimeFunctionSignature,
    val implementation: ComptimeFunction
)

// TODO: refine logic with predicate types
open class ComptimeFunctionRegistry {

    private val operations: MutableMap<String, MutableMap<ComptimeFunctionSignature, ComptimeFunction>> = mutableMapOf()

    fun register(names: List<String>, vararg comptimeFunctions: ComptimeFunctionDefinition) {
        names.forEach { name ->
            comptimeFunctions.forEach { (parameterTypes, operation) ->
                operations.computeIfAbsent(name) { mutableMapOf() }[parameterTypes] = operation
            }
        }
    }

    open fun findOperation(name: String, signature: ComptimeFunctionSignature): ComptimeFunction? =
        operations[name]?.get(signature)
}

// TODO: support all primitive types and String
object DefaultComptimeFunctionRegistry : ComptimeFunctionRegistry() {
    init {
        val equals = ComptimeFunction { args ->
            val (a, b) = args
            BooleanValue(a.value == b.value).right()
        }

        register(
            listOf("equals", "EQEQ"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntValue::class, IntValue::class)),
                implementation = equals
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(BooleanValue::class, BooleanValue::class)),
                implementation = equals
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(StringValue::class, StringValue::class)),
                implementation = equals
            )
        )

        val notEquals = ComptimeFunction { args ->
            val (a, b) = args
            BooleanValue(a.value != b.value).right()
        }
        register(
            listOf("notEquals"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntValue::class, IntValue::class)),
                implementation = notEquals
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(BooleanValue::class, BooleanValue::class)),
                implementation = notEquals
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(StringValue::class, StringValue::class)),
                implementation = notEquals
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("compareTo"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntValue::class, IntValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntValue>
                    IntValue(a.value.compareTo(b.value)).right()
                }
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(StringValue::class, StringValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<StringValue>
                    IntValue(a.value.compareTo(b.value)).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("greater"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntValue::class, IntValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntValue>
                    BooleanValue(a.value > b.value).right()
                }
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(StringValue::class, StringValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<StringValue>
                    BooleanValue(a.value > b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("greaterOrEqual"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntValue::class, IntValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntValue>
                    BooleanValue(a.value >= b.value).right()
                }
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(StringValue::class, StringValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<StringValue>
                    BooleanValue(a.value >= b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("less"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntValue::class, IntValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntValue>
                    BooleanValue(a.value < b.value).right()
                }
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(StringValue::class, StringValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<StringValue>
                    BooleanValue(a.value < b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("lessOrEqual"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntValue::class, IntValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntValue>
                    BooleanValue(a.value <= b.value).right()
                }
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(StringValue::class, StringValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<StringValue>
                    BooleanValue(a.value <= b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("plus"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntValue::class, IntValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntValue>
                    IntValue(a.value + b.value).right()
                }
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(StringValue::class, StringValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<StringValue>
                    StringValue(a.value + b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("inc"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a) = args as List<IntValue>
                    IntValue(a.value.inc()).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("dec"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a) = args as List<IntValue>
                    IntValue(a.value.dec()).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("minus"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntValue::class, IntValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntValue>
                    IntValue(a.value - b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("times"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntValue::class, IntValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntValue>
                    IntValue(a.value * b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("div"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntValue::class, IntValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntValue>
                    IntValue(a.value / b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("and"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(BooleanValue::class, BooleanValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<BooleanValue>
                    BooleanValue(a.value && b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("or"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(BooleanValue::class, BooleanValue::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<BooleanValue>
                    BooleanValue(a.value || b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("not"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(BooleanValue::class)),
                implementation = ComptimeFunction { args ->
                    val a = args.first() as BooleanValue
                    BooleanValue(!a.value).right()
                }
            )
        )

    }
}