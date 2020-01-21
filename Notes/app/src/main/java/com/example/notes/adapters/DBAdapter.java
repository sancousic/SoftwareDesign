package com.example.notes.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notes.helpers.DBHelper;
import com.example.notes.models.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBAdapter {
    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DBAdapter(Context context) {
        dbHelper = new DBHelper(context.getApplicationContext());
    }

    public DBAdapter open() {
        sqLiteDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    private Cursor getAllEntries() {
        String[] columns = new String[] {
                DBHelper.COLUMN_ID,
                DBHelper.COLUMN_TITLE,
                DBHelper.COLUMN_BODY,
                DBHelper.COLUMN_TAGS,
                DBHelper.COLUMN_DATE
        };
        return sqLiteDatabase.query(DBHelper.TABLE, columns, null,
                null, null, null, null);
    }

    public List<Note> getNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TITLE));
                String tags = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TAGS));
                String body = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BODY));
                String date = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DATE));
                notes.add(new Note(id, title, body, tags, date));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    private ContentValues getContentValues(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_TITLE, note.hasTitle() ? note.getTitle() : "");
        contentValues.put(DBHelper.COLUMN_BODY, note.getBody());
        contentValues.put(DBHelper.COLUMN_DATE,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(note.getCreatingDate()));
        contentValues.put(DBHelper.COLUMN_TAGS, note.getSringTags());
        return contentValues;
    }
    public long insert(Note note) {
        ContentValues contentValues = getContentValues(note);
        return sqLiteDatabase.insert(DBHelper.TABLE, null, contentValues);
    }

    public long delete(long id){
        return sqLiteDatabase.delete(DBHelper.TABLE,
                DBHelper.COLUMN_ID + '=' + id, null);
    }

    public long update(Note note) {
        ContentValues contentValues = getContentValues(note);
        return sqLiteDatabase.update(DBHelper.TABLE, contentValues,
                DBHelper.COLUMN_ID + '=' + note.getId(), null);
    }
}
