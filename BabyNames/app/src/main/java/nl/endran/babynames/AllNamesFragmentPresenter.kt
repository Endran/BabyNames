/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames

import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.toObservable
import javax.inject.Inject

class AllNamesFragmentPresenter @Inject constructor(
        val babyNameExtractor: BabyNameExtractor,
        val favoriteStorage: FavoriteStorage) {

    fun start(fragment: AllNamesFragment) {
        val names = babyNameExtractor.babyNames.names
        names.toObservable()
                .map { it.name }
                .toSortedList { name1, name2 -> name1.compareTo(name2) }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { fragment.showNames(it) }
    }

    fun stop() {
    }

    fun isFavorite(name: String): Boolean {
        return favoriteStorage.isFavorite(name)
    }

    fun toggleFavorite(name: String) {
        favoriteStorage.toggleFavorite(name)
    }
}
