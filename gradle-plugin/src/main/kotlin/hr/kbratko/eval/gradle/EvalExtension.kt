package hr.kbratko.eval.gradle

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import javax.inject.Inject

open class EvalExtension @Inject constructor(objects: ObjectFactory) {
    val prefixes: ListProperty<String> = objects.listProperty(String::class.java)

    fun prefix(prefix: String) {
        prefixes.add(prefix)
    }

    fun prefixes(vararg prefixes: String) {
        this.prefixes.addAll(prefixes.asList())
    }

    fun prefixes(prefixes: List<String>) {
        this.prefixes.addAll(prefixes)
    }
}