package com.omneagate.lip.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Model.GrievanceHistoryDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Utility.ConstantsKeys;

import org.w3c.dom.Text;

/**
 * Created by user on 4/6/16.
 */
public class GrievancesHistoryFullViewActivity extends BaseActivity
        implements View.OnClickListener {

    private TextView title;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private String data;
    private GrievanceHistoryDto grievance_details;
    private LinearLayout mLlStatus;

    private TextView mTvStatus;
    private String status;
    TextView fedback_btn;
    private LinearLayout feed_lay;
    private String selectedLanguage;
    private static final String TAG = GrievancesHistoryFullViewActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievances_history_fullview);
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage = languageChcek();
        setUpView();
    }

    public void setUpView() {
        data = getIntent().getStringExtra("grievance_details");
        feed_lay = (LinearLayout) findViewById(R.id.feed_lay);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        grievance_details = gson.fromJson(data, GrievanceHistoryDto.class);

        title = (TextView) findViewById(R.id.title_toolbar);
        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        fedback_btn = (TextView) findViewById(R.id.feed_back);
        fedback_btn.setOnClickListener(this);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {


            if (selectedLanguage.equalsIgnoreCase("en")) {
                title.setText("Grievance Detail");
            } else if (selectedLanguage.equalsIgnoreCase("ta")) {

                title.setText("शिकायत विवरण");

            }
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        grievance_details.getId();
        ((TextView) findViewById(R.id.cat_history_tv)).setText(grievance_details.getCategoryName());
        ((TextView) findViewById(R.id.sub_cat_history_tv)).setText(grievance_details.getSubCategoryName());
        ((TextView) findViewById(R.id.des)).setText(grievance_details.getDescription());
        ((TextView) findViewById(R.id.grievance_id_tv)).setText(grievance_details.getComplaintNumber());
        ((TextView) findViewById(R.id.posted_date_tv)).setText(grievance_details.getStringPostedDate());

        mTvStatus = (TextView) findViewById(R.id.txt_grievance_status);


        status = grievance_details.getProgressStatus();

        switch (status) {

            case "NEW":
                status = "Pending";
                break;
            case "COMPLETED":
                status = "Inprogress";
                break;
            case "INPROGRESS":
                status = "Inprogress";
                break;
            case "CLOSED":
                status = "Resolved";
                feed_lay.setVisibility(View.VISIBLE);
                break;
            default:
                status = "Inprogress";
                break;

        }

        mTvStatus.setText(status);

//        processing_icon
//        ((LinearLayout)findViewById(R.id.processing_type_tv))
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.feed_back:
                Intent i = new Intent(this, FeedBackActivity.class);
                i.putExtra(ConstantsKeys.GRIENVANCE_ID, grievance_details.getId());
                i.putExtra(ConstantsKeys.FEEDBACK_TYPE, grievance_details.getFeedbackType());
                i.putExtra(ConstantsKeys.FEEDBACK_DESCRIPTION, grievance_details.getFeedbackComments());
                startActivity(i);
                break;
        }
    }
}
