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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.hackdtu.healthhistory.fragment.HomeActivityFragment;
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
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
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
    private Toolbar topToolBar;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

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

        initializeView();

        setupDrawer();

        attachFragment();

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

    private void attachFragment() {
        Fragment fragment = new HomeActivityFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_container,fragment);
        transaction.commit();
    }

    private void initializeView() {
        uploadPhoto=(FloatingActionButton)findViewById(R.id.upload_photo);
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQ_CAMERA_IMAGE);
            }
        });
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("imageUrl");


        topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        topToolBar.setTitle("User History");
        topToolBar.setTitleTextColor(Color.WHITE);
    }

    private void setupDrawer(){

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.mipmap.ic_launcher).build();

        //if you want to update the items at a later time it is recommended to keep it in a variable
        SecondaryDrawerItem item1 = new SecondaryDrawerItem().withIdentifier(1).withName(R.string.disease_history);
                //.withIcon(R.drawable.library_music);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.sugar_level_history);
                //.withIcon(R.drawable.music_circle);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.now_playing);
                //.withIcon(R.drawable.ic_play_arrow_black_36dp);

//create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(topToolBar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        item2,
                        item3
                )
                /*
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D

                        Fragment fragment = null;

                        if(position==1){
                            Log.e("onItemClick: ", " All Songs");
                            fragment = new MainFragment();
                        }
                        else if(position==2){
                            Log.e("onItemClick: ", " Favourites");
                            fragment = new FavouritesFragment();
                        }
                        else if(position==3){
                            Song song = musicSrv.getSong();
                            fragment = NowPlaying.newInstance(song.getAlbumID(),
                                    song.getAlbum(),song.getTitle(),song.getId());
                        }

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fl_container, fragment);
                        fragmentTransaction.commit();

                        return false;
                    }
                })*/
                .build();
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
