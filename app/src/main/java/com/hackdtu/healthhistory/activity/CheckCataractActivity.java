package com.hackdtu.healthhistory.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckCataractActivity extends AppCompatActivity {

    String path, name;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_cataract);
        progressStart();
        path = getIntent().getStringExtra("path");
        name = getIntent().getStringExtra("name");

        try {
            Uri uri = Uri.parse(path);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            String imageBase64 = getEncoded64ImageStringFromBitmap(bitmap);

            OkHttpClient client = new OkHttpClient();

            /*
            Request request = new Request.Builder()
                    .url("http://7a52c212.ngrok.io/history/checkstring/" + imageBase64)
                    .build();*/
            Request request = new Request.Builder()
                    .url(Constants.CATARACT_DETECT_URL + imageBase64)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("Check cataract", "onFailure: " + e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                  //  Log.d("Check cataract", "onResponse: " + response.body().string()) ;
                    String jsonResult = response.body().string();
                    try {
                        JSONObject js = new JSONObject(jsonResult);
                        Intent intent = new Intent(CheckCataractActivity.this, ScanResultActivtiy.class);
                        intent.putExtra("disease",0);
                        if(js.getInt("points")>100){
                            intent.putExtra("result", 2);
                        }
                        else if(js.getInt("points")<75) {
                            intent.putExtra("result", 0);
                        }
                        else {
                            intent.putExtra("result", 1);
                        }
                        progressStop();
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

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
    void progressStart() {
        //pb = new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
        pd = new ProgressDialog(this);
        pd.setMessage("Logging In Please Wait...");
        pd.setCancelable(false);
        pd.show();
    }

    void progressStop() {
        pd.dismiss();
    }
}
