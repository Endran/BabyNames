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

enum class FavoriteState {
    NOPE, FAVORITE, DEPRECATED
}

data class BabyCollection(val year: Int, val names: List<Baby>);
data class Baby(val name: String, val count: Int, val place: Int, var favoriteState: FavoriteState = FavoriteState.NOPE);

class BabyExtractor @Inject constructor(val resources: Resources) {
    private val babies by lazy {
        Gson().fromJson(
                BufferedReader(InputStreamReader(
                        resources.openRawResource(R.raw.boy_names))), BabyCollection::class.java).names
    }

    val observable = this.toSingletonObservable()
            .subscribeOn(Schedulers.computation())
            .flatMap { it.babies.toObservable() }

}