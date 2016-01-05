/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import com.f2prateek.rx.preferences.Preference
import nl.endran.babynames.names.BabyName
import nl.endran.babynames.util.FavoriteStorage
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

class NamesFragmentPresenter constructor(
        val babyNameObservable: Observable<MutableList<BabyName>>,
        val favoritesObservable: Preference<Set<String>>,
        val favoriteStorage: FavoriteStorage) {

    private var namesFragment: NamesFragment? = null
    private var favoriteSubscription: Subscription? = null

    fun start(namesFragment: NamesFragment) {
        this.namesFragment = namesFragment

        favoritesObservable
                .asObservable()
                .subscribe { updateUI() }

        updateUI()
    }

    fun stop() {
        favoriteSubscription?.unsubscribe()
        favoriteSubscription = null
        namesFragment = null
    }

    private fun updateUI() {
        babyNameObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    namesFragment?.showNames(it)
                }
        return
    }

    fun isFavorite(name: String): Boolean {
        return favoriteStorage.isFavorite(name)
    }

    fun toggleFavorite(name: String) {
        favoriteStorage.toggleFavorite(name)
    }
}
