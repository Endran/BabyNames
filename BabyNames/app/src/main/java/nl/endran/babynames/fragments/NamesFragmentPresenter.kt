/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import nl.endran.babynames.extensions.ifContainsThenMinusElsePlus
import nl.endran.babynames.names.BabyName
import rx.Observable
import rx.Subscription
import rx.functions.Action1
import rx.lang.kotlin.BehaviourSubject
import rx.lang.kotlin.toObservable

class NamesFragmentPresenter constructor(
        val babyNameObservable: Observable<List<BabyName>>,
        val favoritesPreferenceObservable: Observable<Set<String>>,
        val favoritesPreferenceAction: Action1<in Set<String>>) {

    private var favoriteSubscription: Subscription? = null

    public val nameSubject = BehaviourSubject<List<BabyName>>()

    fun start() {
        favoriteSubscription = favoritesPreferenceObservable
                .subscribe {
                    informSubject()
                }

        informSubject()
    }

    private fun informSubject() {
        babyNameObservable
                .subscribe {
                    nameSubject.onNext(it)
                }
    }

    fun stop() {
        favoriteSubscription?.unsubscribe()
        favoriteSubscription = null
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
