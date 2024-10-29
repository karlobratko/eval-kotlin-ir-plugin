package hr.kbratko.eval.interpreter.stack

import org.jetbrains.kotlin.backend.common.peek
import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.backend.common.push
import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration

class DeclarationScope(initVariables: Map<IrValueDeclaration, ConstantValue<*>> = mapOf()) {
    private val variables: MutableMap<IrValueDeclaration, ConstantValue<*>> = mutableMapOf()

    init {
        variables.putAll(initVariables)
    }

    operator fun set(name: IrValueDeclaration, value: ConstantValue<*>) {
        variables[name] = value
    }

    operator fun get(name: IrValueDeclaration): ConstantValue<*>? = variables[name]
}

class DeclarationStack(baseScope: DeclarationScope) {
    private val stack = mutableListOf<DeclarationScope>()

    init {
        stack.push(baseScope)
    }

    fun push(scope: DeclarationScope): DeclarationScope? {
        stack.push(scope)
        return scope
    }

    fun pop(): DeclarationScope? = if (stack.isNotEmpty()) stack.pop() else null

    fun peek(): DeclarationScope? = stack.peek()

    inline fun scope(body: DeclarationScope.() -> Unit) {
        val scope = push(DeclarationScope()) ?: return
        try {
            val result = scope.body()
            return result
        } finally {
            pop()
        }
    }

    fun write(name: IrValueDeclaration, value: ConstantValue<*>) {
        val scope = findScopeWithVariable(name)
        if (scope != null) {
            scope[name] = value
        } else {
            declare(name, value)
        }
    }

    fun declare(name: IrValueDeclaration, value: ConstantValue<*>) {
        peek()!![name] = value
    }

    private fun findScopeWithVariable(name: IrValueDeclaration): DeclarationScope? =
        stack.reversed().firstOrNull {
            it[name] != null
        }

    operator fun get(name: IrValueDeclaration): ConstantValue<*>? = stack.reversed().firstNotNullOfOrNull { it[name] }
}