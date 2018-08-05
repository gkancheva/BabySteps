package com.company.babysteps.entities;

import android.content.Context;

import com.company.babysteps.R;

import java.util.ArrayList;
import java.util.List;

public enum MilkType {
    BREAST_MILK(R.string.breast_milk), FORMULA(R.string.formula);

    private int mValue;

    MilkType(int value) {
        this.mValue = value;
    }

    public String getName(Context ctx) {
        return ctx.getString(mValue);
    }

    public static List<String> getValues(Context ctx) {
        List<String> arr = new ArrayList<>();
        for (MilkType type : MilkType.values()) {
            arr.add(ctx.getString(type.mValue));
        }
        return arr;
    }

    public static MilkType getMilkType(String milkType, Context ctx) {
        if(milkType.equals(ctx.getString(R.string.breast_milk))) {
            return MilkType.BREAST_MILK;
        }
        return MilkType.FORMULA;
    }
}