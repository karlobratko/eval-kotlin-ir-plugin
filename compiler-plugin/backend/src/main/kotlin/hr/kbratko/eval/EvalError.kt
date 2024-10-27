package hr.kbratko.eval

sealed interface EvalError {
    val message: String
}

data object ResultNotPresent : EvalError {
    override val message = "Result not present"
}

data object AllArgumentsAreNotConstants : EvalError {
    override val message = "Not all arguments are constants"
}

data object DispatcherCouldNotBeEvaluated : EvalError {
    override val message = "Dispatcher could not be evaluated"
}

data object UnsupportedArgumentType : EvalError {
    override val message = "Unsupported argument type"
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