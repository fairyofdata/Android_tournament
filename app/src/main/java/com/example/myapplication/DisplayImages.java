package com.example.myapplication;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DisplayImages extends AppCompatActivity {
    private DBHelper dbHelper;
    private String tableName;
    private ArrayList<Bitmap> imageArray;
    private ArrayList<String> contentArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_images);

        dbHelper = new DBHelper(this);

        tableName = getIntent().getStringExtra("tableName");

        imageArray = new ArrayList<>();
        contentArray = new ArrayList<>();
        retrieveData();
    }

    private void retrieveData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT image, content FROM " + tableName, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
                Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));

                imageArray.add(image);
                contentArray.add(content);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }
    // to get image[0] and string[0]
    // Bitmap firstImage = imageArray.get(0);
    // String firstContent = contentArray.get(0);
}
