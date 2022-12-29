package com.example.spotifydemo

import android.app.Application

class SpotifyDemoApp : Application() {

    companion object {
        private lateinit var instance: Application

        fun getInstance(): Application {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}