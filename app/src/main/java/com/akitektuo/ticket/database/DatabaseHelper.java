package com.akitektuo.ticket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by AoD Akitektuo on 21-Apr-17 at 17:57.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_QUERY = "CREATE TABLE " + DatabaseContract.MessageContractEntry.TABLE_NAME + " (" +
            DatabaseContract.MessageContractEntry.COLUMN_ERROR + " TEXT," +
            DatabaseContract.MessageContractEntry.COLUMN_TYPE + " NUMBER," +
            DatabaseContract.MessageContractEntry.COLUMN_TEXT + " TEXT," +
            DatabaseContract.MessageContractEntry.COLUMN_DAY + " NUMBER," +
            DatabaseContract.MessageContractEntry.COLUMN_MONTH + " NUMBER," +
            DatabaseContract.MessageContractEntry.COLUMN_YEAR + " NUMBER," +
            DatabaseContract.MessageContractEntry.COLUMN_HOUR + " NUMBER," +
            DatabaseContract.MessageContractEntry.COLUMN_MINUTE + " NUMBER" + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addMessage(boolean error, int type, String text, int day, int month, int year, int hour, int minute) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.MessageContractEntry.COLUMN_ERROR, String.valueOf(error));
        contentValues.put(DatabaseContract.MessageContractEntry.COLUMN_TYPE, type);
        contentValues.put(DatabaseContract.MessageContractEntry.COLUMN_TEXT, text);
        contentValues.put(DatabaseContract.MessageContractEntry.COLUMN_DAY, day);
        contentValues.put(DatabaseContract.MessageContractEntry.COLUMN_MONTH, month);
        contentValues.put(DatabaseContract.MessageContractEntry.COLUMN_YEAR, year);
        contentValues.put(DatabaseContract.MessageContractEntry.COLUMN_HOUR, hour);
        contentValues.put(DatabaseContract.MessageContractEntry.COLUMN_MINUTE, minute);
        getWritableDatabase().insert(DatabaseContract.MessageContractEntry.TABLE_NAME, null, contentValues);
    }

    public Cursor getMessages() {
        String[] list = {DatabaseContract.MessageContractEntry.COLUMN_ERROR,
                DatabaseContract.MessageContractEntry.COLUMN_TYPE,
                DatabaseContract.MessageContractEntry.COLUMN_TEXT,
                DatabaseContract.MessageContractEntry.COLUMN_DAY,
                DatabaseContract.MessageContractEntry.COLUMN_MONTH,
                DatabaseContract.MessageContractEntry.COLUMN_YEAR,
                DatabaseContract.MessageContractEntry.COLUMN_HOUR,
                DatabaseContract.MessageContractEntry.COLUMN_MINUTE};
        return getReadableDatabase().query(DatabaseContract.MessageContractEntry.TABLE_NAME, list, null, null, null, null, null);
    }

    public void deleteMessage(String text, int day, int month, int year, int hour, int minute) {
        String selection = DatabaseContract.MessageContractEntry.COLUMN_TEXT + " LIKE ? AND " +
                DatabaseContract.MessageContractEntry.COLUMN_DAY + " LIKE ? AND " +
                DatabaseContract.MessageContractEntry.COLUMN_MONTH + " LIKE ? AND " +
                DatabaseContract.MessageContractEntry.COLUMN_YEAR + " LIKE ? AND " +
                DatabaseContract.MessageContractEntry.COLUMN_HOUR + " LIKE ? AND " +
                DatabaseContract.MessageContractEntry.COLUMN_MINUTE + " LIKE ?";
        String[] selectionArgs = {text, String.valueOf(day), String.valueOf(month), String.valueOf(year), String.valueOf(hour), String.valueOf(minute)};
        getWritableDatabase().delete(DatabaseContract.MessageContractEntry.TABLE_NAME, selection, selectionArgs);
    }

}
