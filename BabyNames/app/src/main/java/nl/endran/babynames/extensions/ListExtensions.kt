/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.extensions

fun <E> List<E>.ifContainsThenMinusElsePlus(item: E): List<E> {
    if (contains(item)) {
        return minus(item)
    } else {
        return plus(item)
    }
}