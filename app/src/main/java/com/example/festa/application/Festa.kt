package com.example.festa.application

import android.app.Application
import android.content.res.Configuration
import com.example.food_app.utils.PreferenceManager

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Festa : Application(){
    companion object {
        lateinit var encryptedPrefs: PreferenceManager
        lateinit var instance: Festa
    }

    override fun onCreate() {
        super.onCreate()
        encryptedPrefs = PreferenceManager(applicationContext).getInstance(applicationContext)
        instance = this

    }

    fun isDarkThemeOn(): Boolean {
        return resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}