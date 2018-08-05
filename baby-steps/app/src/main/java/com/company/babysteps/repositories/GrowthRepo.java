package com.company.babysteps.repositories;

import com.company.babysteps.entities.Growth;

public interface GrowthRepo {
    void saveGrowth(Growth growth);
    void updateGrowth(Growth growth);
    void deleteGrowth(Growth growth);
    void getGrowthList();
}