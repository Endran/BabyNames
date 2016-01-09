/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import com.f2prateek.rx.preferences.Preference
import nl.endran.babynames.EPreference
import nl.endran.babynames.injections.AppModule
import nl.endran.babynames.names.BabyName
import nl.endran.babynames.names.BabyNameExtractor
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class NamesUtilFactory @Inject constructor(
        val babyNameExtractor: BabyNameExtractor,
        @Named(AppModule.FAVORITES_PREFERENCE) val favoritesPreference: EPreference<Set<String>>) {

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
                        .filter { favoritesPreference.get().contains(it.name) }
                        .toSortedList { name1, name2 -> name1.name.compareTo(name2.name) }
            else -> throw IllegalAccessException("Invalid value of type $type")
        }

        return observable
    }

    fun createPresenter(type: Type): NamesFragmentPresenter {
        return NamesFragmentPresenter(
                getBabyNameObservable(type),
                favoritesPreference)
    }
}
