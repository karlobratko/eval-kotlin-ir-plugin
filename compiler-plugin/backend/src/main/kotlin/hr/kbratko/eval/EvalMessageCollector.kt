package hr.kbratko.eval

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector

class EvalMessageCollector(private val messageCollector: MessageCollector) : MessageCollector by messageCollector {
    override fun report(severity: CompilerMessageSeverity, message: String, location: CompilerMessageSourceLocation?) {
        messageCollector.report(severity, "[EVAL]: $message", location)
    }

    fun reportInfo(message: String) {
        report(CompilerMessageSeverity.INFO, message)
    }

    fun reportWarn(message: String) {
        report(CompilerMessageSeverity.WARNING, message)
    }
}