package com.company.babysteps.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Week implements Parcelable {
    private long mId;
    private String mTitle;
    private String mImageUrl;
    private List<Post> mPosts;

    public Week() {
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

    public String getImageUrl() {
        return this.mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public List<Post> getPosts() {
        return this.mPosts;
    }

    public void setPosts(List<Post> posts) {
        this.mPosts = posts;
    }

    public void addPost(Post post) {
        if(this.mPosts == null) {
            this.mPosts = new ArrayList<>();
        }
        this.mPosts.add(post);
    }

    protected Week(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mImageUrl = in.readString();
        if (in.readByte() == 0x01) {
            mPosts = new ArrayList<>();
            in.readList(mPosts, Post.class.getClassLoader());
        } else {
            mPosts = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mTitle);
        dest.writeString(mImageUrl);
        if (mPosts == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mPosts);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Week> CREATOR = new Parcelable.Creator<Week>() {
        @Override
        public Week createFromParcel(Parcel in) {
            return new Week(in);
        }

        @Override
        public Week[] newArray(int size) {
            return new Week[size];
        }
    };
}