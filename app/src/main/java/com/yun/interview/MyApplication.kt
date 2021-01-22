package com.yun.interview

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        app = this;
    }

    companion object{
        private lateinit var app:MyApplication
        fun getInstance(): MyApplication{
            return app
        }
    }
}