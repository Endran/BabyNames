/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import nl.endran.babynames.names.BabyName
import nl.endran.babynames.names.BabyNameExtractor
import nl.endran.babynames.util.FavoriteStorage
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.toObservable
import rx.schedulers.Schedulers
import javax.inject.Inject

class NamesFragmentPresenter @Inject constructor(
        val babyNameExtractor: BabyNameExtractor,
        val favoriteStorage: FavoriteStorage) {

    private var namesFragment: NamesFragment? = null
    private var type = NamesFragment.Type.POPULARITY
    private var favoriteSubscription: Subscription? = null

    fun start(namesFragment: NamesFragment, type: NamesFragment.Type) {
        this.namesFragment = namesFragment
        this.type = type

        favoriteStorage.favoritesObservable.asObservable()
                .subscribe { updateUI() }
        updateUI()
    }

    fun stop() {
        favoriteSubscription?.unsubscribe()
        favoriteSubscription = null
        namesFragment = null
    }

    private fun updateUI() {
        val names = babyNameExtractor.babyNames.names
        getObservableForType(names)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe {
                    namesFragment?.showNames(it)
                }
        return
    }

    private fun getObservableForType(names: MutableList<BabyName>): Observable<MutableList<String>>? {
        var observable: Observable<MutableList<String>>?

        val babyNameObservable = names.toObservable()
                .subscribeOn(Schedulers.computation())

        when (type) {
            NamesFragment.Type.ALPHABET ->
                observable = babyNameObservable
                        .map { it.name }
                        .toSortedList { name1, name2 -> name1.compareTo(name2) }
            NamesFragment.Type.POPULARITY ->
                observable = babyNameObservable
                        .toSortedList { babyName1, babyName2 -> babyName1.place - babyName2.place }
                        .flatMap { it.toObservable() }
                        .map { it.name }
                        .toList()
            NamesFragment.Type.FAVORITES ->
                observable = babyNameObservable
                        .map { it.name }
                        .filter { favoriteStorage.isFavorite(it) }
                        .toSortedList { name1, name2 -> name1.compareTo(name2) }
            else -> observable = null
        }

        return observable
    }

    fun isFavorite(name: String): Boolean {
        return favoriteStorage.isFavorite(name)
    }

    fun toggleFavorite(name: String) {
        favoriteStorage.toggleFavorite(name)
    }
}
