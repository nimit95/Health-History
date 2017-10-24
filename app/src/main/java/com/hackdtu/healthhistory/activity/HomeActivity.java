package com.hackdtu.healthhistory.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.model.Diseases;
import com.hackdtu.healthhistory.model.DrawerHeader;
import com.hackdtu.healthhistory.model.DrawerMenuItem;
import com.hackdtu.healthhistory.model.HeadingView;
import com.hackdtu.healthhistory.model.InfoView;
import com.hackdtu.healthhistory.model.UserHistory;
import com.hackdtu.healthhistory.model.UserHistoryList;
import com.hackdtu.healthhistory.network.NetworkCall;
import com.hackdtu.healthhistory.network.NetworkCall2;
import com.hackdtu.healthhistory.utils.Constants;
import com.hackdtu.healthhistory.utils.SuperPrefs;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mindorks.placeholderview.ExpandablePlaceHolderView;
import com.mindorks.placeholderview.PlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private int REQ_CAMERA_IMAGE=10;
    private FloatingActionButton uploadPhoto;
    private ExpandablePlaceHolderView mExpandableView;
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Dexter.withActivity(this)

                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Toast.makeText(getApplicationContext(), "Permission Denied\n" + permissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).check();
        uploadPhoto=(FloatingActionButton)findViewById(R.id.upload_photo);
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQ_CAMERA_IMAGE);
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("imageUrl");

        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setupDrawer();
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("User History");
        mToolbar.setTitleTextColor(Color.WHITE);
        //new ShowList().execute();
        mExpandableView = (ExpandablePlaceHolderView)findViewById(R.id.expandableView);
        /*for(int i=0;i<10;i++) {
            mExpandableView.addView(new HeadingView(getApplicationContext(), "Heading" + i));
            for(int j=0;j<4;j++){
                UserHistory userHistory = new UserHistory();
                userHistory.setTitle("title"+j);
                userHistory.setDescription("description"+j);
                mExpandableView.addView(new InfoView(getApplicationContext(), userHistory));
            }
        }*/
        for(int i=0;i<4;i++) {
            if (i == 0)
                mExpandableView.addView(new HeadingView(getApplicationContext(), "June, 2017"));
            if (i == 1)
                mExpandableView.addView(new HeadingView(getApplicationContext(), "May, 2017"));
            if (i == 2)
                mExpandableView.addView(new HeadingView(getApplicationContext(), "March, 2017"));
            if (i == 3)
                mExpandableView.addView(new HeadingView(getApplicationContext(), "January, 2017"));
        }
        ArrayList<UserHistory> userHistoryList = new ArrayList<>();

        for(int j=0;j<4;j++){
            userHistoryList.add(new UserHistory("https://firebasestorage.googleapis.com/v0/b/healthhistory-459fe.appspot.com/o/WZvBhbeWmuMB6gTEt4149PUbN4t1images%2Ftesting?alt=media&token=52f4c92d-f14a-4cca-a201-ba85f489fbd5"
            , "lalu", "21 june 2017", "Blood test Report", "Detailed report awaited","102"));
            UserHistory userHistory = userHistoryList.get(j);
           // mExpandableView.addView();
        }
        userHistoryList.add(1,new UserHistory("https://firebasestorage.googleapis.com/v0/b/healthhistory-459fe.appspot.com/o/WZvBhbeWmuMB6gTEt4149PUbN4t1images%2Ftesting?alt=media&token=52f4c92d-f14a-4cca-a201-ba85f489fbd5"
                , "Piyush", "20 june 2017", "Chest X Ray", "Little Congestion in chest","102"));
        mExpandableView.addChildView(0,new InfoView(getApplicationContext(), userHistoryList.get(0)));
        mExpandableView.addChildView(0, new InfoView(getApplicationContext(), userHistoryList.get(1)));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);

                Log.d("app", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("app", "Failed to read value.", error.toException());
            }
        });
    }
    private void setupDrawer(){
        mDrawerView
                .addView(new DrawerHeader())
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_PROFILE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_REQUESTS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_MESSAGE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_GROUPS));


        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }

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
            intent.putExtra("name",finalFile.getName());
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
    public class ShowList extends AsyncTask<Object, Object, String>{

        @Override
        protected String doInBackground(Object... objects) {
            NetworkCall2 networkCall=new NetworkCall2();
            try {
                String response=networkCall.run(Constants.DATA_URL);
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
                Log.e("onPostExecute: ","no response" );
            }
            else
            {
                List<UserHistory> userHistoryLists=new ArrayList<>();
                Gson gson=new GsonBuilder().create();
                try {
                    JSONArray jsonArray=new JSONArray(s);


                    for(int i=0;i<jsonArray.length();i++)
                    {
                        UserHistory object=gson.fromJson(jsonArray.getString(i),UserHistory.class);
                        userHistoryLists.add(object);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.e("onPostExecute: ",s );
            }

    }}
}
