package com.omneagate.lip.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Service.Application;

/**
 * Created by USER1 on 10-06-2016.
 */
public class SurveyEndActivity extends BaseActivity {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private TextView toolbarTitle;
    Button finish;
    String selectedLanguage;
    private static final String TAG = SurveyEndActivity.class.getCanonicalName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_end);
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage=languageChcek();
        finish = (Button) findViewById(R.id.survey_finish_btn);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SurveyEndActivity.this, NewsActivity.class);
                startActivity(i);
                finish();

            }
        });

        setupToolbar();
    }


    private void setupToolbar() {

        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = (TextView) findViewById(R.id.title_toolbar);

        if (selectedLanguage.equalsIgnoreCase("en")) {
            toolbarTitle.setText("Survey");
        } else if (selectedLanguage.equalsIgnoreCase("ta")) {

            toolbarTitle.setText("सर्वेक्षण");

        }


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
