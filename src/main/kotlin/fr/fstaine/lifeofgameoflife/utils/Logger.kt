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
    private fun log(lvl: LogLevel, msg: String) {
        if (level.ordinal <= lvl.ordinal) {
            val cls: String = Throwable().stackTrace[2].className.split(".").last()
            val method: String = Throwable().stackTrace[2].methodName
            println("[$lvl] [$cls] [$method]: $msg")
        }
    }

    fun v(msg: String) {
        log(LogLevel.VERBS, msg)
    }

    fun d(msg: String) {
        log(LogLevel.DEBUG, msg)
    }

    fun w(msg: String) {
        log(LogLevel.WARNG, msg)
    }

    fun e(msg: String) {
        log(LogLevel.ERROR, msg)
    }
}
