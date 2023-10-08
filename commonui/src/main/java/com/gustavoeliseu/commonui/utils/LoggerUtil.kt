package com.gustavoeliseu.commonui.utils

import com.gustavoeliseu.domain.BuildConfig
import com.gustavoeliseu.pokedex.utils.SafeCrashlyticsUtil


object LoggerUtil {
    fun printStackTraceOnlyInDebug(throwable: Throwable, canLog: Boolean = true){
        if(canLog)
            SafeCrashlyticsUtil.logException(throwable)
        if(BuildConfig.DEBUG)
            throwable.printStackTrace()
    }

    fun printlnOnlyInDebug(message: String){
        if(BuildConfig.DEBUG){
            println(message)
        }
    }
}