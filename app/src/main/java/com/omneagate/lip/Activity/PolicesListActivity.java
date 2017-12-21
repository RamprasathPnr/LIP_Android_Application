package com.omneagate.lip.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Adaptor.DepartmentListAdapter;
import com.omneagate.lip.Adaptor.PoliciesListAdapter;
import com.omneagate.lip.Model.GrievanceCategoryDto;
import com.omneagate.lip.Model.PolicyDto;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.MySharedPreference;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user1 on 11/6/16.
 */
public class PolicesListActivity extends BaseActivity implements View.OnClickListener, Handler.Callback {

    String language, userId;
    private ResponseDto responseData;
    final ResReqController controller = new ResReqController(this);
    private TextView title;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private LinearLayoutManager mLayoutManager;
    ListView mListView;
    PoliciesListAdapter adapter;
    List<PolicyDto> policiesDtoList;
    PolicyDto departmentdto;
    Context context;
    String selectedLanguage;
    private static final String TAG = PolicesListActivity.class.getCanonicalName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departmentlist);
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage = languageChcek();
        context = this;
        configureInitialPage();
    }

    private void configureInitialPage() {

        controller.addOutboxHandler(new Handler(this));
        title = (TextView) findViewById(R.id.title_toolbar);
        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {
            if (selectedLanguage.equalsIgnoreCase("en")) {
                title.setText("Governance Policy");
            } else if (selectedLanguage.equalsIgnoreCase("ta")) {

                title.setText("शासन की नीति");

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        language = MySharedPreference.readString(getApplicationContext(), MySharedPreference.LANGUAGE_SELECT, "");
        String userdetailsDTO = MySharedPreference.readString(getApplicationContext(), MySharedPreference.USER_DETAILS, "");
        responseData = new Gson().fromJson(userdetailsDTO, ResponseDto.class);
        getAllPolices_list();
        mListView = (ListView) findViewById(R.id.department_list);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                departmentdto = policiesDtoList.get(position);
                String feedback = departmentdto.getFeedBackComments();

                if (feedback == null) {
                    //Toast.makeText(PolicesListActivity.this, "" + feedback, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), PoliciesDetailActivity.class);
                    String appointment = new Gson().toJson(policiesDtoList.get(position));
                    i.putExtra("policy_detail", appointment);
                    startActivity(i);
                    finish();
                } else {
                    //Toast.makeText(PolicesListActivity.this, "" + feedback, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), PoliciesDetailActivity.class);
                    String appointment = new Gson().toJson(policiesDtoList.get(position));
                    i.putExtra("policy_detail", appointment);
                    startActivity(i);
                    finish();


                }

            }
        });
    }

    private void getAllPolices_list() {
        try {
            userId = responseData.getGeneralVoterDto().getId();
            Object data = userId;
            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
            inputParams.put("", "");
            Log.e("userid", "" + data);
            controller.handleMessage(ResReqController.GET_POLICES_LIST, inputParams, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.GET_POLICES_SUCCESS:
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    ResponseDto responseDto = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (responseDto != null && responseDto.getStatus().equalsIgnoreCase("true")) {
                        policiesDtoList = responseDto.getPolicyDtoList();
                        Log.e("policeslist", "" + policiesDtoList.toString());
                        setDepartmentAdapter(policiesDtoList);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void setDepartmentAdapter(List<PolicyDto> record) {
        if (record.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.nodata_tv)).setVisibility(View.GONE);
            adapter = new PoliciesListAdapter(context, record);
            mListView.setAdapter(adapter);
        } else {
            mListView.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.nodata_tv)).setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {

    }


}
