package com.example.spidertask2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonApod;
    private Button buttonIVLib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonApod = findViewById(R.id.button_apod);
        buttonIVLib = findViewById(R.id.button_ivlib);

        buttonApod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toApodActivity = new Intent(MainActivity.this, ApodActivity.class);
                startActivity(toApodActivity);
            }
        });

        buttonIVLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toIVLibActivity = new Intent(MainActivity.this, NasaIVLibActivity.class);
                startActivity(toIVLibActivity);
            }
        });

    }
}
