package com.example.androidappauthentication.utils

import android.app.Application
import android.content.Context

class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
        mApplicationClass = this
    }

    companion object {

        @JvmStatic
        private lateinit var mApplicationClass: ApplicationClass

        @JvmStatic
        val application: ApplicationClass by lazy { mApplicationClass }

        fun getContext(): Context {
            return application.baseContext
        }
    }
}