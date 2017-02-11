package com.hackdtu.healthhistory.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.model.User;
import com.hackdtu.healthhistory.network.NetworkCall;
import com.hackdtu.healthhistory.utils.Constants;
import com.hackdtu.healthhistory.utils.SuperPrefs;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private EditText adhaarNo, password;
    private Button login;
    SuperPrefs superPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        superPrefs=new SuperPrefs(MainActivity.this);
        if(superPrefs.stringExists("adhaar_card")){
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }
        adhaarNo = (EditText) findViewById(R.id.aadhar);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adhaarNo.getText().toString().length()==12 && password.getText().toString().length()!=0) {
                    superPrefs.setString("adhaar_card", adhaarNo.getText().toString());
                    String[] s={password.getText().toString(), adhaarNo.getText().toString()};
                    new GetResponse().execute(s);
                }
                else
                    Toast.makeText(getApplicationContext(), "Enter Valid Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class GetResponse extends AsyncTask<String, Object, String> {

        @Override
        protected String doInBackground(String[] password) {


            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("adhaar_card",superPrefs.getString("adhaar_card"));
                jsonObject.put("password", password[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            NetworkCall networkCall=new NetworkCall();
            try {
                String response=networkCall.post(Constants.AUTH_URL,jsonObject.toString());
                Log.e("yo", response);
                return response;
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            //super.onPostExecute(jsonResponse);
            Gson gson=new GsonBuilder().create();
            User user=gson.fromJson(jsonResponse,User.class);
            if(user.getStatus()!=null) {
                if (user.getStatus().equalsIgnoreCase("Login Successfully")) {
                    superPrefs.setString("first_name", user.getFirst_name());
                    superPrefs.setString("last_name", user.getLast_name());
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(), "Adhaar/Password Wrong", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getApplicationContext(), "Adhaar/Password Wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
