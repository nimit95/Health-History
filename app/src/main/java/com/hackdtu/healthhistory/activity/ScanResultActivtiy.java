package com.hackdtu.healthhistory.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackdtu.healthhistory.R;

import info.hoang8f.widget.FButton;

public class ScanResultActivtiy extends AppCompatActivity {


    TextView tvResult;
    ImageView imageView;
    FButton btnResultAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result_activtiy);

        tvResult = (TextView) findViewById(R.id.tvResult);
        imageView = findViewById(R.id.imageView);
        btnResultAction = (FButton) findViewById(R.id.btnResultAction);

        btnResultAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.google.com/search?q=Nearby%20cataract%20doctors");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        int result = getIntent().getIntExtra("result", 1);

        if(result==0) {
            tvResult.setText("No Cataract Detected");
            imageView.setImageResource(R.drawable.fine);
        }

        else if(result==1) {
            tvResult.setText("Mild Cataract Detected");
            imageView.setImageResource(R.drawable.seedoctor);
            btnResultAction.setVisibility(View.VISIBLE);
        }
        else {
            tvResult.setText("Cataract Detected");
            imageView.setImageResource(R.drawable.seedoctor);
            btnResultAction.setVisibility(View.VISIBLE);
        }


    }
}
