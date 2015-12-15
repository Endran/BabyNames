/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.tracking

import android.content.Context

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.ContentViewEvent
import com.crashlytics.android.answers.CustomEvent
import com.crashlytics.android.answers.ShareEvent
import javax.inject.Inject


class Tracking @Inject constructor(val fabricFactory: FabricFactory) {

    fun start(context: Context) {
        fabricFactory.create(context, Crashlytics())
    }

    fun logContentView(event: ContentViewEvent) {
        fabricFactory.answers.logContentView(event)
    }

    fun logCustom(event: CustomEvent) {
        fabricFactory.answers.logCustom(event)
    }

    fun logShare(event: ShareEvent) {
        fabricFactory.answers.logShare(event)
    }
}
