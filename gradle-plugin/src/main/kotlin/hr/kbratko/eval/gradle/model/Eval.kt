package hr.kbratko.eval.gradle.model

interface Eval {

    /**
     * Return a number representing the version of this API.
     * Always increasing if changed.
     *
     * @return the version of this model.
     */
    val modelVersion: Long

    /**
     * Returns the module (Gradle project) name.
     *
     * @return the module name.
     */
    val name: String

    /**
     * Return the list of annotations.
     *
     * @return the list of annotations.
     */
    val annotations: List<String>

    /**
     * Return the list of prefixes.
     *
     * @return the list of prefixes.
     */
    val prefixes: List<String>

}