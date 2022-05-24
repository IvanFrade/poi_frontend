package com.example.poi_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MediaActivity extends AppCompatActivity {

    private String codicePOI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        codicePOI = getIntent().getStringExtra("codicePOI");
    }
}