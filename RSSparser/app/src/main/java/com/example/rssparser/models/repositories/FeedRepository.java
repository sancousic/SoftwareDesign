package com.example.rssparser.models.repositories;

import android.app.Application;

import com.example.rssparser.models.AppDatabase;
import com.example.rssparser.models.daos.FeedDao;
import com.example.rssparser.models.entities.Feed;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FeedRepository {
    private FeedDao feedDao;

    public FeedRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        feedDao = database.feedDao();
    }

    public List<Feed> selectAll(String feedUrl) {
        Future<List<Feed>> future = AppDatabase.databaseWriteExecutor.submit(() ->
                feedDao.selectAll(feedUrl));
        try {
            return future.get();
        }
        catch (ExecutionException | InterruptedException exception) {
            return null;
        }
    }

    public void insert(Feed feed) {
        AppDatabase.databaseWriteExecutor.submit(() ->
                feedDao.insert(feed));
    }

    public void deleteAll(String feedUrl) {
        Future future = AppDatabase.databaseWriteExecutor.submit(() ->
                feedDao.deleteAll(feedUrl));

        try {
            future.get();
        }
        catch (ExecutionException | InterruptedException exception)
        {
            System.out.println(exception.getMessage());
        }
    }
}
