package com.hackdtu.healthhistory.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hackdtu.healthhistory.R;
import com.squareup.picasso.Picasso;

public class UploadActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText title,description;
    private Button uploadButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        imageView=(ImageView)findViewById(R.id.image_to_be_uploaded);
        String path=getIntent().getStringExtra("path");
        Picasso.with(UploadActivity.this).load(path).into(imageView);

        title=(EditText)findViewById(R.id.photo_title);
        description=(EditText)findViewById(R.id.photo_description);
        uploadButton=(Button)findViewById(R.id.upload_button);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().length()>0 && description.getText().toString().length()>0)
                {
                    // Upload start
                    uploadImage();
                }
                else
                {
                    if(title.getText().toString().length()<=0)
                        title.setError("Please enter a valid title");
                    if(description.getText().toString().length()<=0)
                        description.setError("Please enter a valid description");
                }
            }
        });
    }

    private void uploadImage() {
    }
}
