package com.hackdtu.healthhistory.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.utils.Constants;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckDiabetesActivity extends AppCompatActivity {

    private EditText etBp,etInsulinLvl,etGlucoseLvl,etAge,etWeight,etHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_diabetes);

        initializeViews();
        (findViewById(R.id.btnSubmit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDiabetes();
            }
        });
    }

    private void checkDiabetes() {
        String appendUrl = getParamsValues();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constants.DIABETES_DETECT_URL + appendUrl)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

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
                        "4"+
                        etGlucoseLvl.getText().toString()+
                        etBp.getText().toString()+
                        // skin thickness avg from dataset
                        "20.623778501628664"+
                        etInsulinLvl.getText().toString()+
                        String.valueOf(bmi)+
                        //diabetes pedigree
                        "0.466470684039088"+
                        etAge.getText().toString();
        return paramsValues;
    }
}
