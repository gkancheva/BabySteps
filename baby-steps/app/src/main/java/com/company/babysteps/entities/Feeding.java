package com.company.babysteps.entities;

import android.content.Context;

import com.company.babysteps.R;
import com.company.babysteps.utils.StringFormatter;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Feeding extends SelectableEntity {
    private long mId;
    private FeedingType mType;
    private String mDetails;
    private Date mStartDateTime;
    private Date mEndDateTime;
    private double mQuantity;
    private MilkType mMilkType;

    public Feeding(long mId, FeedingType mType, String mDetails, Date mStartDateTime, Date mEndDateTime, double quantity, MilkType milkType) {
        this.mId = mId;
        this.mType = mType;
        this.mDetails = mDetails;
        this.mStartDateTime = mStartDateTime;
        this.mEndDateTime = mEndDateTime;
        this.mQuantity = quantity;
        this.mMilkType = milkType;
    }

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public FeedingType getType() {
        return this.mType;
    }

    public void setType(FeedingType type) {
        this.mType = type;
    }

    public String getDetails() {
        return this.mDetails;
    }

    public void setDetails(String details) {
        this.mDetails = details;
    }

    public Date getStartDateTime() {
        return this.mStartDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.mStartDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return this.mEndDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.mEndDateTime = endDateTime;
    }

    public double getQuantity() {
        return this.mQuantity;
    }

    public void setQuantity(double quantity) {
        this.mQuantity = quantity;
    }

    public MilkType getMilkType() {
        return this.mMilkType;
    }

    public void setMilkType(MilkType milkType) {
        this.mMilkType = milkType;
    }

    public String feedingToString(Context ctx) {
        switch (this.mType) {
            case BREAST_FEEDING:
                return this.getBreastFeedingToString(ctx);
            case BOTTLE_FEEDING:
                return this.getBottleFeedingToString(ctx);
        }
        return "";
    }

    private String getBottleFeedingToString(Context ctx) {
        // "12:30, Bottle feeding, 100ml, breast milk, other info"
        String formattedDateTime = StringFormatter.formatTime(this.getStartDateTime());
        String formattedQuantity = StringFormatter.formatQuantity(this.getQuantity(), ctx);
        String output = String.format(Locale.getDefault(),
                ctx.getString(R.string.format_bottle_feeding_item),
                formattedDateTime,
                this.getType().getName(ctx),
                formattedQuantity,
                this.getMilkType().getName(ctx));
        if(this.getDetails() != null) {
            output += ", " + this.getDetails();
        }
        return output;
    }

    private String getBreastFeedingToString(Context ctx) {
        // "12:30, Breast feeding, 10min, left breast"
        String formattedDateTime = StringFormatter.formatTime(this.getStartDateTime());
        long duration = TimeUnit.MILLISECONDS.toMinutes(this.getEndDateTime().getTime() - this.getStartDateTime().getTime());
        String output = String.format(Locale.getDefault(),
                ctx.getString(R.string.format_breast_feeding_item),
                formattedDateTime,
                this.getType().getName(ctx),
                duration);
        if(this.getDetails() != null) {
            output += ", " + this.getDetails();
        }
        return output;
    }
}