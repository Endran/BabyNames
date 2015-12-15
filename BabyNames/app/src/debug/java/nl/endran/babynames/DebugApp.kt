/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames

import com.facebook.stetho.Stetho
import timber.log.Timber

class DebugApp : App() {

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        Timber.plant(Timber.DebugTree())
//        Timber.plant(StethoTree())
    }
}
