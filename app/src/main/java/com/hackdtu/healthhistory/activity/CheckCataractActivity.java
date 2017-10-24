package com.hackdtu.healthhistory.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;

import com.hackdtu.healthhistory.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class CheckCataractActivity extends AppCompatActivity {

    String path, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_cataract);

        path = getIntent().getStringExtra("path");
        name = getIntent().getStringExtra("name");

        try {
            Uri uri = Uri.parse(path);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            getEncoded64ImageStringFromBitmap(bitmap);

        } catch(Exception e) {
            System.out.println(e);
        }

    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }
}
