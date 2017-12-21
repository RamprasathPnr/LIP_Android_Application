package com.omneagate.lip.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Model.AppointmentListDto;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Utility.MySharedPreference;

/**
 * Created by user on 7/6/16.
 */
public class AppointmentHistoryActivity extends BaseActivity {

    private TextView title;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private String data;
    ResponseDto response;
    private AppointmentListDto appointmentDetails;
    private String selectedLanguage;
    private static final String TAG = AppointmentHistoryActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);
        Application.getInstance().setGoogleTracker(TAG);
        setTitle("");
        selectedLanguage = languageChcek();
        setUpView();
    }

    public void setUpView() {
        String userdetailsDTO = MySharedPreference.readString(getApplicationContext(), MySharedPreference.USER_DETAILS, "");
        response = new Gson().fromJson(userdetailsDTO, ResponseDto.class);

        data = getIntent().getStringExtra("appointment_details");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        appointmentDetails = gson.fromJson(data, AppointmentListDto.class);

        title = (TextView) findViewById(R.id.title_toolbar);
        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {

            if (selectedLanguage.equalsIgnoreCase("en")) {
                title.setText("Appointment Details");
            } else if (selectedLanguage.equalsIgnoreCase("ta")) {

                title.setText("नियुक्ति विवरण");

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


        if (selectedLanguage.equalsIgnoreCase("en")) {
            ((TextView) findViewById(R.id.appoint_number_tv)).setText(appointmentDetails.getAppointmentNumber());
            ((TextView) findViewById(R.id.name_tv)).setText(response.getGeneralVoterDto().getAdminName() );                 // Need to Changed as per API
            ((TextView) findViewById(R.id.appointment_timining)).setText(appointmentDetails.getAppointmentDateString());
            ((TextView) findViewById(R.id.address_tv)).setText(appointmentDetails.getAddress());
            ((TextView) findViewById(R.id.reason_tv)).setText(appointmentDetails.getReason());
            ((TextView) findViewById(R.id.status_tv)).setText(appointmentDetails.getStatus());
        } else if (selectedLanguage.equalsIgnoreCase("ta")) {

            ((TextView) findViewById(R.id.appoint_number_tv)).setText(appointmentDetails.getAppointmentNumber());
            ((TextView) findViewById(R.id.name_tv)).setText(response.getGeneralVoterDto().getAdminRegionalName() + " MLA");
            ((TextView) findViewById(R.id.appointment_timining)).setText(appointmentDetails.getAppointmentDateString());
            ((TextView) findViewById(R.id.address_tv)).setText(appointmentDetails.getAddress());
            ((TextView) findViewById(R.id.reason_tv)).setText(appointmentDetails.getReason());
            ((TextView) findViewById(R.id.status_tv)).setText(appointmentDetails.getStatus());

        }


    }
}
