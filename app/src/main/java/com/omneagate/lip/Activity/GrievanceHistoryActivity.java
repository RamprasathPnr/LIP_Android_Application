package com.omneagate.lip.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.omneagate.lip.Adaptor.GrievanceHistoryAdaptor;
import com.omneagate.lip.Model.GrievanceHistoryDto;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.MySharedPreference;

import java.util.HashMap;

/**
 * Created by user1 on 3/6/16.
 */
public class GrievanceHistoryActivity extends BaseActivity implements Handler.Callback {

    Context context;
    private TextView title;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private ListView grievacne_lv;
    private GrievanceHistoryAdaptor adaptor;
    private ResponseDto responseData, responseList;
    public static ResponseDto responseData_;
    private String userId;
    String selectedLanguage;
    private GrievanceHistoryDto grievance;
    final ResReqController controller = new ResReqController(this);
    private TextView mTvNoHistory;
    private static final String TAG = GrievanceHistoryActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grievance_history_activity);
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage = languageChcek();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = this;
        setUpView();
        controller.addOutboxHandler(new Handler(this));
        grievanceHistory();
    }

    private void setUpView() {
        String userdetailsDTO = MySharedPreference.readString(getApplicationContext(), MySharedPreference.USER_DETAILS, "");
        responseData = new Gson().fromJson(userdetailsDTO, ResponseDto.class);
        userId = responseData.getGeneralVoterDto().getId();

        mTvNoHistory = (TextView) findViewById(R.id.txt_no_history);
        grievacne_lv = (ListView) findViewById(R.id.grievacne_lv);
        title = (TextView) findViewById(R.id.title_toolbar);
        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {


            if (selectedLanguage.equalsIgnoreCase("en")) {
                title.setText("Grievance History");
            } else if (selectedLanguage.equalsIgnoreCase("ta")) {
                title.setText("शिकायत इतिहास");
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

        grievacne_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                grievance = responseData_.getListComplaintDto().get(position);
                Intent i = new Intent(GrievanceHistoryActivity.this, GrievancesHistoryFullViewActivity.class);
                String grievance_ = new Gson().toJson(grievance);
                i.putExtra("grievance_details", grievance_);
                startActivity(i);
            }
        });
    }

    private void grievanceHistory() {
        try {
            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
            inputParams.put("", "");
            Object v_id = userId;
            controller.handleMessage(ResReqController.GRIEVANCE_HISTORY, inputParams, v_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.GRIEVANCE_HISTORY_SUCCESS:
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    responseList = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (responseList.getStatus().equalsIgnoreCase("true")) {
                        responseData_ = responseList;
                        if (responseList != null
                                && responseList.getListComplaintDto() != null
                                && responseList.getListComplaintDto().size() > 0) {
                            adaptor = new GrievanceHistoryAdaptor(context,
                                    responseList.getListComplaintDto());
                            grievacne_lv.setAdapter(adaptor);
                        } else {
                            grievacne_lv.setVisibility(View.GONE);
                            mTvNoHistory.setVisibility(View.VISIBLE);
                        }
                    } else {
                        grievacne_lv.setVisibility(View.GONE);
                        mTvNoHistory.setVisibility(View.VISIBLE);
                    }
                    return true;

                case ResReqController.GRIEVANCE_HISTORY_FAILED:
                    Toast.makeText(GrievanceHistoryActivity.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
