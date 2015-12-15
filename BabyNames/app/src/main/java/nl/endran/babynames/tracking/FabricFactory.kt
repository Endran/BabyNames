/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.tracking

import android.content.Context

import com.crashlytics.android.answers.Answers

import io.fabric.sdk.android.Fabric
import io.fabric.sdk.android.Kit
import javax.inject.Inject

class FabricFactory @Inject constructor() {

    fun create(context: Context, vararg kits: Kit<*>): Fabric {
        return Fabric.with(context, *kits)
    }

    val answers: Answers
        get() = Answers.getInstance()
}
