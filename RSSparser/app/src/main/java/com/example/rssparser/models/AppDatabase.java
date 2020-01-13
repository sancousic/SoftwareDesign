package com.example.rssparser.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.rssparser.models.daos.FeedDao;
import com.example.rssparser.models.entities.Feed;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Feed.class}, version = 1)
@TypeConverters({TimestampConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;
    public static final String DB_NAME = "appDatabase.db";
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null)
            instance = create(context);
        return instance;
    }

    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
    }

    public abstract FeedDao feedDao();
}
