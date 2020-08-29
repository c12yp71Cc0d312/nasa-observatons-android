package com.example.spidertask2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonApod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonApod = findViewById(R.id.button_apod);

        buttonApod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toApodActivity = new Intent(MainActivity.this, ApodActivity.class);
                startActivity(toApodActivity);
            }
        });

    }
}
