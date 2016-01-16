/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.injections;

import com.f2prateek.rx.preferences.Preference;

import java.util.Set;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import nl.endran.babynames.App;
import nl.endran.babynames.MainActivity;
import nl.endran.babynames.names.BabyExtractor;
import nl.endran.babynames.tracking.Tracking;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(App app);

    void inject(MainActivity mainActivity);

    Tracking getTracking();

    BabyExtractor getBabyExtractor();

    @Named(AppModule.FAVORITES_PREFERENCE)
    Preference<Set<String>> getFavoritesPreference();
}
