package com.omneagate.lip.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.user.lip.R;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Utility.MySharedPreference;

public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2500;
    private static final String TAG = SplashActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Application.getInstance().setGoogleTracker(TAG);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {

                boolean is_Sync = MySharedPreference.readBoolean(getApplicationContext(), MySharedPreference.REGISTRATION, false);
                if (is_Sync) {
                    Intent i = new Intent(SplashActivity.this, NewsActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(SplashActivity.this, GetStartedActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}