package com.hackdtu.healthhistory.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.network.NetworkCall;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    public class ShowList extends AsyncTask<Object, Object, String>{

        @Override
        protected String doInBackground(Object... objects) {
            NetworkCall networkCall=new NetworkCall();
            try {
                String response=networkCall.run();
                return response;
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
}
