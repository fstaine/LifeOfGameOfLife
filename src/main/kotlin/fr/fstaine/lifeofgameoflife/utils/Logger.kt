package fr.fstaine.lifeofgameoflife.utils

enum class LogLevel {
    VERBS,
    DEBUG,
    WARNG,
    ERROR,
    NONE,
}

object Logger {
    var level: LogLevel = LogLevel.VERBS

    @Synchronized
    private fun log(lvl: LogLevel, msg: Any) {
        if (level.ordinal <= lvl.ordinal) {
            val cls: String = Throwable().stackTrace[2].className.split(".").last()
            val method: String = Throwable().stackTrace[2].methodName
            println("[$lvl] [$cls] [$method]: $msg")
        }
    }

    fun v(msg: Any) {
        log(LogLevel.VERBS, msg)
    }

    fun d(msg: Any) {
        log(LogLevel.DEBUG, msg)
    }

    fun w(msg: Any) {
        log(LogLevel.WARNG, msg)
    }

    fun e(msg: Any) {
        log(LogLevel.ERROR, msg)
    }
}
