package com.hackdtu.healthhistory.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.model.DiseasesHistory;
import com.hackdtu.healthhistory.model.User;
import com.hackdtu.healthhistory.model.UserHistoryList;
import com.hackdtu.healthhistory.network.NetworkCall;
import com.hackdtu.healthhistory.utils.Constants;
import com.hackdtu.healthhistory.utils.SuperPrefs;

import org.json.JSONException;
import org.json.JSONObject;

public class DiseaseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_list);
    }

    public class ShowList extends AsyncTask<Object, Object, String> {

        @Override
        protected String doInBackground(Object... objects) {
            SuperPrefs superPrefs=new SuperPrefs(DiseaseListActivity.this);

            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("adhaar_card",superPrefs.getString(""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            NetworkCall networkCall=new NetworkCall();
            try {
                String response=networkCall.post(Constants.DISEASE_LIST_URL,jsonObject.toString());
                return response;
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            super.onPostExecute(jsonResponse);
            if(jsonResponse==null)
            {
                // Net not present
            }
            else
            {
                Gson gson=new GsonBuilder().create();
                DiseasesHistory diseasesHistory=gson.fromJson(jsonResponse,DiseasesHistory.class);
            }
        }
    }
}
