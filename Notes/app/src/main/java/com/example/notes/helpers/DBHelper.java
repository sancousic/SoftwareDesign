package com.example.notes.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes.db";
    private static final int DB_VERSION = 2;
    public static final String TABLE = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_TAGS = "tags";
    public static final String COLUMN_DATE = "date";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE + " ("
                + COLUMN_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE  + " TEXT, "
                + COLUMN_BODY  + " TEXT, "
                + COLUMN_TAGS  + " TEXT, "
                + COLUMN_DATE + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(sqLiteDatabase);
    }
}
