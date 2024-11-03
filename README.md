# Comptime Evaluation Compiler and Gradle Plugin

This repository implements a Kotlin Compiler Plugin and Gradle Plugin for compile-time expression evaluation (comptime
evaluation).
The project uses Kotlin version 1.9.24 and provides a framework for evaluating expressions at compile time in supported
types, facilitating optimizations and reducing runtime overhead by precomputing values in compile-time where possible.

## Project Structure

The project is organized into several main modules:

- `compiler-plugin/backend`: The backend module defines the core interpretation logic.
- `compiler-plugin/cli`: The CLI module provides command-line interface support for the plugin.
- `gradle-plugin`: This module integrates the comptime evaluation functionality with Gradle, allowing users to configure
  and apply the compiler plugin to their projects.

## Compiler Plugin Design and Evolution

### Initial Approach: IR Traversal

Initially, the project explored a straightforward approach of using a visitor pattern to traverse the IR tree. This
method, however, lacked sufficient control for our use case; it did not allow for the necessary granular management of
evaluation states, which is essential in determining precise results for expressions.

### Adopting the Interpreter Pattern

To gain full control over evaluation, we adopted the interpreter pattern, creating an interpreter for each IR node that
our plugin supports. This design allowed for selective evaluation of specific IR elements (e.g., IrBlock, IrConst,
IrCall) and provided more accurate outcomes during compile-time optimization. The interpreter pattern facilitated a more
granular evaluation, as we could fine-tune each IR node type’s interpretation and error handling.

### Exploring and Abandoning ComptimeIR

There was an early consideration to implement a separate ComptimeIR domain that would serve as a deterministic tree
structure mapped from the backend IR. The goal was to produce a more predictable, ComptimeIR tree suitable for
compile-time execution and evaluation. However, this approach was ultimately set aside due to its complexity and the
overhead of mapping every IR node into a specialized domain. Instead, we maintained control through the interpreter
pattern within the existing IR framework, achieving the necessary control and optimization.

### Implementation

### Supported Types and Constants

The core of the plugin is built on a set of specialized `ComptimeConstant` types. These types represent constants that
the compiler can evaluate at compile time, ensuring deterministic behavior and optimized execution.

```kotlin
sealed interface ComptimeConstant {
    val value: Any
}

data class BooleanConstant(override val value: Boolean) : ComptimeConstant
data class CharConstant(override val value: Char) : ComptimeConstant
data class StringConstant(override val value: String) : ComptimeConstant

// All primitive types and supported numeric and unsigned types are implemented as subtypes.
```

This model allows only specific types (e.g., primitive types, unsigned types, and `String`) to be evaluated at compile
time, making the set of supported types deterministic.

### Error Management

Error handling within the interpreter is designed to be **fail-fast** and **fail-safe**. All errors, such as unsupported
operations or invalid computations, are represented as `ComptimeError` types and propagate through the interpreter call
stack. Additionally, runtime execution errors, like division by zero, are caught and wrapped in a domain-specific error
type, `ComptimeUnexpectedError`, ensuring safe and clear error reporting. By catching runtime execution errors and
presenting them in a controlled manner, we maintain consistency and prevent unexpected crashes during compilation.

```kotlin
sealed interface ComptimeError {
    val description: String
}

class ComptimeUnexpectedError(cause: Throwable) : ComptimeError, Throwable(cause) {
    override val description: String = cause.message ?: "Unknown error"
}

sealed interface ComptimeProducedError : ComptimeError

// Specific subclasses are defined for specific known error scenarios.
```

### Outcome Propagation

To manage interpretation results and control flow, we use `ComptimeOutcome`, a sealed interface that supports multiple
outcomes, such as successful value results, return control flow, and error propagation.

```kotlin
sealed interface EvaluationOutcome {
    sealed interface EvaluationResult : EvaluationOutcome {
        data class ConstantResult(val value: ComptimeConstant) : EvaluationResult
        data object NoResult : EvaluationResult
    }

    sealed interface ControlFlow : EvaluationOutcome {
        data class Return(val value: ComptimeConstant, val target: IrReturnTargetSymbol) : ControlFlow
        data class Break(val loop: IrLoop, val label: String? = null) : ControlFlow
        data class Continue(val loop: IrLoop, val label: String? = null) : ControlFlow
        data class EvaluationError(val error: ComptimeError) : ControlFlow
    }

    val isControlFlow: Boolean get() = this is ControlFlow
}
```

### Comptime Interpreter

The main functionality of the compiler plugin is in the `ComptimeInterpreter`. This interpreter traverses supported
`IrElement` nodes, interprets their results, and performs comptime evaluations where applicable. The interpreter pattern
is applied across supported `IrElement` types.

