package com.omneagate.lip.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.user.lip.R;
import com.omneagate.lip.Service.Application;

/**
 * Created by user on 21/5/16.
 */
public class GetStartedActivity extends Activity implements View.OnClickListener {

    Button btn_get_started;
    private static final String TAG = GetStartedActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstarted);
        Application.getInstance().setGoogleTracker(TAG);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        btn_get_started=(Button)findViewById(R.id.btn_getstart);
        btn_get_started.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_getstart :
                Intent i=new Intent(GetStartedActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
