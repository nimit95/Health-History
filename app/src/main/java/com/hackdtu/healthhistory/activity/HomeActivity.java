package com.hackdtu.healthhistory.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.model.UserHistoryList;
import com.hackdtu.healthhistory.network.NetworkCall;
import com.hackdtu.healthhistory.utils.Constants;
import com.hackdtu.healthhistory.utils.SuperPrefs;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
