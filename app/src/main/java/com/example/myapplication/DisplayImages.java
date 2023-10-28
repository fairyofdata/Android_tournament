package com.example.myapplication;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DisplayImages extends AppCompatActivity {
    private DBHelper dbHelper;
    private GridView gridView;
    private ArrayList<Bitmap> images;
    private ImageAdapter adapter;
    private String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_images);

        dbHelper = new DBHelper(this);
        gridView = findViewById(R.id.gridViewImages);
        images = new ArrayList<>();
        tableName = getIntent().getStringExtra("tableName");

        // Retrieve images from the selected table
        retrieveImages();

        // ImageAdapter를 사용하여 이미지를 GridView에 연결
        adapter = new ImageAdapter(this, images);
        gridView.setAdapter(adapter);
        Toast.makeText(this, "Display Images.java open", Toast.LENGTH_SHORT).show();
    }

    private void retrieveImages() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Replace "image" and "content" with your column names in the selected table
        Cursor cursor = db.rawQuery("SELECT image FROM " + tableName, null);

        if (cursor.moveToFirst()) {
            do {
                // Retrieve the image from the cursor and convert it to a Bitmap
                @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
                Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                images.add(image);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "No images found in " + tableName, Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }
}
