/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import nl.endran.babynames.EPreference
import nl.endran.babynames.extensions.ifContainsThenMinusElsePlus
import nl.endran.babynames.names.BabyName
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.toObservable

class NamesFragmentPresenter constructor(
        val babyNameObservable: Observable<MutableList<BabyName>>,
        val favoritesPreference: EPreference<Set<String>>) {

    private var namesFragment: NamesFragment? = null
    private var favoriteSubscription: Subscription? = null

    fun start(namesFragment: NamesFragment) {
        this.namesFragment = namesFragment

        favoriteSubscription = favoritesPreference.asObservable()
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
        return favoritesPreference.get().contains(name)
    }

    fun toggleFavorite(name: String) {
        favoritesPreference.get().toObservable()
                .toList()
                .flatMap { it.ifContainsThenMinusElsePlus(name).toObservable() }
                .toList()
                .subscribe {
                    favoritesPreference
                            .asAction()
                            .call(it.toSet())
                }
    }
}
