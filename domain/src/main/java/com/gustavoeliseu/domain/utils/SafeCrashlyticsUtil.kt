package com.gustavoeliseu.pokedex.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.net.ConnectException
import java.net.UnknownHostException

object SafeCrashlyticsUtil {

    fun logException(throwable: Throwable){
        try{
            if(canLog(throwable))
                FirebaseCrashlytics.getInstance().recordException(throwable)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun logException(canSendLog: Boolean, throwable: Throwable){
        if(canSendLog){
            logException(throwable)
        }
    }

    private fun canLog(throwable: Throwable): Boolean {
        return throwable !is UnknownHostException
                && throwable !is ConnectException
        //&& throwable !is HttpException
    }
}