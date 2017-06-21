package com.hackdtu.healthhistory.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.network.Jsonparsor;
import com.hackdtu.healthhistory.utils.Constants;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.okhttp.OkHttpStack;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import static com.hackdtu.healthhistory.utils.Constants.UPLOAD_URL;


public class UploadActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private StorageReference mStorageRef;
    private static String TAG ="" ;
    private ImageView imageView;
    private EditText title,description;
    private Button uploadButton;
    String path,name,ba1="";
    int serverResponseCode = 0;
    Jsonparsor jsonparsor;
    JSONObject json;
    private String titleValue,descriptionValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mStorageRef= FirebaseStorage.getInstance().getReference();

        UploadService.HTTP_STACK = new OkHttpStack();
        TAG=this.getClass().getSimpleName();
        imageView=(ImageView)findViewById(R.id.image_to_be_uploaded);

        jsonparsor=new Jsonparsor();
         path=getIntent().getStringExtra("path");
         name=getIntent().getStringExtra("name");

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
                    titleValue=title.getText().toString();
                    descriptionValue=description.getText().toString();
                    //new demo().execute();
                    upload();
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
    void upload()
    {
        Uri uri=Uri.parse(path);

        progressStart();
        StorageReference riversRef=mStorageRef.child("images/"+name);
        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        //and displaying a success toast
                        Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        pd.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });
    }
        void progressStart() {
            pd = new ProgressDialog(UploadActivity.this);
            pd.setMessage("Upload Image Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }
    }

