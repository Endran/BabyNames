/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.names

import android.content.res.Resources
import com.google.gson.Gson
import nl.endran.babynames.R
import rx.lang.kotlin.toObservable
import rx.lang.kotlin.toSingletonObservable
import rx.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BabyNameExtractor @Inject constructor(val resources: Resources) {

    private val babyNames by lazy {
        Gson().fromJson(
                BufferedReader(InputStreamReader(
                        resources.openRawResource(R.raw.boy_names))), BabyNames::class.java)
    }

    val observable = this.toSingletonObservable()
            .subscribeOn(Schedulers.computation())
            .flatMap { it.babyNames.names.toObservable() }
}
