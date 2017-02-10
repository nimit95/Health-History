package com.hackdtu.healthhistory.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.model.UserHistoryList;
import com.hackdtu.healthhistory.network.NetworkCall;
import com.hackdtu.healthhistory.utils.Constants;
import com.hackdtu.healthhistory.utils.SuperPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class HomeActivity extends AppCompatActivity {
    private int REQ_CAMERA_IMAGE=10;
    private FloatingActionButton uploadPhoto;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==REQ_CAMERA_IMAGE)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            /*
            imageView.setImageBitmap(photo);
            knop.setVisibility(Button.VISIBLE);*/


            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));

            //System.out.println(mImageCaptureUri);
            Intent intent=new Intent(HomeActivity.this,UploadActivity.class);
            intent.putExtra("path",tempUri.toString());
            startActivity(intent);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        uploadPhoto=(FloatingActionButton)findViewById(R.id.upload_photo);
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQ_CAMERA_IMAGE);
            }
        });
    }
    public class ShowList extends AsyncTask<Object, Object, String>{

        @Override
        protected String doInBackground(Object... objects) {
            SuperPrefs superPrefs=new SuperPrefs(HomeActivity.this);

            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("adhaar_card",superPrefs.getString(""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            NetworkCall networkCall=new NetworkCall();
            try {
                String response=networkCall.post(Constants.DATA_URL,jsonObject.toString());
                return response;
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s==null)
            {
                // Net not present
            }
            else
            {
                Gson gson=new GsonBuilder().create();
                UserHistoryList userHistoryList=gson.fromJson(s,UserHistoryList.class);
            }
        }
    }
}
