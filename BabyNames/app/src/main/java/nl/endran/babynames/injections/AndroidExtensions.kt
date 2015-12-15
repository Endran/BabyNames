/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.injections

import android.content.Context
import nl.endran.babynames.App

fun Context.getAppComponent(): AppComponent {
    return (applicationContext as App).appComponent
}
