/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames;

import android.content.Context;
import android.util.AttributeSet;

import xyz.danoz.recyclerviewfastscroller.sectionindicator.title.SectionTitleIndicator;

public class SectionIndicator extends SectionTitleIndicator<Object> {

    public SectionIndicator(Context context) {
        super(context);
    }

    public SectionIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SectionIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setSection(final Object object) {
        setTitleText(object.toString());
//        setTitleText(colorGroup.getName().charAt(0) + "");

        // Example of using a longer string
        // setTitleText(colorGroup.getName());

//        setIndicatorTextColor(colorGroup.getAsColor());
    }
}