package com.company.babysteps.entities;

import java.util.Date;

public class Baby {
    private String mName;
    private Date mDateOfBirth;

    public Baby(String name, Date dateOfBirth) {
        this.mName = name;
        this.mDateOfBirth = dateOfBirth;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Date getDateOfBirth() {
        return this.mDateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.mDateOfBirth = dateOfBirth;
    }
}