/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f2prateek.rx.preferences.Preference;

import rx.Observable;
import rx.functions.Action1;

public class EPreference<T> {

    @NonNull
    private final Preference<T> preference;

    private EPreference(@NonNull final Preference<T> preference) {
        this.preference = preference;
    }

    public static <T> EPreference<T> create(Preference<T> preference) {
        return new EPreference<>(preference);
    }

    @NonNull
    public String key() {
        return preference.key();
    }

    @Nullable
    public T defaultValue() {
        return preference.defaultValue();
    }

    @Nullable
    public T get() {
        return preference.get();
    }

    public void set(@Nullable T value) {
        preference.set(value);
    }

    public boolean isSet() {
        return preference.isSet();
    }

    public void delete() {
        preference.delete();
    }

    @CheckResult
    @NonNull
    public Observable<T> asObservable() {
        return preference.asObservable();
    }

    @CheckResult
    @NonNull
    public Action1<? super T> asAction() {
        return preference.asAction();
    }
}
