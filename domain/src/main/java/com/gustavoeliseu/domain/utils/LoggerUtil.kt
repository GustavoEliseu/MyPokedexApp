package com.gustavoeliseu.domain.utils

import com.gustavoeliseu.myapplication.BuildConfig


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