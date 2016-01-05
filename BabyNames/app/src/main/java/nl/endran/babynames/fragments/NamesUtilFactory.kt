/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import nl.endran.babynames.names.BabyName
import nl.endran.babynames.names.BabyNameExtractor
import nl.endran.babynames.util.FavoriteStorage
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

class NamesUtilFactory @Inject constructor(
        val babyNameExtractor: BabyNameExtractor,
        val favoriteStorage: FavoriteStorage) {

    public enum class Type {
        ALPHABET, POPULARITY, FAVORITES
    }

    private fun getBabyNameObservable(type: Type): Observable<MutableList<BabyName>> {
        var observable: Observable<MutableList<BabyName>>

        val babyNameObservable = babyNameExtractor.babyNamesObservable
                .subscribeOn(Schedulers.computation())

        when (type) {
            Type.ALPHABET ->
                observable = babyNameObservable
                        .toSortedList { name1, name2 -> name1.name.compareTo(name2.name) }
            Type.POPULARITY ->
                observable = babyNameObservable
                        .toSortedList { babyName1, babyName2 -> babyName1.place - babyName2.place }
            Type.FAVORITES ->
                observable = babyNameObservable
                        .filter { favoriteStorage.isFavorite(it.name) }
                        .toSortedList { name1, name2 -> name1.name.compareTo(name2.name) }
            else -> throw IllegalAccessException("Invalid value of type $type")
        }

        return observable
    }

    fun createPresenter(type: Type): NamesFragmentPresenter {
        return NamesFragmentPresenter(
                getBabyNameObservable(type),
                favoriteStorage.favoritesObservable,
                favoriteStorage)
    }

}
