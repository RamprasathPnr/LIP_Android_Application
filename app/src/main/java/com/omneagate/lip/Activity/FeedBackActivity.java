package com.omneagate.lip.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.ConstantsKeys;
import com.omneagate.lip.Utility.FeedbackType;
import com.omneagate.lip.Utility.MySharedPreference;

import java.text.BreakIterator;
import java.util.HashMap;

/**
 * Created by user on 9/6/16.
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener,
        Handler.Callback {


    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private String selectedLanguage;
    private TextView title;
    CheckBox v_good, good, excellent, average, bad;

    private Button mBtSubmit;
    private ResReqController controller;
    private String userId;
    private ResponseDto responseData;
    private String feedbackType = null;
    private String description = null;
    private EditText mEtDescription;
    private String historyId;
    private String feedback_type;
    private String feedbackDescription;
    private ResponseDto category;
    Context context;
    private static final String TAG = FeedBackActivity.class.getCanonicalName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        context = this;
        Application.getInstance().setGoogleTracker(TAG);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            historyId = extras.getString(ConstantsKeys.GRIENVANCE_ID);
            feedback_type = extras.getString(ConstantsKeys.FEEDBACK_TYPE);
            feedbackDescription = extras.getString(ConstantsKeys.FEEDBACK_DESCRIPTION);
        }

        selectedLanguage = languageChcek();
        controller = new ResReqController(this);
        controller.addOutboxHandler(new Handler(this));
        setUpView();
        setupValue();
    }

    private void setupValue() {

        mEtDescription.setText(feedbackDescription);

        excellent.setChecked(false);
        v_good.setChecked(false);
        good.setChecked(false);
        average.setChecked(false);
        bad.setChecked(false);

        if (feedback_type != null) {
            switch (feedback_type) {
                case FeedbackType.excellent:
                    excellent.setChecked(true);
                    break;
                case FeedbackType.verygood:
                    v_good.setChecked(true);
                    break;
                case FeedbackType.good:
                    good.setChecked(true);
                    break;
                case FeedbackType.average:
                    average.setChecked(true);
                    break;
                case FeedbackType.bad:
                    bad.setChecked(true);
                    break;
            }
        }
    }

    private void setUpView() {


        title = (TextView) findViewById(R.id.title_toolbar);
        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {


            if (selectedLanguage.equalsIgnoreCase("en")) {
                title.setText("FeedBack");
            } else if (selectedLanguage.equalsIgnoreCase("ta")) {

                title.setText("प्रतिक्रिया");

            }
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }


        excellent = (CheckBox) findViewById(R.id.checkbox_excellent);
        v_good = (CheckBox) findViewById(R.id.checkbox_very_good);
        good = (CheckBox) findViewById(R.id.checkbox_good);
        average = (CheckBox) findViewById(R.id.checkbox_average);
        bad = (CheckBox) findViewById(R.id.checkbox_bad);

        mEtDescription = (EditText) findViewById(R.id.ed_feedback_des);
        mBtSubmit = (Button) findViewById(R.id.btn_feedback);
        mBtSubmit.setOnClickListener(this);
    }

    public void onCheckboxClicked(View view) {

        switch (view.getId()) {

            case R.id.checkbox_excellent:

                v_good.setChecked(false);
                good.setChecked(false);
                average.setChecked(false);
                bad.setChecked(false);
                feedbackType = String.valueOf(FeedbackType.excellent);

                break;

            case R.id.checkbox_very_good:

                excellent.setChecked(false);
                good.setChecked(false);
                average.setChecked(false);
                bad.setChecked(false);
                feedbackType = String.valueOf(FeedbackType.verygood);
                break;

            case R.id.checkbox_good:

                v_good.setChecked(false);
                excellent.setChecked(false);
                average.setChecked(false);
                bad.setChecked(false);
                feedbackType = String.valueOf(FeedbackType.good);
                break;


            case R.id.checkbox_average:

                v_good.setChecked(false);
                excellent.setChecked(false);
                good.setChecked(false);
                bad.setChecked(false);
                feedbackType = String.valueOf(FeedbackType.average);
                break;

            case R.id.checkbox_bad:

                v_good.setChecked(false);
                excellent.setChecked(false);
                average.setChecked(false);
                good.setChecked(false);
                feedbackType = String.valueOf(FeedbackType.bad);
                break;


        }
    }

    private void sendFeedbackStatus() {
        try {

            String userDetailsDTO = MySharedPreference.readString(FeedBackActivity.this, MySharedPreference.USER_DETAILS, "");
            responseData = new Gson().fromJson(userDetailsDTO, ResponseDto.class);
            userId = responseData.getGeneralVoterDto().getId();


            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
            inputParams.put("id", historyId);
            inputParams.put("feedbackComments", description);
            inputParams.put("feedbackType", feedbackType);
            Object v_id = userId;
            controller.handleMessage(ResReqController.COMPLAINT_FEEDBACK, inputParams, v_id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_feedback:
                description = mEtDescription.getText().toString();
                if (feedbackType == null
                        && description == null) {

                    Toast.makeText(FeedBackActivity.this, context.getString(R.string.toast_feed_description), Toast.LENGTH_SHORT).show();
                } else {
                    sendFeedbackStatus();
                }
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.COMPLAINT_FEEDBACK_SUCCESS:

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    category = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (category != null
                            && category.getStatus().equalsIgnoreCase("true")) {
                        finish();
                        Toast.makeText(FeedBackActivity.this, context.getString(R.string.toast_feedbk_submitted), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
