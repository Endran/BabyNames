/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames;

import java.util.ArrayList;
import java.util.List;

public class BabyNames {
    private int year = 0;
    private List<BabyName> names = new ArrayList<>();

    public BabyNames() {
    }

    public BabyNames(final int year, final List<BabyName> names) {
        this.year = year;
        this.names = names;
    }

    public int getYear() {
        return year;
    }

    public void setYear(final int year) {
        this.year = year;
    }

    public List<BabyName> getNames() {
        return names;
    }

    public void setNames(final List<BabyName> names) {
        this.names = names;
    }
}
