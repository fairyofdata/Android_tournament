package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myAppDatabase.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createMainTableSQL = "CREATE TABLE IF NOT EXISTS mainTable (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tableName TEXT)";
        db.execSQL(createMainTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Keep it empty for now
    }

    public long insertIntoMainTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tableName", tableName);
        return db.insert("mainTable", null, contentValues);
    }
}
