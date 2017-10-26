package com.hackdtu.healthhistory.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.utils.SuperPrefs;

public class ShareUserIdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_user_id);

        SuperPrefs superPrefs = new SuperPrefs(this);
        ((TextView)findViewById(R.id.tvUserId)).setText(superPrefs.getString("user-id"));
    }
}
