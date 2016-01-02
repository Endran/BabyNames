/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.util

import android.content.Context
import android.content.SharedPreferences
import com.f2prateek.rx.preferences.RxSharedPreferences
import javax.inject.Inject

class FavoriteStorage @Inject constructor(context: Context) {

    val FAVORITE_KEYS = "FAVORITE_KEYS"
    val FAVORITE_STORAGE = "FAVORITE_STORAGE"
    val preferences: SharedPreferences
//    val favoritesObservable: SharedPreferences

    init {
        preferences = context.getSharedPreferences(FAVORITE_STORAGE, Context.MODE_PRIVATE)
        val rxSharedPreferences = RxSharedPreferences.create(preferences)
        val favoritesObservable = rxSharedPreferences.getStringSet(FAVORITE_KEYS)
    }

    fun isFavorite(name: String): Boolean {
        val favorites = getFavorites()
        return favorites?.contains(name) ?: false
    }


    fun toggleFavorite(name: String) {
        val favorites = getFavorites()
        if (isFavorite(name)) {
            favorites.remove(name)
        } else {
            favorites.add(name)
        }

        preferences.edit().putStringSet(FAVORITE_KEYS, favorites).apply()
    }

    private fun getFavorites() = preferences.getStringSet(FAVORITE_KEYS, hashSetOf())
}
