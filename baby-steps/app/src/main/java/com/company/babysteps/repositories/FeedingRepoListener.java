package com.company.babysteps.repositories;

import com.company.babysteps.entities.Feeding;

import java.util.List;

public interface FeedingRepoListener {
    void onGetFeedingsSuccess(List<Feeding> feedings);
    void onSingleCompleted();
}