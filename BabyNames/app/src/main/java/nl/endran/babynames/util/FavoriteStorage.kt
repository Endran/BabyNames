/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.util

import com.f2prateek.rx.preferences.Preference
import nl.endran.babynames.extensions.ifContainsThenMinusElsePlus
import nl.endran.babynames.injections.AppModule
import rx.lang.kotlin.toObservable
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
open class FavoriteStorage @Inject constructor(@Named(AppModule.FAVORITE_STRING_SET_PREFERENCE) val favoriteStringSetPreference: Preference<Set<String>>) {

    val favoritesObservable = favoriteStringSetPreference.asObservable()

    fun isFavorite(name: String): Boolean {
        return favoriteStringSetPreference.get().contains(name)
    }

    fun toggleFavorite(name: String) {
        favoriteStringSetPreference.get().toObservable()
                .toList()
                .flatMap { it.ifContainsThenMinusElsePlus(name).toObservable() }
                .toList()
                .subscribe {
                    favoriteStringSetPreference
                            .asAction()
                            .call(it.toSet())
                }
    }
}
