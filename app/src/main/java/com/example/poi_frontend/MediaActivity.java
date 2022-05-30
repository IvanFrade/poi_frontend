package com.example.poi_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MediaActivity extends AppCompatActivity implements View.OnClickListener {

    private String poiText;
    private POI poi;
    private Media[] poiMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        poiText = getIntent().getStringExtra("poi");

        try {
            poi = new POI(new JSONObject(poiText));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((TextView)findViewById(R.id.txt_poi_name)).setText(poi.getName());

        poiMedia = poi.getMedia();

        ViewGroup layout = (ViewGroup)findViewById(R.id.button_layout);
        for(int i = 0; i < poiMedia.length; i++) {
            Button button = new Button(this);
            button.setText(poiMedia[i].getLabel());
            button.setTag(poiMedia[i]);
            button.setOnClickListener(this);
            layout.addView(button);
        }

        Button exitButton = new Button(this);
        exitButton.setText("ESCI");
        exitButton.setTag(false);
        exitButton.setOnClickListener(this);
        layout.addView(exitButton);
    }

    @Override
    public void onClick(View view) {
        if (view.getTag().equals(false)) finish();
        else {
            Media clickedMedia = (Media)view.getTag();

            Intent intent = new Intent(this, ShowMedia.class);
            intent.putExtra("url", clickedMedia.getUrl());
            startActivity(intent);
        }
    }
}