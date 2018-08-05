package com.company.babysteps.utils;

import android.database.Cursor;

import com.company.babysteps.data.FeedingDBEntry;
import com.company.babysteps.data.GrowthDBEntry;
import com.company.babysteps.data.PostsDBEntry;
import com.company.babysteps.entities.Feeding;
import com.company.babysteps.entities.FeedingType;
import com.company.babysteps.entities.Growth;
import com.company.babysteps.entities.MilkType;
import com.company.babysteps.entities.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CursorUtil {
    private CursorUtil() {
    }

    public static List<Post> getPostsFromCursor(Cursor cursor) {
        List<Post> posts = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(PostsDBEntry._ID));
            String title = cursor.getString(cursor.getColumnIndex(PostsDBEntry.COLUMN_TITLE));
            String body = cursor.getString(cursor.getColumnIndex(PostsDBEntry.COLUMN_BODY));
            Post post = new Post();
            post.setId(id);
            post.setTitle(title);
            post.setBody(body);
            posts.add(post);
        }
        return posts;
    }

    public static Feeding getOneRowFeedingFromCursor(Cursor c) {
        long id = c.getLong(c.getColumnIndex(FeedingDBEntry._ID));
        String details = c.getString(c.getColumnIndex(FeedingDBEntry.COLUMN_DETAILS));
        String feedingType = c.getString(c.getColumnIndex(FeedingDBEntry.COLUMN_FEEDING_TYPE));
        long timestampStart = c.getLong(c.getColumnIndex(FeedingDBEntry.COLUMN_START_DATE_TIME));
        long timestampEnd = c.getLong(c.getColumnIndex(FeedingDBEntry.COLUMN_END_DATE_TIME));
        int quantity = c.getInt(c.getColumnIndex(FeedingDBEntry.COLUMN_QUANTITY));
        String milkType = c.getString(c.getColumnIndex(FeedingDBEntry.COLUMN_MILK_TYPE));
        return new Feeding(id, FeedingType.valueOf(feedingType), details, new Date(timestampStart), new Date(timestampEnd), quantity,
                milkType == null ? null : MilkType.valueOf(milkType));
    }

    public static Growth getOneRowGrothFromCursor(Cursor c) {
        long id = c.getLong(c.getColumnIndex(GrowthDBEntry._ID));
        long timestampDate = c.getLong(c.getColumnIndex(GrowthDBEntry.COLUMN_DATE));
        double weight = c.getDouble(c.getColumnIndex(GrowthDBEntry.COLUMN_WEIGHT));
        double height = c.getDouble(c.getColumnIndex(GrowthDBEntry.COLUMN_HEIGHT));
        double head = c.getDouble(c.getColumnIndex(GrowthDBEntry.COLUMN_HEAD));
        return new Growth(id, new Date(timestampDate), weight, height, head);
    }
}