package hr.kbratko.eval.interpreter

import arrow.core.Either
import arrow.core.right
import hr.kbratko.eval.BooleanConstant
import hr.kbratko.eval.ComptimeConstant
import hr.kbratko.eval.ComptimeError
import hr.kbratko.eval.IntConstant
import hr.kbratko.eval.StringConstant
import kotlin.reflect.KClass

data class ComptimeFunctionSignature(val argumentTypes: List<KClass<out ComptimeConstant>>)

fun interface ComptimeFunction {
    operator fun invoke(arguments: List<ComptimeConstant>): Either<ComptimeError, ComptimeConstant>
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
            BooleanConstant(a.value == b.value).right()
        }

        register(
            listOf("equals", "EQEQ"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntConstant::class, IntConstant::class)),
                implementation = equals
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(BooleanConstant::class, BooleanConstant::class)),
                implementation = equals
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(StringConstant::class, StringConstant::class)),
                implementation = equals
            )
        )

        val notEquals = ComptimeFunction { args ->
            val (a, b) = args
            BooleanConstant(a.value != b.value).right()
        }
        register(
            listOf("notEquals"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntConstant::class, IntConstant::class)),
                implementation = notEquals
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(BooleanConstant::class, BooleanConstant::class)),
                implementation = notEquals
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(StringConstant::class, StringConstant::class)),
                implementation = notEquals
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("compareTo"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntConstant::class, IntConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntConstant>
                    IntConstant(a.value.compareTo(b.value)).right()
                }
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(StringConstant::class, StringConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<StringConstant>
                    IntConstant(a.value.compareTo(b.value)).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("greater"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntConstant::class, IntConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntConstant>
                    BooleanConstant(a.value > b.value).right()
                }
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(StringConstant::class, StringConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<StringConstant>
                    BooleanConstant(a.value > b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("greaterOrEqual"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntConstant::class, IntConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntConstant>
                    BooleanConstant(a.value >= b.value).right()
                }
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(StringConstant::class, StringConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<StringConstant>
                    BooleanConstant(a.value >= b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("less"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntConstant::class, IntConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntConstant>
                    BooleanConstant(a.value < b.value).right()
                }
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(StringConstant::class, StringConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<StringConstant>
                    BooleanConstant(a.value < b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("lessOrEqual"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntConstant::class, IntConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntConstant>
                    BooleanConstant(a.value <= b.value).right()
                }
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(StringConstant::class, StringConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<StringConstant>
                    BooleanConstant(a.value <= b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("plus"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntConstant::class, IntConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntConstant>
                    IntConstant(a.value + b.value).right()
                }
            ),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(StringConstant::class, StringConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<StringConstant>
                    StringConstant(a.value + b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("inc"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a) = args as List<IntConstant>
                    IntConstant(a.value.inc()).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("dec"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a) = args as List<IntConstant>
                    IntConstant(a.value.dec()).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("minus"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntConstant::class, IntConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntConstant>
                    IntConstant(a.value - b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("times"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntConstant::class, IntConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntConstant>
                    IntConstant(a.value * b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("div"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(IntConstant::class, IntConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<IntConstant>
                    IntConstant(a.value / b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("and"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(BooleanConstant::class, BooleanConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<BooleanConstant>
                    BooleanConstant(a.value && b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("or"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(BooleanConstant::class, BooleanConstant::class)),
                implementation = ComptimeFunction { args ->
                    val (a, b) = args as List<BooleanConstant>
                    BooleanConstant(a.value || b.value).right()
                }
            )
        )

        @Suppress("UNCHECKED_CAST")
        register(
            listOf("not"),
            ComptimeFunctionDefinition(
                signature = ComptimeFunctionSignature(listOf(BooleanConstant::class)),
                implementation = ComptimeFunction { args ->
                    val a = args.first() as BooleanConstant
                    BooleanConstant(!a.value).right()
                }
            )
        )

    }
}