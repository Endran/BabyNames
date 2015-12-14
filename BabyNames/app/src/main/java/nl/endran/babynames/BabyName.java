/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames;

public class BabyName {
    private String name = "";
    private int count = 0;
    private int place = 0;

    public BabyName() {
    }

    public BabyName(final String name, final int count, final int place) {
        this.name = name;
        this.count = count;
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(final int place) {
        this.place = place;
    }
}
