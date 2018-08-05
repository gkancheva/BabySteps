package com.company.babysteps.services;

import com.company.babysteps.entities.Week;

public interface PostRepoListener {
    void onPostSuccess(Week week);
    void onPostFailure(String message);
}