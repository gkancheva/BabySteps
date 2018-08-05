package com.company.babysteps.repositories;

import com.company.babysteps.entities.Week;

public interface PostRepo {
    void getPostsForWeekFromTheWeb(int[] age);
    void getWeekWithPostsFromDB();
    void saveWeekLocally(Week week);
}