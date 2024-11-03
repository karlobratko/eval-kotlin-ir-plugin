package hr.kbratko.eval.gradle.model

import java.io.Serializable

internal data class EvalImpl(
    override val name: String,
    override val prefixes: List<String>
) : Eval, Serializable {

    override val modelVersion = serialVersionUID

    companion object {
        private const val serialVersionUID = 1L
    }

}
