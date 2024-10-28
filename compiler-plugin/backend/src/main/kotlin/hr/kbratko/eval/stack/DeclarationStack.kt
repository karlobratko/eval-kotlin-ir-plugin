package hr.kbratko.eval.stack

import org.jetbrains.kotlin.constant.ConstantValue
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration

class DeclarationScope(
    override val element: IrElement,
    initVariables: Map<IrValueDeclaration, ConstantValue<*>> = mapOf()
) : ElementScope<IrElement> {
    private val variables: MutableMap<IrValueDeclaration, ConstantValue<*>> = mutableMapOf()

    init {
        variables.putAll(initVariables)
    }

    operator fun set(name: IrValueDeclaration, value: ConstantValue<*>) {
        variables[name] = value
    }

    operator fun get(name: IrValueDeclaration): ConstantValue<*>? = variables[name]
}

class DeclarationStack(baseScope: DeclarationScope) : ElementScopeStack<DeclarationScope>() {
    init {
        push(baseScope)
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