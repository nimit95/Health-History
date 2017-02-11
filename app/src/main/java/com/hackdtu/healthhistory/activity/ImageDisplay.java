package com.hackdtu.healthhistory.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.hackdtu.healthhistory.R;
import com.squareup.picasso.Picasso;

public class ImageDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        ImageView imageView = (ImageView) findViewById(R.id.image);
        Picasso.with(getApplicationContext()).load(getIntent().getStringExtra("path")).into(imageView);
    }
}
