package hr.kbratko.eval

import arrow.core.Option
import arrow.core.getOrNone
import arrow.core.toOption
import org.jetbrains.kotlin.backend.common.pop
import org.jetbrains.kotlin.backend.common.push
import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration

class Scope(variables: Map<IrValueDeclaration, ConstantValue<*>> = mapOf()) {
    private val variables = mutableMapOf<IrValueDeclaration, ConstantValue<*>>()
        .apply { putAll(variables) }

    operator fun set(name: IrValueDeclaration, value: ConstantValue<*>) {
        variables[name] = value
    }

    operator fun get(name: IrValueDeclaration): Option<ConstantValue<*>> = variables.getOrNone(name)
}

class ScopeStack(baseScope: Scope = Scope()) {

    private val scopeStack = mutableListOf(baseScope)

    fun enterScope() {
        scopeStack.push(Scope())
    }

    fun leaveScope() {
        if (scopeStack.isNotEmpty()) {
            scopeStack.pop()
        }
    }

    fun currentScope(): Scope {
        return scopeStack.last()
    }

    inline fun <T> scope(body: Scope.() -> T): T {
        enterScope()
        return try {
            currentScope().body()
        } finally {
            leaveScope()
        }
    }

    operator fun set(name: IrValueDeclaration, value: ConstantValue<*>) {
        currentScope()[name] = value
    }

    operator fun get(name: IrValueDeclaration): Option<ConstantValue<*>> =
        scopeStack.reversed().firstNotNullOfOrNull { it[name].getOrNull() }.toOption()

}