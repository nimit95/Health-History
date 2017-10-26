package com.hackdtu.healthhistory.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hackdtu.healthhistory.FirebaseReference;
import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.model.BloodPressure;
import com.hackdtu.healthhistory.model.Disease;
import com.hackdtu.healthhistory.model.ImagePojo;
import com.hackdtu.healthhistory.model.SugarLevel;
import com.hackdtu.healthhistory.network.Jsonparsor;
import com.hackdtu.healthhistory.utils.Constants;
import com.hackdtu.healthhistory.utils.SuperPrefs;
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
import java.util.ArrayList;
import java.util.HashMap;

import static com.hackdtu.healthhistory.utils.Constants.UPLOAD_URL;


public class UploadActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private ProgressBar pb;
    private StorageReference mStorageRef;
    private static String TAG ="" ;
    private ImageView imageView;
    private EditText title,description;
    private Button uploadButton;
    private String path,name,ba1="";
    private String titleValue,descriptionValue;
    private SuperPrefs superPrefs;
    private DatabaseReference mDatabase;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        path=getIntent().getStringExtra("path");
        name=getIntent().getStringExtra("name");

        initializeViews();

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),Uri.parse(path));
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Picasso.with(UploadActivity.this).load(path).into(imageView);
    }

    private void initializeViews() {
        superPrefs = new SuperPrefs(this);
        mStorageRef = FirebaseStorage.getInstance().getReference(
                superPrefs.getString(Constants.USER_ID));
        mDatabase = FirebaseDatabase.getInstance().getReference(
                superPrefs.getString(Constants.USER_ID));


        TAG = this.getClass().getSimpleName();
        imageView = (ImageView)findViewById(R.id.image_to_be_uploaded);
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

        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("X Ray");
        stringList.add("MRI Report");
        stringList.add("Doctor Prescription");
        stringList.add("Ultrasound");
        stringList.add("Test Report");
        stringList.add("Others");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stringList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    void upload()
    {
        Uri uri=Uri.parse(path);

        progressStart();

        StorageReference riversRef=mStorageRef.child("images/"+System.currentTimeMillis());
        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        ArrayList<SugarLevel> temp1 = new ArrayList<SugarLevel>();
                        ArrayList<BloodPressure> temp2 = new ArrayList<BloodPressure>();

                        String type = getTypeOfImage();
                        Uri url=taskSnapshot.getMetadata().getDownloadUrl();
                        ImagePojo image = new ImagePojo(titleValue,String.valueOf(System.currentTimeMillis()),
                                url.toString(),descriptionValue,type,temp1,temp1,temp2,
                                new ArrayList<Disease>()
                                );

                        //DatabaseReference databaseReference = mDatabase.child(Constants.USER_IMG_FB).push();

                        FirebaseReference.userReference.child(
                                superPrefs.getString(Constants.USER_ID)
                        ).child(Constants.USER_IMG_FB).child(image.getTimeStamp()).setValue(image);
                        pd.dismiss();
                        //and displaying a success toast
                        Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        finish();
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
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / (double) taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        pd.setMessage("Uploaded " + ( progress) + "%...");
                    }
                });
    }
        void progressStart() {
            //pb = new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
            pd = new ProgressDialog(UploadActivity.this);
            pd.setMessage("Upload Image Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

    public String getTypeOfImage() {
        String typeOfImage="";
        int pos= spinner.getSelectedItemPosition();

        switch (pos){
            case 0:
                typeOfImage = Constants.XRAY_TYPE;
                break;
            case 1:
                typeOfImage = Constants.MRI_TYPE;
                break;
            case 2:
                typeOfImage = Constants.DOCTOR_PRESCRIPTION_TYPE;
                break;
            case 3:
                typeOfImage = Constants.ULTRASOUND_TYPE;
                break;
            case 4:
                typeOfImage = Constants.TEST_REPORT_TYPE;
                break;
        }
        return typeOfImage;
    }
}

