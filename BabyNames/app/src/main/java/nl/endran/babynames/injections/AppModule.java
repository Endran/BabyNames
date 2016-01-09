/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.injections;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    public static final String FAVORITE_STRING_SET_PREFERENCE = "FAVORITE_STRING_SET_PREFERENCE";
    @NonNull
    private final Context context;

    @NonNull
    private final Handler handler;

    public AppModule(@NonNull final Context context, @NonNull final Handler handler) {
        this.context = context.getApplicationContext();
        this.handler = handler;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    public Handler provideHandler() {
        return handler;
    }

    @Singleton
    @Provides
    public Resources provideResources(@NonNull final Context context) {
        return context.getResources();
    }

    @Singleton
    @Provides
    @Named(FAVORITE_STRING_SET_PREFERENCE)
    public Preference<Set<String>> provideFavoriteStringSetPreference(@NonNull final Context context) {
        SharedPreferences preferences = context.getSharedPreferences("FAVORITE_STORAGE", Context.MODE_PRIVATE);
        final RxSharedPreferences rxSharedPreferences = RxSharedPreferences.create(preferences);
        return rxSharedPreferences.getStringSet("FAVORITE_KEYS", new HashSet<String>());
    }
}
