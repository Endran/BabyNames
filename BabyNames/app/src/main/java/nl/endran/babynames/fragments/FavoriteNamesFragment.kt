/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import nl.endran.babynames.injections.AppComponent
import nl.endran.babynames.names.Baby
import rx.Observable

class FavoriteNamesFragment : NamesFragment() {

    override fun getBabiesObservable(appComponent: AppComponent): Observable<List<Baby>> {
        return appComponent.babyExtractor.observable
                .filter { appComponent.favoritesPreference.get().contains(it.name) }
                .toSortedList { baby1, baby2 -> baby1.name.compareTo(baby2.name) }
    }

    override fun getNamesAdapter() = PopularNamesAdapter()
}
