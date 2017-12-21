package com.omneagate.lip.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Service.Application;

/**
 * Created by user1 on 29/2/16.
 */
public class PreviewImageActivity extends BaseActivity {

    private ImageView imageView, back_img;
    private TextView title;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    String selectedLanguage;
    private static final String TAG = PreviewImageActivity.class.getCanonicalName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_preview);
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage=languageChcek();

        title = (TextView) findViewById(R.id.title_toolbar);
        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {


            if (selectedLanguage.equalsIgnoreCase("en")) {
                title.setText("PREVIEW");
            } else if (selectedLanguage.equalsIgnoreCase("ta")) {

                title.setText("पूर्वावलोकन");

            }


            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imageView = (ImageView)findViewById(R.id.imageView);
        //back_img = (ImageView) findViewById(R.id.back_img);
        imageView.setImageBitmap(GrievancesActivity.perview_image);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}
