package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toDBButton = findViewById(R.id.toDB);
        Button toDataShowTempButton = findViewById(R.id.toDataShowTemp);
        toDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DataInput 액티비티로 이동
                Intent intent = new Intent(MainActivity.this, InputData.class);
                startActivity(intent);
            }
        });
        toDataShowTempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // DataShowTemp 액티비티로 이동
                Intent intent = new Intent(MainActivity.this, ShowMenu.class);
                startActivity(intent);
            }
        });
    }

}
