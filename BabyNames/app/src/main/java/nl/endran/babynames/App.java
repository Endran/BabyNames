/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames;

import android.app.Application;
import android.os.Handler;

import nl.endran.babynames.injections.AppComponent;
import nl.endran.babynames.injections.AppModule;
import nl.endran.babynames.injections.DaggerAppComponent;

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this, new Handler())).build();

        appComponent.getTracking().start(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
