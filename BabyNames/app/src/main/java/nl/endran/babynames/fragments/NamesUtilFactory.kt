/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import nl.endran.babynames.EPreference
import nl.endran.babynames.injections.AppModule
import nl.endran.babynames.names.BabyName
import nl.endran.babynames.names.BabyNameExtractor
import rx.Observable
import rx.lang.kotlin.toObservable
import rx.lang.kotlin.toSingletonObservable
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class NamesUtilFactory @Inject constructor(
        val babyNameExtractor: BabyNameExtractor,
        @Named(AppModule.FAVORITES_PREFERENCE) val favoritesPreference: EPreference<Set<String>>) {

    public enum class Type {
        ALPHABET, POPULARITY, FAVORITES
    }

    private fun getBabyNameObservable(type: Type): Observable<List<BabyName>> {
        var observable: Observable<List<BabyName>>

        when (type) {
            Type.ALPHABET ->
                observable = babyNameExtractor.observable
                        .toSortedList { name1, name2 -> name1.name.compareTo(name2.name) }
            Type.POPULARITY ->
                observable = babyNameExtractor.observable
                        .toSortedList { babyName1, babyName2 -> babyName1.place - babyName2.place }
            Type.FAVORITES ->
                observable = babyNameExtractor.observable
                        .filter { favoritesPreference.get().contains(it.name) }
                        .toSortedList { name1, name2 -> name1.name.compareTo(name2.name) }
            else -> throw IllegalAccessException("Invalid value of type $type")
        }

        return observable
    }

    fun createPresenter(type: Type): NamesFragmentPresenter {
        return NamesFragmentPresenter(
                getBabyNameObservable(type),
                favoritesPreference.asObservable(),
                favoritesPreference.asAction())
    }
}
