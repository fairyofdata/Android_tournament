package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

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
}
