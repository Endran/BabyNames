/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import nl.endran.babynames.names.BabyName
import nl.endran.babynames.names.BabyNameExtractor
import nl.endran.babynames.util.FavoriteStorage
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.toObservable
import javax.inject.Inject

class NamesFragmentPresenter @Inject constructor(
        val babyNameExtractor: BabyNameExtractor,
        val favoriteStorage: FavoriteStorage) {

    fun start(fragment: NamesFragment, type: NamesFragment.Type) {
        val names = babyNameExtractor.babyNames.names
        observable(type, names)
                ?.subscribeOn(AndroidSchedulers.mainThread())
                ?.subscribe { fragment.showNames(it) }
    }

    private fun observable(type: NamesFragment.Type, names: MutableList<BabyName>): Observable<MutableList<String>>? {
        var observable: Observable<MutableList<String>>?

        when (type) {
            NamesFragment.Type.ALPHABET ->
                observable = names.toObservable()
                        .map { it.name }
                        .toSortedList { name1, name2 -> name1.compareTo(name2) }
            NamesFragment.Type.POPULARITY ->
                observable = names.toObservable()
                        .toSortedList { babyName1, babyName2 -> babyName1.place - babyName2.place }
                        .flatMap { it.toObservable() }
                        .map { it.name }
                        .toList()
            NamesFragment.Type.FAVORITES ->
                observable = names.toObservable()
                        .map { it.name }
                        .filter { favoriteStorage.isFavorite(it) }
                        .toSortedList { name1, name2 -> name1.compareTo(name2) }
            else -> observable = null
        }

        return observable
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
