package com.example.rssparser.models.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.rssparser.models.entities.Feed;

import java.util.List;

@Dao
public interface FeedDao {
    @Query("SELECT * from feed WHERE feed_url=:feed_url")
    List<Feed> selectAll(String feed_url);

    @Insert
    void  insert(Feed feed);

    @Delete
    void delete(Feed feed);

    @Query("DELETE FROM feed WHERE feed_url=:feed_url")
    void deleteAll(String feed_url);
}
