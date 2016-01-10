/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import nl.endran.babynames.names.BabyName
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import rx.functions.Action1
import rx.lang.kotlin.toSingletonObservable

class NamesFragmentPresenterTest : Spek() {

    val RANDOM_NAME_1 = "RANDOM_NAME_1"
    val TEST_NAME = "TEST_NAME"
    val RANDOM_NAME_2 = "RANDOM_NAME_2"

    @Mock
    lateinit var favoritesPreferenceAction: Action1<in Set<String>>

    init {
        MockitoAnnotations.initMocks(this)

        val babyNameRandom1 = BabyName(RANDOM_NAME_1, 1, 11)
        val babyNameTest1 = BabyName(TEST_NAME, 2, 22)
        val babyNameRandom2 = BabyName(RANDOM_NAME_2, 3, 33)
        val babyNameObservable = listOf(babyNameRandom1, babyNameTest1, babyNameRandom2).toSingletonObservable()

        val setExcludingTestName = setOf(RANDOM_NAME_1, RANDOM_NAME_2)
        val setIncludingTestName = setOf(TEST_NAME, RANDOM_NAME_2)

        val favoritesPreferenceObservableExcludingTestName = setExcludingTestName.toSingletonObservable()
        val favoritesPreferenceObservableIncludingTestName = setIncludingTestName.toSingletonObservable()

        given ("a NamesFragmentPresenter with favorites excluding $TEST_NAME") {
            val presenter = NamesFragmentPresenter(babyNameObservable, favoritesPreferenceObservableExcludingTestName, favoritesPreferenceAction)

            on("asking if $TEST_NAME is a favorite") {
                val isNameFavorite = presenter.isFavorite(TEST_NAME)

                it("should return false") {
                    Assertions.assertThat(isNameFavorite).isFalse()
                }
            }

            on("toggling favorite for $TEST_NAME") {
                presenter.toggleFavorite(TEST_NAME)

                it("should append current favorites with $TEST_NAME") {
                    Mockito.verify(favoritesPreferenceAction).call(setExcludingTestName.plus(TEST_NAME))
                }
            }
        }

        given("a NamesFragmentPresenter with as favorites including $TEST_NAME") {
            val presenter = NamesFragmentPresenter(babyNameObservable, favoritesPreferenceObservableIncludingTestName, favoritesPreferenceAction)

            on("asking if $TEST_NAME is a favorite") {
                val isNameFavorite = presenter.isFavorite(TEST_NAME)

                it("should return true") {
                    Assertions.assertThat(isNameFavorite).isTrue()
                }
            }

            on("toggling favorite for $TEST_NAME") {
                presenter.toggleFavorite(TEST_NAME)

                it("should remove $TEST_NAME from favorites") {
                    Mockito.verify(favoritesPreferenceAction).call(setIncludingTestName.minus(TEST_NAME))
                }
            }
        }
    }
}
