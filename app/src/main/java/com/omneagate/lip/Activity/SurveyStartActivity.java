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
public class SurveyStartActivity extends BaseActivity {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private Button mBnStart;
    private TextView toolbarTitle;
    String selectedLanguage;
    private static final String TAG = SurveyStartActivity.class.getCanonicalName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_start);
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage = languageChcek();

        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        customizeActionBar(mActionBar, mToolbar);

        mBnStart = (Button) findViewById(R.id.btn_survey_start);
        mBnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent i = new Intent(SurveyStartActivity.this, SurveyActivity.class);
                startActivity(i);
                finish();*/

                Intent i = new Intent(SurveyStartActivity.this, GroupSurveyQuestionActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void customizeActionBar(ActionBar actionBar, Toolbar toolbar) {

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarTitle = (TextView) findViewById(R.id.title_toolbar);

        if (selectedLanguage.equalsIgnoreCase("en")) {
            toolbarTitle.setText("Survey");
        } else if (selectedLanguage.equalsIgnoreCase("ta")) {

            toolbarTitle.setText("सर्वेक्षण");

        }


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
