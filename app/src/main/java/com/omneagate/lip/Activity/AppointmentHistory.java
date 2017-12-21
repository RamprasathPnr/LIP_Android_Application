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
import com.omneagate.lip.Adaptor.AppointmentHistoryAdaptor;
import com.omneagate.lip.Model.AppointmentListDto;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.MySharedPreference;

import java.util.HashMap;

/**
 * Created by user1 on 6/6/16.
 */
public class AppointmentHistory extends BaseActivity implements Handler.Callback {

    Context context;
    private TextView title;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private ListView grievacne_lv;
    private AppointmentHistoryAdaptor adaptor;
    private ResponseDto responseData, responseList;
    public static ResponseDto responseData_;
    private String userId;
    private AppointmentListDto appointmentDetails;
    final ResReqController controller = new ResReqController(this);
    private String selectedLanguage;
    private TextView mTvNoAppoinment;
    private static final String TAG = AppointmentHistory.class.getCanonicalName();

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
        appointmentHistory();
    }

    private void setUpView() {
        String userdetailsDTO = MySharedPreference.readString(getApplicationContext(), MySharedPreference.USER_DETAILS, "");
        responseData = new Gson().fromJson(userdetailsDTO, ResponseDto.class);
        userId = responseData.getGeneralVoterDto().getId();
        mTvNoAppoinment = (TextView) findViewById(R.id.txt_no_history);
        grievacne_lv = (ListView) findViewById(R.id.grievacne_lv);
        title = (TextView) findViewById(R.id.title_toolbar);
        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {

            if (selectedLanguage.equalsIgnoreCase("en")) {
                title.setText("Appointment History");
            } else if (selectedLanguage.equalsIgnoreCase("ta")) {

                title.setText("नियुक्ति इतिहास");

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

        grievacne_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                appointmentDetails = responseData_.getAppointmentRequestDtoList().get(position);
                Intent i = new Intent(AppointmentHistory.this, AppointmentHistoryActivity.class);
                String appointment = new Gson().toJson(appointmentDetails);
                i.putExtra("appointment_details", appointment);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.APPOINTMENT_HISTORY_SUCCESS:
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    responseList = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (responseList.getStatus().equalsIgnoreCase("true")) {
                        responseData_ = responseList;
                        if (responseList != null
                                && responseList.getAppointmentRequestDtoList() != null
                                && responseList.getAppointmentRequestDtoList().size() > 0) {
                            adaptor = new AppointmentHistoryAdaptor(context, responseList.getAppointmentRequestDtoList());
                            grievacne_lv.setAdapter(adaptor);
                        } else {
                            grievacne_lv.setVisibility(View.GONE);
                            mTvNoAppoinment.setVisibility(View.VISIBLE);
                        }
                    }
                    return true;

                case ResReqController.APPOINTMENT_HISTORY_FAILED:
                    Toast.makeText(AppointmentHistory.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void appointmentHistory() {
        try {
            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
            inputParams.put("", "");
            Object v_id = userId;
            controller.handleMessage(ResReqController.APPOINTMENT_HISTORY, inputParams, v_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
