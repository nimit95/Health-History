package com.hackdtu.healthhistory.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.network.NetworkCall;
import com.hackdtu.healthhistory.utils.Constants;
import com.hackdtu.healthhistory.utils.SuperPrefs;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private EditText adhaarNo, password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adhaarNo = (EditText) findViewById(R.id.aadhar);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetResponse().execute();
            }
        });
    }

    public class GetResponse extends AsyncTask<Object, Object, String> {

        @Override
        protected String doInBackground(Object... Void) {
            SuperPrefs superPrefs=new SuperPrefs(MainActivity.this);

            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("adhaar_card",superPrefs.getString(""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            NetworkCall networkCall=new NetworkCall();
            try {
                String response=networkCall.post(Constants.AUTH_URL,jsonObject.toString());
                return response;
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
