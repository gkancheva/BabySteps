package com.company.babysteps.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    private long mId;
    private String mTitle;
    private String mBody;

    public Post() {
    }

    public Post(long mId, String mTitle) {
        this.mId = mId;
        this.mTitle = mTitle;
    }

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getBody() {
        return this.mBody;
    }

    public void setBody(String body) {
        this.mBody = body;
    }

    public String getReducedBody() {
        int endIndex = Math.min(150, this.getBody().length());
        return this.getBody().substring(0, endIndex);
    }

    protected Post(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mBody = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mTitle);
        dest.writeString(mBody);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}