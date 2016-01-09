/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import nl.endran.babynames.EPreference
import nl.endran.babynames.names.BabyName
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import rx.Observable

class NamesFragmentPresenterTest : Spek() {

    @Mock
    lateinit var babyNameObservableMock: Observable<MutableList<BabyName>>

    @Mock
    lateinit var favoritesPreferenceMock: EPreference<Set<String>>

    val TEST_NAME = "TEST_NAME"

    init {
        MockitoAnnotations.initMocks(this)

        given ("a NamesFragmentPresenter with empty favorites") {
            val presenter = NamesFragmentPresenter(babyNameObservableMock, favoritesPreferenceMock)

            on("asking if $TEST_NAME is a favorite") {
                val isNameFavorite = presenter.isFavorite(TEST_NAME)

                it("should return false") {
                    Assertions.assertThat(isNameFavorite).isFalse()
                }
            }
        }

        given("a NamesFragmentPresenter with pre filled favorites") {
            Mockito.`when`(favoritesPreferenceMock.get()).thenReturn(hashSetOf(TEST_NAME))
            val presenter = NamesFragmentPresenter(babyNameObservableMock, favoritesPreferenceMock)

            on("asking if $TEST_NAME is a favorite") {
                val isNameFavorite = presenter.isFavorite(TEST_NAME)

                it("should return true") {
                    Assertions.assertThat(isNameFavorite).isTrue()
                }
            }
        }
    }
}