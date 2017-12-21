package com.omneagate.lip.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Adaptor.SurveyAnswerAdapter;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Model.SurveyOptionDto;
import com.omneagate.lip.Model.SurveyQuestionDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Service.QuestionChangeListener;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.SurveyAnswerListener;
import com.omneagate.lip.Service.SurveyServices;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.MySharedPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class using to attend survey
 * Created by Shnathakumar on 10-06-2016.
 */
public class SurveyActivity extends BaseActivity implements QuestionChangeListener,
        SurveyAnswerListener, Handler.Callback, View.OnClickListener {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private TextView toolbarTitle;
    private ResReqController controller;

    private TextView mTvQuestion;
    private TextView mTvQuestionNo;
    private TextView mTvNoQuestion;
    private Button mBtNext;
    private LinearLayout mLlSurvey;
    private LinearLayout mSurveySlider;
    private RelativeLayout mRlSurveyRating;
    private ProgressBar progressBar;
    private RatingBar mRbSurveyBar;
    private RecyclerView mRecyclerView;
    private SurveyAnswerAdapter adapter;
    private boolean optionType;
    private ArrayList<String> selectAnswer;
    private String userId;
    private String selectedLanguage;
    private TextView mTvRatingDisplay;
    private TextView mTvSeekbarValue;
    private SeekBar surveySeekBar;
    private boolean hideLayouts = false;
    private String postAnswer = "";
    Context context;
    private static final String TAG = SurveyActivity.class.getCanonicalName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        context=this;
        Application.getInstance().setGoogleTracker(TAG);
        controller = new ResReqController(SurveyActivity.this);
        controller.addOutboxHandler(new Handler(this));
        selectAnswer = new ArrayList<>();

        String userDetailsDTO = MySharedPreference.readString(SurveyActivity.this,
                MySharedPreference.USER_DETAILS, "");
        ResponseDto responseData = new Gson().fromJson(userDetailsDTO, ResponseDto.class);
        userId = responseData.getGeneralVoterDto().getId();

        selectedLanguage = languageChcek();

        setupToolbar();
        setupView();

        loadSurveyQuestion();
    }


    private void setupView() {

        mTvQuestionNo = (TextView) findViewById(R.id.tv_survey_ques_no);
        mTvQuestion = (TextView) findViewById(R.id.tv_survey_ques);
        mTvNoQuestion = (TextView) findViewById(R.id.txt_no_question);
        mTvRatingDisplay = (TextView) findViewById(R.id.ratingDisplay);
        mBtNext = (Button) findViewById(R.id.btn_survey_next);
        mLlSurvey = (LinearLayout) findViewById(R.id.ll_survey_layout);
        mRlSurveyRating = (RelativeLayout) findViewById(R.id.survey_rating_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.survey_check_answer);
        surveySeekBar = (SeekBar) findViewById(R.id.survey_seekBar);
        mRbSurveyBar = (RatingBar) findViewById(R.id.survey_rating);
        mTvSeekbarValue = (TextView) findViewById(R.id.txt_seek_value);
        mSurveySlider = (LinearLayout) findViewById(R.id.slider_layout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(SurveyActivity.this));

        postAnswer = String.valueOf(mRbSurveyBar.getRating());
        mTvRatingDisplay.setText(postAnswer);

        mBtNext.setOnClickListener(this);
        progressBar.setProgress(0);


        mRbSurveyBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                postAnswer = String.valueOf(rating);
                mTvRatingDisplay.setText(postAnswer);
            }
        });

        surveySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarProgress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float seekValue = (float) (seekBarProgress / 2.0);
                postAnswer = String.valueOf(seekValue);
                mTvSeekbarValue.setText(postAnswer);
            }
        });
    }

    /**
     * To setup screen toolbar
     */
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
                onBackPressed();
            }
        });
    }

    /* To call get survey question from services */
    private void loadSurveyQuestion() {
        try {
            Object data = GroupSurveyQuestionActivity.group_id+"/"+userId;
            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
            inputParams.put("", "");
            controller.handleMessage(ResReqController.SURVEY_QUESTION, inputParams, data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {

        if (mLlSurvey.getVisibility() == View.GONE
                && mTvNoQuestion.getVisibility() == View.VISIBLE) {
            finish();
        } else {
            AlertDialog diaBox = AskOption();
            diaBox.show();
        }
    }


    /* To handle response from services */
    @Override
    public boolean handleMessage(Message msg) {

        GsonBuilder gsonBuilder;
        Gson gson;
        ResponseDto category;
        try {

            switch (msg.what) {
                case ResReqController.SURVEY_QUESTION_SUCCESS:
                    gsonBuilder = new GsonBuilder();
                    gson = gsonBuilder.create();
                    category = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (category != null
                            && category.getStatus().equalsIgnoreCase("true")) {
                        // Get survey question
                        List<SurveyQuestionDto> surveyQuestionDtoList = category.getSurveyQuestionList();

                        if (surveyQuestionDtoList != null
                                && surveyQuestionDtoList.size() > 0) {
                            // Set progress bar max value from survey question
                            progressBar.setMax(surveyQuestionDtoList.size());
                            // To assign all survey question to survey services class
                            SurveyServices.getInstance().setAllQuestions(SurveyActivity.this, surveyQuestionDtoList);
                        } else {
                            mTvNoQuestion.setVisibility(View.VISIBLE);
                            mLlSurvey.setVisibility(View.GONE);
                        }
                    }
                    break;

                case ResReqController.SURVEY_ANSWER_SUCCESS:
                    gsonBuilder = new GsonBuilder();
                    gson = gsonBuilder.create();
                    category = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (category != null
                            && category.getStatus().equalsIgnoreCase("true")) {
                        startThankActivity();
                    }
                    break;

                case ResReqController.SURVEY_QUESTION_FAILED:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onQuestionChanged(SurveyQuestionDto question) {

        checkQuestionType(question);
    }


    /**
     * If user attend all survey question then move to thank screen based condition
     */
    private void startThankActivity() {

        // To check the user currently attend question and total question
        if (SurveyServices.getInstance().getCurrentQuestionNo() == SurveyServices
                .getInstance().getTotalQuestionCount()) {

            Intent i = new Intent(SurveyActivity.this, SurveyEndActivity.class);
            startActivity(i);
            finish();
        } else {
            onNextQuestionClicked();
        }
    }

    /**
     * To set question and it's answer
     *
     * @param question
     */
    private void setQuestion(SurveyQuestionDto question) {

        List<SurveyOptionDto> surveyOptionList = question.getSurveyOptionList();

        // Check question type (ie - Single choice or multi choice)
        if (question.getOptionType().equalsIgnoreCase("CHECK_BOX")) optionType = true;
        else optionType = false;

        // Call question answer adapter
        adapter = new SurveyAnswerAdapter(SurveyActivity.this, surveyOptionList,
                optionType, SurveyActivity.this, selectedLanguage);
        mRecyclerView.setAdapter(adapter);

    }

    /**
     * Check survey question type and show
     *
     * @param question
     */

    private void checkQuestionType(SurveyQuestionDto question) {


        postAnswer = "";
        progressBar.setProgress(0);
        surveySeekBar.setProgress(0);
        int currentQues = SurveyServices.getInstance().getCurrentQuestionNo();
        progressBar.setProgress(currentQues);
        mTvQuestionNo.setText("" + currentQues);
        // Check user select language
        if (selectedLanguage.equalsIgnoreCase("ta"))
            mTvQuestion.setText(question.getRegionalQuestion());
        else mTvQuestion.setText(question.getQuestion());

        if (question.getOptionType().equalsIgnoreCase("SLIDER")) {
            hideLayouts = true;
            mRecyclerView.setVisibility(View.GONE);
            mRlSurveyRating.setVisibility(View.GONE);
            mSurveySlider.setVisibility(View.VISIBLE);
        } else if (question.getOptionType().equalsIgnoreCase("RATING")) {
            hideLayouts = true;
            mRecyclerView.setVisibility(View.GONE);
            mRlSurveyRating.setVisibility(View.VISIBLE);
            mSurveySlider.setVisibility(View.GONE);
        } else {
            hideLayouts = false;
            mRecyclerView.setVisibility(View.VISIBLE);
            mRlSurveyRating.setVisibility(View.GONE);
            mSurveySlider.setVisibility(View.GONE);
            setQuestion(question);
        }
    }

    /* Call next question */
    private void onNextQuestionClicked() {
        selectAnswer.clear();
        SurveyServices.getInstance().loadNextQuestion(SurveyActivity.this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_survey_next:
                postAnswer();
                break;
        }
    }

    @Override
    public void selectAnswer(int position, String answer, boolean isSelected) {

        /* If user select answer above lister will be call*/
        if (optionType) {
            if (isSelected) {
                if (!selectAnswer.contains(answer))
                    selectAnswer.add(answer);
            } else {
                if (selectAnswer.contains(answer))
                    selectAnswer.remove(answer);
            }
        } else {
            selectAnswer.clear();
            selectAnswer.add(answer);
        }
    }

    private void postAnswer() {

        Object data = userId;
        HashMap<String, Object> inputParams = null;
        inputParams = Utilities.getInstance().getRequestParams();
        inputParams.put("generalVoterId", userId);
        inputParams.put("surveyQuestionId", SurveyServices.getInstance().getCurrentQuestionId());


        if (hideLayouts) {

            if (postAnswer != null) {
                inputParams.put("answer", postAnswer);
                inputParams.put("surveyOptionId", "");
                controller.handleMessage(ResReqController.SURVEY_ANSWER, inputParams, data);
            } else {
                Toast.makeText(SurveyActivity.this, context.getString(R.string.toast_Please_select_rating), Toast.LENGTH_SHORT).show();
            }

        } else {
            if ((selectAnswer != null)
                    && selectAnswer.size() > 0) {
                postAnswer = "";
                for (int q = 0; q < selectAnswer.size(); q++) {
                    postAnswer = selectAnswer.get(q) + "-" + postAnswer;
                }

                postAnswer = postAnswer.substring(0, postAnswer.length() - 1);
                if (optionType) {
                    inputParams.put("answer", postAnswer);
                    inputParams.put("surveyOptionId", "");
                } else {
                    inputParams.put("answer", "");
                    inputParams.put("surveyOptionId", postAnswer);
                }

                controller.handleMessage(ResReqController.SURVEY_ANSWER, inputParams, data);
            } else {
                Toast.makeText(SurveyActivity.this,context.getString(R.string.toast_please_select_answer), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("Survey is not completed")
                .setMessage("Are you sure you want to exit")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }
}