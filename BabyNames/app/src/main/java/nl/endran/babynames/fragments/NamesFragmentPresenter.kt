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
import rx.functions.Action1
import rx.lang.kotlin.toObservable

class NamesFragmentPresenter constructor(
        val babyNameObservable: Observable<List<BabyName>>,
        val favoritesPreferenceObservable: Observable<Set<String>>,
        val favoritesPreferenceAction: Action1<in Set<String>>) {

    private var namesFragment: NamesFragment? = null
    private var favoriteSubscription: Subscription? = null

    fun start(namesFragment: NamesFragment) {
        this.namesFragment = namesFragment

        favoriteSubscription = favoritesPreferenceObservable
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
    }

    fun isFavorite(name: String): Boolean {
        var res = false
        favoritesPreferenceObservable
                .take(1)
                .flatMap { it.toObservable() }
                .contains(name)
                .subscribe { res = it }
        return res
    }

    fun toggleFavorite(name: String) {
        favoritesPreferenceObservable
                .take(1)
                .flatMap { it.toObservable() }
                .toList()
                .map { it.ifContainsThenMinusElsePlus(name).toSet() }
                .subscribe { favoritesPreferenceAction.call(it) }
    }
}
