package com.mindtrust.scutinativesdk

import android.R.string
import android.util.Log


class ScutiLogger private constructor() {
    companion object {
        @Volatile
        private var instance: ScutiLogger? = null

        private var pLogSettings: LogSettings = LogSettings.ERROR_ONLY
        var logSettings: LogSettings = pLogSettings
            get() = pLogSettings

        fun getInstance(): ScutiLogger {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = ScutiLogger()
                    }
                }
            }
            return instance!!
        }
    }

    fun setLogSettings(logSettings: LogSettings){
        pLogSettings = logSettings
    }

    fun log(message: String) {
        if (logSettings.ordinal >= LogSettings.VERBOSE.ordinal)  Log.d("INFO", message)
    }

    fun logWarning(message: String) {
        if (logSettings.ordinal >= LogSettings.VERBOSE.ordinal)  Log.d("WARNING", message)
    }

    fun logError(message: String) {
        if (logSettings.ordinal >= LogSettings.ERROR_ONLY.ordinal)  Log.d("ERROR", message)
    }

}