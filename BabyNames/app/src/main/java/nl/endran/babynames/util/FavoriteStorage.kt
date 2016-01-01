/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.util

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class FavoriteStorage @Inject constructor(context: Context) {

    val preferences: SharedPreferences

    init {
        preferences = context.getSharedPreferences("FavoriteStorage", Context.MODE_PRIVATE)
    }

    fun isFavorite(name: String): Boolean {
        val favorite = preferences.getBoolean(name, false)
        return favorite
    }

    fun toggleFavorite(name: String) {
        preferences.edit().putBoolean(name, !isFavorite(name)).apply()
    }
}
