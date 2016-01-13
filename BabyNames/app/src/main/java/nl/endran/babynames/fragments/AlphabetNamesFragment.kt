/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import nl.endran.babynames.injections.AppComponent
import nl.endran.babynames.names.BabyName
import rx.Observable

class AlphabetNamesFragment : NamesFragment() {

    override fun getBabyNameObservable(appComponent: AppComponent): Observable<List<BabyName>> {
        return appComponent.babyNameExtractor.observable
                .toSortedList { name1, name2 -> name1.name.compareTo(name2.name) }
    }

    override fun getNamesAdapter() = AlphabetNamesAdapter()
}
