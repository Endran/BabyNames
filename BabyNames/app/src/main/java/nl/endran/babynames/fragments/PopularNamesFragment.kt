/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import nl.endran.babynames.injections.AppComponent
import nl.endran.babynames.names.BabyName
import rx.Observable

class PopularNamesFragment : NamesFragment() {

    override fun getBabyNameObservable(appComponent: AppComponent): Observable<List<BabyName>> {
        return  appComponent.babyNameExtractor.observable
                .toSortedList { babyName1, babyName2 -> babyName1.place - babyName2.place }
    }

    override fun getNamesAdapter() = PopularNamesAdapter()
}
