package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.util.Pair;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import java.io.ByteArrayOutputStream;

import kotlin.Triple;

public class DataHandler {
    private String tableName;
    private DBHelper dbHelper;

    public DataHandler(Context ctx, String tableName) {
        this.tableName = tableName;
        this.dbHelper = new DBHelper(ctx);
    }

    public void createTable() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "image BLOB," +
                "content TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    public long insertData(Bitmap image, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("image", getBytes(image));
        contentValues.put("content", content);
        return db.insert(tableName, null, contentValues);
    }

    private byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
    public List<Triple<Integer, Bitmap, String>> getAllData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Log.d("DataHandler", "Database opened for reading.");

        String query = "SELECT _id, image, content FROM " + tableName;
        Cursor cursor = db.rawQuery(query, null);
        Log.d("DataHandler", "Query executed: " + query);

        List<Triple<Integer, Bitmap, String>> dataList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Log.d("DataHandler", "Processing a new record.");

                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("_id"));
                Log.d("DataHandler", "ID retrieved: " + id);

                @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
                Log.d("DataHandler", "Image bytes retrieved.");

                Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                Log.d("DataHandler", "Image decoded.");

                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                Log.d("DataHandler", "Content retrieved: " + content);

                dataList.add(new Triple<>(id, image, content));
                Log.d("DataHandler", "Record added to the list.");
            } while (cursor.moveToNext());
        } else {
            Log.d("DataHandler", "No records found.");
        }

        cursor.close();
        db.close();
        Log.d("DataHandler", "Database closed.");

        return dataList;
    }

    public Bitmap getImageByText(String content) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT image FROM " + tableName + " WHERE content = ?";
        Cursor cursor = db.rawQuery(query, new String[]{content});

        Bitmap image = null;

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
            image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }

        cursor.close();
        db.close();
        return image;
    }
}
