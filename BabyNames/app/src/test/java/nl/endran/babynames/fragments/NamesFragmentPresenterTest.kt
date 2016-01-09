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
import rx.lang.kotlin.toObservable

class NamesFragmentPresenterTest : Spek() {

    @Mock
    lateinit var babyNameObservableMock: Observable<MutableList<BabyName>>

    @Mock
    lateinit var favoritesPreferenceMock: EPreference<Set<String>>

    @Mock
    lateinit var stringSetMock: Set<String>

    @Mock
    lateinit var favoritesObservableMock: Observable<String>

    val TEST_NAME = "TEST_NAME"

    init {
        MockitoAnnotations.initMocks(this)

        given ("a NamesFragmentPresenter with favorites excluding $TEST_NAME") {

            Mockito.`when`(favoritesPreferenceMock.get()).thenReturn(hashSetOf("a", "b"))
            val presenter = NamesFragmentPresenter(babyNameObservableMock, favoritesPreferenceMock)

            on("asking if $TEST_NAME is a favorite") {
                val isNameFavorite = presenter.isFavorite(TEST_NAME)

                it("should return false") {
                    Assertions.assertThat(isNameFavorite).isFalse()
                }
            }
        }

        given("a NamesFragmentPresenter with as favorites only $TEST_NAME") {
            Mockito.`when`(favoritesPreferenceMock.get()).thenReturn(stringSetMock)
            Mockito.`when`(stringSetMock.contains(TEST_NAME)).thenReturn(true)
//            Mockito.`when`(stringSetMock.toObservable()).thenReturn(favoritesObservableMock)

            val presenter = NamesFragmentPresenter(babyNameObservableMock, favoritesPreferenceMock)

            on("asking if $TEST_NAME is a favorite") {
                val isNameFavorite = presenter.isFavorite(TEST_NAME)

                it("should return true") {
                    Assertions.assertThat(isNameFavorite).isTrue()
                }
            }
            //
            //            on("toggling $TEST_NAME") {
            //                presenter.toggleFavorite(TEST_NAME)
            //
            //                it("should save an empty set") {
            //

            //            InOrder inOrder = inOrder(firstMock, secondMock);
            //
            //            //following will make sure that firstMock was called before secondMock
            //            inOrder.verify(firstMock).methodOne();
            //            inOrder.verify(secondMock).methodTwo();


            //                }
            //            }
        }
    }
}