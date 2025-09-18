package com.hadify.omnicast

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * OmniCast Application class
 * Sets up dependency injection and app-wide initialization
 */
@HiltAndroidApp
class OmniCastApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // App initialization will go here
        // For now, Hilt handles all our dependency injection
    }
}