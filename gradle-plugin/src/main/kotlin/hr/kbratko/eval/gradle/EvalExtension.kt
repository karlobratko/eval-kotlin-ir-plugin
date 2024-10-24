package hr.kbratko.eval.gradle

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import javax.inject.Inject

open class EvalExtension @Inject constructor(objects: ObjectFactory) {

    val annotations: ListProperty<String> = objects.listProperty(String::class.java)
    val prefixes: ListProperty<String> = objects.listProperty(String::class.java)

    fun annotation(fqName: String) {
        annotations.add(fqName)
    }

    fun annotations(vararg fqNames: String) {
        annotations.addAll(fqNames.asList())
    }

    fun annotations(fqNames: List<String>) {
        annotations.addAll(fqNames)
    }

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