```kotlin
sealed interface ComptimeIrElementInterpreter<E : IrElement> {
    val element: E
    fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome
}

// Specific implementations handle different IR element types, such as `IrBlock`, `IrVariable`, and `IrCall`.
```

#### Example Interpreter Usage with Declaration Stack

The interpreter employs a stack-based scoping model to manage variable declarations. Each `IrBlock` element opens a new
scope in the `DeclarationStack`, ensuring proper variable scoping and lifecycle management.

```kotlin
class DeclarationStack(baseScope: DeclarationScope) {
    inline fun scope(body: DeclarationScope.() -> Unit) {
        val scope = push(DeclarationScope()) ?: return
        try {
            body(scope)
        } finally {
            pop()
        }
    }
}

// Usage in an interpreter
class ComptimeIrBlockInterpreter(override val element: IrBlock) : ComptimeIrElementInterpreter<IrBlock> {
    override fun interpret(context: ComptimeInterpreterContext): EvaluationOutcome {
        val outcomes = mutableListOf<EvaluationOutcome>()
        context.scopeStack.scope {
            element.statements.forEach { it.interpret(context) }
        }
        return outcomes.lastOrNull() ?: NoResult
    }
}
```

### Comptime Function Registry

The `ComptimeFunctionRegistry` provides a centralized registry for supported operators and functions. This registry
allows overloading functions based on `ComptimeConstant` types and supports defining type-predicate-based signatures.

```kotlin
class ComptimeFunctionRegistry {
    private val operations: MutableMap<String, MutableMap<ComptimeFunctionSignature, ComptimeFunction>> = mutableMapOf()

    fun register(vararg comptimeFunctions: ComptimeFunctionDefinition) { /* Registration logic */
    }

    fun execute(
        name: String,
        arguments: List<ComptimeConstant>
    ): Either<ComptimeError, ComptimeConstant> { /* Execution logic */
    }
}

val DefaultComptimeFunctionRegistry = ComptimeFunctionRegistry {
    registerEquals()
    registerArithmeticOperators()

    // Additional operator registrations
}
```

### Testing with Kotlin Compile Testing

We utilized `kotlin-compile-testing` and `kotest` to create programmatic tests for validating compile-time evaluation.
Each test inspects the bytecode to confirm that computations were evaluated at compile-time by checking for constants in
the generated bytecode.

For instance, we verify that computed values are pushed directly to the stack without redundant function calls by
analyzing bytecode instructions:

```kotlin
object InterpreterTests : ShouldSpec({
    context("if expression") {
        withData(
            mapOf(
                "as statement" to OperatorTest(
                    source = """
                    fun evalTest(a: Int, b: Int): Int {
                        var max = a
                        if (a < b) max = b
                        return max
                    }
    
                    fun main() {
                        val result = evalTest(10, 11)
                    }
                    """,
                    expectedInstructions = listOf(
                        PushByte(11),
                        StoreInteger(0)
                    ) atLine 8
                )
            )
        ) { it.test() }
    }
})

```

## Future Improvements

While the current implementation supports a wide range of comptime expressions, future improvements could focus on the
following:

1. **Advanced Data Structures**: Currently, our implementation lacks support for more complex structures like ranges,
   data classes, lists, and arrays. Since Kotlin is a high-level programming language that heavily utilizes these
   structures, adding support for them would significantly enhance the plugin’s usability in real-world scenarios.
2. **Zig’s `comptime` Model**: Zig, a low-level programming language, offers a robust compile-time execution model
   through its `comptime` feature. `comptime` in Zig allows any expression or function to be evaluated during
   compilation if all required inputs are known at compile time. This feature provides full flexibility to the compiler,
   enabling powerful optimizations and custom code generation. Adopting similar techniques in `eval` could offer deeper,
   more integrated compile-time evaluations and broaden the scope of optimizable expressions.
3. **Enhanced Type Safety**: Improving type-checking during compile-time evaluation could prevent certain errors from
   propagating to runtime, especially for functions and operators with multiple overloads. Building a more sophisticated
   type inference and validation mechanism within the interpreter would further strengthen this.

## Proposal for a Future Comptime IR Model

We initially considered implementing a separate `ComptimeIr` model mapped from the backend IR model. This approach would
have allowed more deterministic control and type safety in the comptime evaluation process. However, due to its
complexity and resource requirements, this idea was set aside.

---

This project offers a foundation for comptime evaluation within Kotlin, showcasing the potential of compile-time
computation in optimizing runtime performance and code quality.
