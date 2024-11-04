# Motivation

This project is especially interesting to me because it focuses on adding compile-time evaluation (or "comptime") to
Kotlin. In high-level languages like Kotlin, where ease of use is often prioritized over performance, having
computations resolved at compile time could create big advantages. Comptime evaluation would enable computations that
are predictable or repetitive to be processed during compilation, reducing the work needed at runtime. This could not
only improve runtime performance but also make code safer by catching certain errors earlier, leading to a smoother
development experience overall.

## High-level implementation steps

This project could enhance Kotlin's compiler with comptime features similar to those found in lower-level languages like
Zig. The goal is to add comptime functionality to Kotlin so that certain computations can be done and optimized at
compile time, offering multiple benefits: reducing runtime costs, catching potential errors sooner, and ultimately
improving the performance and reliability of Kotlin applications. However, there may be challenges, such as handling
potentially endless compile-time computations (like while(true) loops).

To achieve comptime evaluation in Kotlin, this project could be divided into a few main steps:

1. Designing a Comptime IR Layer
   This layer would map Kotlin's existing Intermediate Representation (IR) to a new, specialized "Comptime IR" that is
   optimized for compile-time evaluation. It would allow us to track computations, resolve values, and handle
   compile-time functions without affecting the main Kotlin IR, keeping the design organized and modular. A dedicated
   plugin system could also be created specifically for this compilation stage, making the comptime functionality more
   flexible and easier to extend in the future.

2. Core Interpreter and Function Registry
   A powerful interpreter capable of handling various Comptime IR elements, operators, and control flows is essential.
   This interpreter would evaluate functions and expressions on constant values at compile time. A function registry
   would also store basic operators and functions specifically optimized for comptime processing. In the future, it
   could even be extended to support user-defined comptime functions, making Kotlin more versatile at compile time.

3. Error Management and Fail-Fast Mechanisms
   Adopting a fail-fast error handling approach would allow this project to catch issues that would normally only appear
   at runtime. For example, if an illegal operation like division by zero occurs during compilation, it would
   immediately be flagged, improving code safety and providing a smoother development experience.

4. User-Defined Comptime Support (Future Phase)
   To stay consistent with Kotlin's user-friendly design, it would be valuable to allow developers to define custom
   comptime functions. Just like in Zig, where comptime functions are executed during compilation, Kotlin developers
   could create functions for compile-time execution. This could be useful for features like Domain-Specific Languages (
   DSLs) or compile-time validation, enabling new ways to use Kotlin.

5. Documentation, IDE Integration, and Feedback Loop
   Real-time feedback from the IDE could help developers see which parts of their code are comptime-compatible. This
   would make writing comptime-compliant code much easier. Tooling that shows how comptime affects runtime
   performance—such as metrics showing reduced runtime computation—would also give developers insight into how comptime
   optimizations are impacting their code.

## Compile-time optimization

As I was listing The Dragon Book (Compilers: Principles, Techniques, and Tools) before and during solving of this task,
I came across several compile-time optimizations that improve performance and efficiency. Here are some key
optimizations mentioned:

- Constant Folding: Evaluates constant expressions during compilation instead of at runtime. For example, 3 + 4 can be
  calculated at compile time, reducing runtime calculations.
- Constant Propagation: Propagates known constant values through the code, allowing further simplification and even
  elimination of variables with constant values.
- Dead Code Elimination: Removes code that has no effect on the program's output. For instance, if a variable is
  assigned a value that is never used, that assignment can be eliminated.
- Common Subexpression Elimination (CSE): Finds expressions that are computed multiple times with the same operands and
  replaces subsequent occurrences with a single computed value. For example, if a + b appears multiple times in a
  function, the result is computed once and reused.
- Loop-Invariant Code Motion: Moves computations that produce the same result in each loop iteration (loop-invariant)
  outside the loop. This reduces the workload within the loop.
- Strength Reduction: Replaces expensive operations with cheaper ones, especially in loops. For example, it can replace
  multiplication with addition or bit-shifting to make code faster.
- Induction Variable Simplification: Applies strength reduction specifically to loops, simplifying variables that change
  in a predictable way each iteration. For example, certain multiplications or additions can be replaced with simpler
  increments.
- Inline Expansion: Replaces a function call with the body of the function, removing the overhead of the call. This
  increases binary size but can improve performance by avoiding function call overhead.
- Tail Recursion Optimization: Optimizes recursive functions where the recursive call is the last operation. This allows
  the recursion to be transformed into a loop, saving on stack space and function call overhead.
- Peephole Optimization: Examines small sets of instructions and replaces inefficient sequences with more efficient
  ones. This can remove redundant operations or combine instructions for better performance.
- Register Allocation: Assigns frequently used values to CPU registers rather than memory, which speeds up access times.
  This is crucial for performance in tight loops and frequently used functions.
- Code Hoisting: Moves computations to the earliest safe point in the program, reducing redundant operations in
  different branches.
- Jump Threading: Removes unnecessary jumps by linking blocks directly to their targets when the condition is
  predictable. This improves control flow, especially in branches with predictable outcomes.
- Partial Redundancy Elimination (PRE): Combines common subexpression elimination and code hoisting, removing
  redundancies that are not globally redundant but can be optimized by moving code to a point where it appears less
  frequently.
- Loop Unrolling: Replicates the loop body multiple times to reduce the overhead of loop control. This decreases branch
  instructions and can improve CPU cache performance.

## Existing implementations

To give an example of language which has comptime evaluation support I will mention Zig. Zig's comptime evaluation
offers several examples of how compile-time evaluation can be used as and example:

- Compile-Time Assertions: Zig allows developers to make assertions that are checked during compilation, validating
  assumptions early. If an assertion fails, compilation stops, preventing runtime errors.
- Constant Folding: Zig's comptime allows constant expressions to be calculated during compilation, including complex
  data structures and some function calls. These constants are embedded directly into the binary.
- Type Generation: Zig can generate and modify types at compile time, allowing for highly efficient and generic code.
  This eliminates runtime type-checking, providing both type safety and performance improvements by embedding types as
  constants in the binary.

For practical examples take a look at following simple (in-progress) implementation of glm in
Zig: https://github.com/karlobratko/ziglm.