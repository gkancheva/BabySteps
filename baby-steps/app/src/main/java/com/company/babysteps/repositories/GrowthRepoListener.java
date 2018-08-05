package com.company.babysteps.repositories;

import com.company.babysteps.entities.Growth;

import java.util.List;

public interface GrowthRepoListener {
    void onGetGrowthsSuccess(List<Growth> growthList);

    void onSingleCompleted();
}