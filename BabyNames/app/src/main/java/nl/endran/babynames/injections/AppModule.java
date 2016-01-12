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

    public static final String FAVORITES_PREFERENCE = "FAVORITES_PREFERENCE";

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
    public SharedPreferences provideSharedPreferences(@NonNull final Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    @Singleton
    @Provides
    public RxSharedPreferences provideRxSharedPreferences(@NonNull final SharedPreferences sharedPreferences) {
        return RxSharedPreferences.create(sharedPreferences);
    }

    @Singleton
    @Provides
    @Named(FAVORITES_PREFERENCE)
    public Preference<Set<String>> provideFavoriteStringSetPreference(@NonNull final RxSharedPreferences rxSharedPreferences) {
        return rxSharedPreferences.getStringSet(FAVORITES_PREFERENCE, new HashSet<String>());
    }
}
