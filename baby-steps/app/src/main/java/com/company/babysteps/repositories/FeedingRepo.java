package com.company.babysteps.repositories;

import com.company.babysteps.entities.Feeding;

public interface FeedingRepo {
    void getFeedings();
    void saveFeeding(Feeding feeding);
    void deleteFeeding(Feeding feeding);
    void updateFeeding(Feeding feeding);
}