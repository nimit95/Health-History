package com.hackdtu.healthhistory.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.adapter.CustomList;
import com.hackdtu.healthhistory.model.Diseases;
import com.hackdtu.healthhistory.model.DiseasesHistory;
import com.hackdtu.healthhistory.model.User;
import com.hackdtu.healthhistory.model.UserHistoryList;
import com.hackdtu.healthhistory.network.NetworkCall;
import com.hackdtu.healthhistory.utils.Constants;
import com.hackdtu.healthhistory.utils.SuperPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DiseaseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_list);
        new ShowList().execute();
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
                Log.e("onPostExecute: ", "No response");
            }
            else
            {
                Gson gson=new GsonBuilder().create();
                DiseasesHistory diseasesHistory=gson.fromJson(jsonResponse,DiseasesHistory.class);
                ArrayList<Diseases> diseasesArrayList=new ArrayList<>(diseasesHistory.getDiseasesList().size());
                CustomList adapter=new CustomList(DiseaseListActivity.this,diseasesHistory.getDiseasesList());
                ListView listView=(ListView)findViewById(R.id.listView);
                listView.setAdapter(adapter);
            }
        }
    }
}
