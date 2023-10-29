package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory

object DataUtils {
    @SuppressLint("Range")
    fun retrieveData(dbHelper: DBHelper, tableName: String): Pair<ArrayList<Bitmap>, ArrayList<String>> {
        val imageArray = ArrayList<Bitmap>()
        val contentArray = ArrayList<String>()

        val db = dbHelper.getReadableDatabase()
        val cursor = db.rawQuery("SELECT image, content FROM $tableName", null)
        if (cursor.moveToFirst()) {
            do {
                val imageBytes = cursor.getBlob(cursor.getColumnIndex("image"))
                val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                val content = cursor.getString(cursor.getColumnIndex("content"))

                imageArray.add(image)
                contentArray.add(content)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return Pair(imageArray, contentArray)
    }
}
