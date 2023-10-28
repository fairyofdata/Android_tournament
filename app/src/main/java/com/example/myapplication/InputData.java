package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InputData extends AppCompatActivity {
    DataHandler dataHandler;
    DBHelper dbHelper;
    ImageView[] imageViews = new ImageView[8];
    EditText[] editTexts = new EditText[8];

    private static final int PICK_IMAGE_REQUEST = 1;
    private int currentSelectedImageViewIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_insert);

        dbHelper = new DBHelper(this);

        int[] imageIds = {
                R.id.image1, R.id.image2, R.id.image3, R.id.image4,
                R.id.image5, R.id.image6, R.id.image7, R.id.image8
        };

        int[] editTextIds = {
                R.id.editText1, R.id.editText2, R.id.editText3, R.id.editText4,
                R.id.editText5, R.id.editText6, R.id.editText7, R.id.editText8
        };

        for (int i = 0; i < 8; i++) {
            imageViews[i] = findViewById(imageIds[i]);
            editTexts[i] = findViewById(editTextIds[i]);
            final int index = i;
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentSelectedImageViewIndex = index;
                    openGallery();
                }
            });
        }

        Button saveButton = findViewById(R.id.saveIntoDB);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tableNameEditText = findViewById(R.id.tableNameEditText);
                String tableName = tableNameEditText.getText().toString().trim();
                dataHandler = new DataHandler(InputData.this, tableName);
                dataHandler.createTable();
                boolean isInsertSuccess = true;

                for (int i = 0; i < 8; i++) {
                    try {
                        Bitmap image = ((BitmapDrawable) imageViews[i].getDrawable()).getBitmap();
                        String content = editTexts[i].getText().toString();
                        long result = dataHandler.insertData(image, content);
                        if (result == -1) {
                            Toast.makeText(InputData.this, "Error inserting data", Toast.LENGTH_LONG).show();
                            isInsertSuccess = false;
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        isInsertSuccess = false;
                    }
                }

                long mainTableResult = dbHelper.insertIntoMainTable(tableName);
                if (mainTableResult == -1) {
                    Toast.makeText(InputData.this, "Error inserting to MainTable", Toast.LENGTH_LONG).show();
                    isInsertSuccess = false;
                }

                if (isInsertSuccess) {
                    Toast.makeText(InputData.this, "Data inserted successfully", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(InputData.this, "Data insertion failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            if (currentSelectedImageViewIndex != -1 && currentSelectedImageViewIndex < imageViews.length) {
                imageViews[currentSelectedImageViewIndex].setImageURI(selectedImage);
            }
        }
    }
}
