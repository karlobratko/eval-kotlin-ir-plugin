package hr.kbratko.eval

sealed interface EvalError {
    val message: String
}

data object ChildElementResultNotPresent : EvalError {
    override val message = "Result not present"
}

data object UninitializedVariable : EvalError {
    override val message = "Uninitialized variable"
}

data object VariableNotDefined : EvalError {
    override val message = "Variable not defined"
}

data object AllArgumentsAreNotConstants : EvalError {
    override val message = "Not all arguments are constants"
}

data object InsufficientNumberOfArguments : EvalError {
    override val message = "Insufficient number of arguments"
}

data object ArgumentsCouldNotBeEvaluated : EvalError {
    override val message = "Arguments could not be evaluated"
}

data object UnsupportedMethod : EvalError {
    override val message = "Unsupported method"
}

data object DivisionByZero : EvalError {
    override val message = "Division by zero"
}