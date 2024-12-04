package com.example.template

import android.app.Application
import com.example.template.data.SharedPrefs

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPrefs.init(applicationContext)
    }

}