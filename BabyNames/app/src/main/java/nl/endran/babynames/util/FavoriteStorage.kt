/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.util

import android.content.Context
import com.f2prateek.rx.preferences.Preference
import com.f2prateek.rx.preferences.RxSharedPreferences
import rx.lang.kotlin.toObservable
import javax.inject.Inject

class FavoriteStorage @Inject constructor(context: Context) {

    val favoritesObservable: Preference<Set<String>>

    init {
        val preferences = context.getSharedPreferences("FAVORITE_STORAGE", Context.MODE_PRIVATE)
        val rxSharedPreferences = RxSharedPreferences.create(preferences)
        favoritesObservable = rxSharedPreferences.getStringSet("FAVORITE_KEYS", hashSetOf(""))
    }

    fun isFavorite(name: String): Boolean {
        return favoritesObservable.get().contains(name)
    }

    fun toggleFavorite(name: String) {
        var favorites = favoritesObservable.get()

        favorites.toObservable()
                .contains(name)
                .subscribe {
                    favorites = if (it) favorites.minus(name) else favorites.plus(name)
                    favoritesObservable.set(favorites)
                }
    }
}
