package com.company.babysteps.entities;

import android.content.Context;

import com.company.babysteps.R;

public enum  FeedingType {
    BREAST_FEEDING(R.string.breast_feeding),
    BOTTLE_FEEDING(R.string.bottle_feeding);

    private int mValue;

    FeedingType(int value) {
        this.mValue = value;
    }

    public String getName(Context ctx) {
        return ctx.getString(this.mValue);
    }

}