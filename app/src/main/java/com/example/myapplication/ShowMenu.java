package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ShowMenu extends AppCompatActivity {
    private DBHelper dbHelper;
    private GridView gridView;
    private ArrayList<String> tableNames;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_menu);

        dbHelper = new DBHelper(this);
        gridView = findViewById(R.id.gridView);
        tableNames = new ArrayList<>();

        // Retrieve table names from mainTable
        retrieveTableNames();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tableNames);
        gridView.setAdapter(adapter);

        // Handle item click on GridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedTableName = tableNames.get(position);

                Intent intent = new Intent(ShowMenu.this, StartTournamentActivity.class);

                intent.putExtra("tableName", selectedTableName);
                startActivity(intent);
            }
        });
    }

    private void retrieveTableNames() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT tableName FROM mainTable", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String tableName = cursor.getString(cursor.getColumnIndex("tableName"));
                tableNames.add(tableName);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "No tables found in mainTable", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }
}
