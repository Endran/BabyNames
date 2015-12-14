/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames

import android.content.res.Resources
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

class BabyNameExtractor {

    fun extract(resources: Resources): BabyNames {
        val raw = resources.openRawResource(R.raw.boy_names);
        val reader = BufferedReader(InputStreamReader(raw));
        val babyNames = Gson().fromJson(reader, BabyNames::class.java)
        return babyNames
    }
}
