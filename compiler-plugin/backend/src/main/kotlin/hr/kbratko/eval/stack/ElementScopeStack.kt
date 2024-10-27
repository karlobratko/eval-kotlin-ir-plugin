package hr.kbratko.eval.stack

import org.jetbrains.kotlin.backend.common.peek
import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.backend.common.push
import org.jetbrains.kotlin.ir.IrElement

interface ElementScope<out E : IrElement> {
    val element: E
}

open class ElementScopeStack<S : ElementScope<*>> {
    protected val stack = mutableListOf<S>()

    open fun push(scope: S): S? {
        stack.push(scope)
        return scope
    }

    open fun pop(): S? = if (stack.isNotEmpty()) stack.pop() else null

    open fun peek(): S? = stack.peek()

    inline fun useScope(scope: S, body: S.() -> Unit) {
        val scope = push(scope) ?: return
        try {
            val result = scope.body()
            return result
        } finally {
            pop()
        }
    }
}