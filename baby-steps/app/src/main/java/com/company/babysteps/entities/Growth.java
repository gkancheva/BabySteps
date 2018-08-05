package com.company.babysteps.entities;

import java.util.Date;

public class Growth extends SelectableEntity {
    private long mId;
    private Date mDate;
    private double mWeight;
    private double mHeight;
    private double mHead;

    public Growth(Date date, double weight, double height, double head) {
        this.mDate = date;
        this.mWeight = weight;
        this.mHeight = height;
        this.mHead = head;
    }

    public Growth(long id, Date date,double weight, double height, double head) {
        this(date, weight, height, head);
        this.setId(id);
    }

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public Date getDate() {
        return this.mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public double getWeight() {
        return this.mWeight;
    }

    public void setWeight(double weight) {
        this.mWeight = weight;
    }

    public double getHeight() {
        return this.mHeight;
    }

    public void setHeight(double height) {
        this.mHeight = height;
    }

    public double getHead() {
        return this.mHead;
    }

    public void setHead(double head) {
        this.mHead = head;
    }
}