package com.omneagate.lip.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Adaptor.GroupSurveyListAdaptor;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.MySharedPreference;

import java.util.HashMap;

/**
 * Created by USER1 on 17-06-2016.
 */
public class GroupSurveyQuestionActivity extends BaseActivity implements Handler.Callback{

    private String selectedLanguage;
    private RecyclerView group_survey_list;
    private Context context;
    private GroupSurveyListAdaptor adaptor;
    private ActionBar mActionBar;
    final ResReqController controller = new ResReqController(this);
    private TextView title;
    private Toolbar mToolbar;
    private ResponseDto responseData;
    public static String group_id;
    private static final String TAG = GroupSurveyQuestionActivity.class.getCanonicalName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_survey_list);
        context = this;
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage = languageChcek();
        controller.addOutboxHandler(new Handler(this));

        String userdetailsDTO = MySharedPreference.readString(getApplicationContext(), MySharedPreference.USER_DETAILS, "");
        responseData = new Gson().fromJson(userdetailsDTO, ResponseDto.class);
        setView();
    }

    private void setView() {
        title = (TextView) findViewById(R.id.title_toolbar);
        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {
            if (selectedLanguage.equalsIgnoreCase("en")) {
                title.setText("Group Survey");
            } else if (selectedLanguage.equalsIgnoreCase("ta")) {
                title.setText("குழு ஆய்வு");
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

        group_survey_list = (RecyclerView)findViewById(R.id.survey_group);
        group_survey_list.setLayoutManager(new LinearLayoutManager(GroupSurveyQuestionActivity.this));
        getAllGroupData();
    }

    private void getAllGroupData() {

//        Object data = responseData.getGeneralVoterDto().getId();
        Object data = "";
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        inputParams.put("", "");
        controller.handleMessage(ResReqController.GROUP_SURVEY_LIST_, inputParams, data);
    }

    @Override
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.GROUP_SURVEY_LIST_SUCCESS:
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    Log.d("Check", "" + msg.obj.toString());
                    ResponseDto groupSurveyList  = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (groupSurveyList.getStatus().equalsIgnoreCase("true")) {
                        if(groupSurveyList.getSurveyUserGroupDtoList().size()>0){
                            try{
                                ((TextView)findViewById(R.id.no_group_survey)).setVisibility(View.GONE);
                                group_survey_list.setVisibility(View.VISIBLE);
                                // Call question answer adapter
                                adaptor = new GroupSurveyListAdaptor(GroupSurveyQuestionActivity.this, groupSurveyList.getSurveyUserGroupDtoList(), selectedLanguage);
                                group_survey_list.setAdapter(adaptor);
                            }catch (Exception e){
                                Log.d("Error",""+e.toString());
                            }
                        }else{
                            group_survey_list.setVisibility(View.GONE);
                            ((TextView)findViewById(R.id.no_group_survey)).setVisibility(View.VISIBLE);
                        }
                    }
                    return true;

                case ResReqController.GROUP_SURVEY_LIST_FAILED:
                    Toast.makeText(GroupSurveyQuestionActivity.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());

                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
