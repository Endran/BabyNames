/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.util

import com.f2prateek.rx.preferences.Preference
import nl.endran.babynames.injections.AppModule
import rx.lang.kotlin.toObservable
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
open class FavoriteStorage @Inject constructor(@Named(AppModule.FAVORITE_STRING_SET_PREFERENCE) val favoriteStringSetPreference: Preference<Set<String>>) {

    val favoritesObservable by lazy { favoriteStringSetPreference.asObservable() }

    fun isFavorite(name: String): Boolean {
        return favoriteStringSetPreference.get().contains(name)
    }

    fun toggleFavorite(name: String) {
        var favorites = favoriteStringSetPreference.get()

        favorites.toObservable()
                .contains(name)
                .subscribe {
                    favorites = if (it) favorites.minus(name) else favorites.plus(name)
                    favoriteStringSetPreference.set(favorites)
                }
    }
}
