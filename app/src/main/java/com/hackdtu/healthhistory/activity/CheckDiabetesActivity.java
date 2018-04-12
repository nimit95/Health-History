package com.hackdtu.healthhistory.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckDiabetesActivity extends AppCompatActivity {

    private EditText etBp,etInsulinLvl,etGlucoseLvl,etAge,etWeight,etHeight;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_diabetes);

        initializeViews();
        (findViewById(R.id.btnSubmit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressStart();
                checkDiabetes();
            }
        });
    }

    private void checkDiabetes() {
        String appendUrl = getParamsValues();
        Log.d("checkDiabetes: ",Constants.DIABETES_DETECT_URL+appendUrl);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constants.DIABETES_DETECT_URL + appendUrl)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Check diabetes", "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonResult = response.body().string();
                Log.d("Check Diabetes", "onResponse: "+jsonResult);
                progressStop();
                try {
                    JSONObject jsonObject = new JSONObject(jsonResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("points");
                    double res = (double) jsonArray.get(0);
                    Intent intent = new Intent(CheckDiabetesActivity.this, ScanResultActivtiy.class);
                    intent.putExtra("disease",2);
                    if(res > 0.99){
                        // yes to diabetes
                        intent.putExtra("result", 1);
                    }else {
                        // no to diabetes
                        intent.putExtra("result", 0);
                    }
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initializeViews() {
        etBp = (EditText)findViewById(R.id.et_Bp);
        etInsulinLvl = (EditText)findViewById(R.id.et_insulinLevel);
        etGlucoseLvl = (EditText)findViewById(R.id.et_glucoseLevel);
        etAge = (EditText)findViewById(R.id.et_age);
        etWeight = (EditText)findViewById(R.id.et_weight);
        etHeight = (EditText)findViewById(R.id.et_height);
    }

    public String getParamsValues() {
        double weight = Double.parseDouble(etWeight.getText().toString());
        double mass = weight/9.8;
        double bmi = mass/(Double.parseDouble(etHeight.getText().toString())
        * Double.parseDouble(etHeight.getText().toString()));

        String paramsValues =
                //pregnaneCount
                        "4/"+
                        etGlucoseLvl.getText().toString()+"/"+
                        etBp.getText().toString()+"/"+
                        // skin thickness avg from dataset
                        "20.623778501628664"+"/"+
                        etInsulinLvl.getText().toString()+"/"+
                        String.valueOf(bmi)+"/"+
                        //diabetes pedigree
                        "0.466470684039088"+"/"+
                        etAge.getText().toString();
        return paramsValues;
    }

    void progressStart() {
        //pb = new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
        pd = new ProgressDialog(this);
        pd.setMessage("Logging In Please Wait...");
        pd.setCancelable(false);
        pd.show();
    }

    void progressStop() {
        pd.dismiss();
    }
}
