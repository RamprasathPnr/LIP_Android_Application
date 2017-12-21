package com.omneagate.lip.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.lip.R;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Utility.NetworkConnection;

/**
 * Created by user1 on 6/6/16.
 */
public class NoInternetActivity extends Activity {

    NetworkConnection networkConnection;
    Context mContext;
    private static final String TAG = NoInternetActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.no_internet);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            mContext = this;
            Application.getInstance().setGoogleTracker(TAG);
            ((Button) findViewById(R.id.check_connectivity_btn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    networkConnection = new NetworkConnection(mContext);

                    if (networkConnection.isNetworkAvailable()) {

                        Intent i = new Intent(NoInternetActivity.this, SplashActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.toast_no_newtwork_connectivity), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
