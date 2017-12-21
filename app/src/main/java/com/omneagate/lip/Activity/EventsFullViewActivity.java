package com.omneagate.lip.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Model.CitizenEventDto;
import com.omneagate.lip.Model.EventDto;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.ConstantsKeys;
import com.omneagate.lip.Utility.MySharedPreference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * Created by user on 10/6/16.
 */
public class EventsFullViewActivity extends BaseActivity implements Handler.Callback, View.OnClickListener {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    String selectedLanguage;
    private EventDto event_dto;
    private TextView TVtitle;
    private TextView TveventTime, TVeventDate, TVevent_location;
    private ImageView IVimageUrl;
    private LinearLayout mLlStatus;
   // private TextView mTvEventStatus;
    TextView TvStatus_title,TvStatus_value;

    private ResReqController controller;
    private String userId;
    private String eventId;
    private TextView title;

    private WebView myWebview;

    Context context=this;
    private static final String TAG = EventsFullViewActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_fullview);
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage = languageChcek();


        String userDetailsDTO = MySharedPreference.readString(EventsFullViewActivity.this,
                MySharedPreference.USER_DETAILS, "");
        ResponseDto responseData = new Gson().fromJson(userDetailsDTO, ResponseDto.class);
        userId = responseData.getGeneralVoterDto().getId();

        controller = new ResReqController(EventsFullViewActivity.this);
        controller.addOutboxHandler(new Handler(this));
        Intent intent = getIntent();
        event_dto = (EventDto) intent.getSerializableExtra(ConstantsKeys.EVENT_DETAIL);
        eventId = event_dto.getId();
        setUpView();
        setupValue();
        loadCitizenEvent();

    }

    private void setupValue() {
        try {
            if (event_dto != null) {

                if (selectedLanguage.equalsIgnoreCase("ta")) {
                    TVtitle.setText(event_dto.getRegionalTitle());
                    myWebview.loadData(event_dto.getRegionalDescription(), "text/html", "UTF-8");

                } else {
                    TVtitle.setText(event_dto.getTitle());
                    myWebview.loadData(event_dto.getDescription(), "text/html", "UTF-8");
                }

                //
                TVeventDate.setText(event_dto.getDay());
                TveventTime.setText(event_dto.getTime());
                TVevent_location.setText(event_dto.getAddress());

                Picasso.with(this)
                        .load(event_dto.getImageUrl()).placeholder(R.drawable.no_available_image_200x150)
                        .into(IVimageUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpView() {
        try {

            setTitle("");
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            mActionBar = getSupportActionBar();
            title = (TextView) findViewById(R.id.title_toolbar);

            TVtitle = (TextView) findViewById(R.id.event_title);
            //mTvEventStatus = (TextView) findViewById(R.id.txt_event_status);
            //TVeventDescription = (TextView) findViewById(R.id.event_description);
            TvStatus_title=(TextView)findViewById(R.id.status_event_txt);
            TvStatus_value=(TextView)findViewById(R.id.status_event);
            myWebview = (WebView) findViewById(R.id.wv_event_details);
            TVeventDate = (TextView) findViewById(R.id.event_date);
            TveventTime = (TextView) findViewById(R.id.event_time);
            TVevent_location = (TextView) findViewById(R.id.location);
            mLlStatus = (LinearLayout) findViewById(R.id.btn_participate_lay);
            mLlStatus.setVisibility(View.GONE);
            TvStatus_title.setVisibility(View.GONE);
            TvStatus_value.setVisibility(View.GONE);
            mLlStatus.setOnClickListener(this);

            IVimageUrl = (ImageView) findViewById(R.id.event_image);

            if (getSupportActionBar() != null) {


                if (selectedLanguage.equalsIgnoreCase("en")) {
                    title.setText("Event");
                } else if (selectedLanguage.equalsIgnoreCase("ta")) {

                    title.setText("घटना");

                }
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadCitizenEvent() {
        try {

            Object data = eventId + "/" + userId;
            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
            inputParams.put("", "");
            controller.handleMessage(ResReqController.CITIZEN_EVENT, inputParams, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        GsonBuilder gsonBuilder;
        Gson gson;
        ResponseDto category;
         try{
        switch (message.what) {

            case ResReqController.CITIZEN_EVENT_SUCCESS:
                gsonBuilder = new GsonBuilder();
                gson = gsonBuilder.create();
                category = gson.fromJson(message.obj.toString(), ResponseDto.class);
                if (category != null
                        && category.getStatus().equalsIgnoreCase("true")) {
                    CitizenEventDto citizenEventDto = category.getCitizenEventDto();

                    if (citizenEventDto == null || citizenEventDto.getStatus() == null) {
                        mLlStatus.setVisibility(View.VISIBLE);
                        mLlStatus.setOnClickListener(this);
                    } else {
                        // mLlStatus.setVisibility(View.VISIBLE);
                        //mTvEventStatus.setText(citizenEventDto.getStatus());
                        // mLlStatus.setOnClickListener(null);
                        mLlStatus.setVisibility(View.GONE);
                        TvStatus_title.setVisibility(View.VISIBLE);
                        TvStatus_value.setVisibility(View.VISIBLE);
                        if(citizenEventDto.getStatus().equalsIgnoreCase("VISITED")){
                            TvStatus_value.setText("Awaiting Response");
                        }else{
                            TvStatus_value.setText(citizenEventDto.getStatus());
                        }
                    }

                }
                break;
            case ResReqController.ADD_CITIZEN_SUCCESS:
                gsonBuilder = new GsonBuilder();
                gson = gsonBuilder.create();
                category = gson.fromJson(message.obj.toString(), ResponseDto.class);
                if (category != null
                        && category.getStatus().equalsIgnoreCase("true")) {
                    loadCitizenEvent();
                    Toast.makeText(EventsFullViewActivity.this, context.getString(R.string.toast_rating_interest), Toast.LENGTH_SHORT).show();
                }
                break;

        }}catch (Exception e){
             e.printStackTrace();
         }
        return false;
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {

                case R.id.btn_participate_lay:
                    Object data = "";
                    HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
                    inputParams.put("eventId", eventId);
                    inputParams.put("generalVoterId", userId);
                    inputParams.put("status", "VISITED");
                    inputParams.put("description", "VISITED");
                    controller.handleMessage(ResReqController.ADD_CITIZEN, inputParams, data);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}