/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.injections;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

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
}
