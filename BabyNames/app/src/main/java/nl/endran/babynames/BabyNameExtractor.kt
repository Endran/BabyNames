/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames

import android.content.res.Resources
import com.google.gson.Gson
import rx.lang.kotlin.toObservable
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class BabyNameExtractor @Inject constructor(val resources: Resources) {

    val babyNames by lazy {
        Gson().fromJson(
                BufferedReader(InputStreamReader(
                        resources.openRawResource(R.raw.boy_names))), BabyNames::class.java)
    }

    val babyNamesObservable by lazy {
        babyNames.names.toObservable()
    }
}
