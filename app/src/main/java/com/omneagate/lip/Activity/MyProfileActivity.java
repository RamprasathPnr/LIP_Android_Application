package com.omneagate.lip.Activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Utility.MySharedPreference;

/**
 * Created by user on 2/6/16.
 */
public class MyProfileActivity extends BaseActivity {

    private Toolbar toolbar;
    private ResponseDto responseData;
    String address, pincode, vot_number, mobile_number, email_id, dob, name, type_of_proof, id_number, district, constituency, dobString;
    TextView adress_tv, pincode_tv, vot_number_tv, mob_number_tv, email_tv, dob_tv, user_name_tv, type_of_proof_tv, id_number_tv, district_tv, constituency_tv;
    private TextView title;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private String selectedLanguage;
    private static final String TAG = MyProfileActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage = languageChcek();
        title = (TextView) findViewById(R.id.title_toolbar);

        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {
            if (selectedLanguage.equalsIgnoreCase("en")) {
                title.setText("My Profile");
            } else if (selectedLanguage.equalsIgnoreCase("ta")) {
                         title.setText("मेरी प्रोफाइल");

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


        adress_tv = (TextView) findViewById(R.id.adress_tv);
        pincode_tv = (TextView) findViewById(R.id.pincode_tv);
        mob_number_tv = (TextView) findViewById(R.id.mob_number_tv);
        email_tv = (TextView) findViewById(R.id.email_tv);
        dob_tv = (TextView) findViewById(R.id.dob_tv);
        user_name_tv = (TextView) findViewById(R.id.user_name_tv);
        type_of_proof_tv = (TextView) findViewById(R.id.type_of_proof_tv);
        id_number_tv = (TextView) findViewById(R.id.id_number_tv);
        district_tv = (TextView) findViewById(R.id.district_tv);
        constituency_tv = (TextView) findViewById(R.id.constituency_tv);

        String userdetailsDTO = MySharedPreference.readString(getApplicationContext(), MySharedPreference.USER_DETAILS, "");
        responseData = new Gson().fromJson(userdetailsDTO, ResponseDto.class);

        address = responseData.getGeneralVoterDto().getAddress();
        pincode = responseData.getGeneralVoterDto().getPinCode();
        vot_number = responseData.getGeneralVoterDto().getVoterId();
        mobile_number = responseData.getGeneralVoterDto().getMobileNumber();
        email_id = responseData.getGeneralVoterDto().getEmailId();
        //dob = responseData.getGeneralVoterDto().getDob();
        dobString = responseData.getGeneralVoterDto().getDobString();
        name = responseData.getGeneralVoterDto().getName();
        type_of_proof = responseData.getGeneralVoterDto().getIdentityProofType();
        id_number = responseData.getGeneralVoterDto().getIdentityProofNumber();
        district = responseData.getGeneralVoterDto().getDistictName();
        constituency = responseData.getGeneralVoterDto().getConstituencyName();

        if (selectedLanguage.equalsIgnoreCase("en")) {
            adress_tv.setText(address);
            pincode_tv.setText(pincode);
            //vot_number_tv.setText(vot_number);
            mob_number_tv.setText("+91 " + mobile_number);
            email_tv.setText(email_id);
            dob_tv.setText(dobString);
            user_name_tv.setText(name);
            type_of_proof_tv.setText(type_of_proof);
            id_number_tv.setText(id_number);

            district_tv.setText(district);
            constituency_tv.setText(constituency);
        } else if (selectedLanguage.equalsIgnoreCase("ta")) {

            district = responseData.getGeneralVoterDto().getDistictRegionalName();
            constituency = responseData.getGeneralVoterDto().getConstituencyRegionalName();


            district_tv.setText(district);
            constituency_tv.setText(constituency);

            adress_tv.setText(address);
            pincode_tv.setText(pincode);
            mob_number_tv.setText("+91"+mobile_number);
            email_tv.setText(email_id);
            dob_tv.setText(dobString);
            user_name_tv.setText(name);
            type_of_proof_tv.setText(type_of_proof);
            id_number_tv.setText(id_number);
            district_tv.setText(district);

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
