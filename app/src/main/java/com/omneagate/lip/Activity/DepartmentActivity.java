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
import com.omneagate.lip.Model.GrievanceCategoryDto;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.MySharedPreference;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user1 on 10/6/16.
 */
public class DepartmentActivity extends BaseActivity implements View.OnClickListener, Handler.Callback {
    String language, userId;
    private ResponseDto responseData;
    final ResReqController controller = new ResReqController(this);
    private TextView title;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private LinearLayoutManager mLayoutManager;
    ListView mListView;
    DepartmentListAdapter adapter;
    List<GrievanceCategoryDto> departmentDtoList;
    GrievanceCategoryDto departmentdto;
    Context context;
    String selectedLanguage;
    private static final String TAG = DepartmentActivity.class.getCanonicalName();

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

        if (selectedLanguage.equalsIgnoreCase("en")) {
            title.setText("Departments");
        } else if (selectedLanguage.equalsIgnoreCase("ta")) {

            title.setText("विभागों");

        }


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        getAllDepartment_list();

        mListView = (ListView) findViewById(R.id.department_list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //  getAllDepartment_detail(departmentDtoList.get(position).getId());


                departmentdto = departmentDtoList.get(position);

                String feedback = departmentdto.getFeedBackComments();

                if (feedback != null) {
                    Intent i = new Intent(getApplicationContext(), DepartmentDetailActivity.class);
                    String appointment = new Gson().toJson(departmentdto);
                    i.putExtra("department_detail", appointment);
                    startActivity(i);
                    finish();

                } else {

                    Intent i = new Intent(getApplicationContext(), DepartmentDetailActivity.class);
                    String appointment = new Gson().toJson(departmentdto);
                    i.putExtra("department_detail", appointment);
                    startActivity(i);
                    finish();
                }


            }
        });

    }

    private void getAllDepartment_list() {
        userId = responseData.getGeneralVoterDto().getId();
        Object data = userId;
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        inputParams.put("", "");
        Log.e("userid", "" + data);
        controller.handleMessage(ResReqController.GET_DEPARTMENT_LIST, inputParams, data);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case ResReqController.GET_DEPARTMENT_SUCCESS:
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                ResponseDto responseDto = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                if (responseDto != null && responseDto.getStatus().equalsIgnoreCase("true")) {
                    departmentDtoList = responseDto.getGrievanceCategoryList();
                    setDepartmentAdapter();
                }
                break;
            case ResReqController.GET_DEPARTDETAIL__SUCCESS:
                GsonBuilder gsonBuilder1 = new GsonBuilder();
                Gson gson1 = gsonBuilder1.create();
                ResponseDto responseDto1 = gson1.fromJson(msg.obj.toString(), ResponseDto.class);
                if (responseDto1 != null && responseDto1.getStatus().equalsIgnoreCase("true")) {
                    //  DepartmentFeedbackDto departmentFeedback = responseDto1.getDepartmentFeedbackDto();
                    Intent i = new Intent(getApplicationContext(), DepartmentDetailActivity.class);
                    String appointment = new Gson().toJson(departmentdto);
                    i.putExtra("department_detail", appointment);
                    startActivity(i);
                    finish();
                }
                break;

            case ResReqController.GET_DEPARTDETAIL_FAILED:
                Toast.makeText(DepartmentActivity.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                Log.d("Error", "" + msg.obj.toString());
                return true;
        }
        return true;
    }

    private void setDepartmentAdapter() {
        if (departmentDtoList.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.nodata_tv)).setVisibility(View.GONE);
            adapter = new DepartmentListAdapter(context, departmentDtoList);
            mListView.setAdapter(adapter);
        } else {
            mListView.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.nodata_tv)).setVisibility(View.VISIBLE);
        }

    }

    private void getAllDepartment_detail(String deaprtmentid) {
        userId = responseData.getGeneralVoterDto().getId();
        String data1 = deaprtmentid + "/" + userId;
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        inputParams.put("", "");
        Object data = data1;
        controller.handleMessage(ResReqController.GET_DEPARTMENT_DETAIL, inputParams, data);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
