/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class FavoriteStorage @Inject constructor(context: Context) {

    val preferences: SharedPreferences

    init {
        preferences = context.getSharedPreferences("FavoriteStorage", Context.MODE_PRIVATE)
    }

    fun isFavorite(name: String): Boolean {
        return preferences.getBoolean(name, false)
    }

    fun setFavorite(name: String, isFavorite: Boolean) {
        preferences.edit().putBoolean(name, isFavorite).apply()
    }
}
