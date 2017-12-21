package com.omneagate.lip.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.MySharedPreference;

import java.util.ArrayList;
import java.util.HashMap;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by user on 2/7/16.
 */
public class PaymentDetailActivity extends BaseActivity implements Handler.Callback, View.OnClickListener {


    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private TextView title;
    private TextView tv_sub_total;
    private TextView tv_shipping_total;
    private TextView tv_total;
    private CheckBox user_detail;
    private String selectedLanguage;
    private ResponseDto responseData;
    private Object distr_id;
    public static ResponseDto otpresponse;
    private EditText name, email, address, ph_number, district;
    private MaterialSpinner spinner_district;
    private Object[] mStringArray;
    private ArrayAdapter adapter;
    private ResReqController controller;
    private Button save;
    private String final_cost;
    private int final_quantity;
    private String sub_total;
    private String shipping_total;
    private String total;
    private String district_id, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        selectedLanguage = languageChcek();
        setUpView();


    }

    public void setUpView() {
        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.title_toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {
            title.setText(getString(R.string.title_Payment_Detail));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        spinner_district = (MaterialSpinner) findViewById(R.id.spinner_pay_district);
        spinner_district.setHint("Select District");
        district = (EditText) findViewById(R.id.dest_pay_edtxt);

        controller = new ResReqController(PaymentDetailActivity.this);
        controller.addOutboxHandler(new Handler(this));


        name = (EditText) findViewById(R.id.name_pay_edtxt);
        email = (EditText) findViewById(R.id.email_pay_edtxt);
        address = (EditText) findViewById(R.id.address_pay_edtxt);
        ph_number = (EditText) findViewById(R.id.number_pay_edtxt);
        save = (Button) findViewById(R.id.button_save);
        save.setOnClickListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        user_detail = (CheckBox) findViewById(R.id.checkbox_user_details);
        user_detail.setOnClickListener(this);

        Bundle intent = getIntent().getExtras();
        sub_total = intent.getString("subtotal");
        shipping_total = intent.getString("shipping");
        total = intent.getString("total");
        final_quantity = intent.getInt("final_quantity");
        final_cost = intent.getString("final_cost");

        tv_sub_total = (TextView) findViewById(R.id.text_view_subtotal);
        tv_shipping_total = (TextView) findViewById(R.id.text_view_shipping_amount);
        tv_total = (TextView) findViewById(R.id.text_view_total_amount);

        tv_sub_total.setText(sub_total);
        tv_shipping_total.setText(shipping_total);
        tv_total.setText(total);

        getDistrict();

        spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    distr_id = otpresponse.getDistrictList().get(position).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkbox_user_details:
                setUserDetails();
                break;

            case R.id.button_save:
                checkUserDetails();
                break;


        }
    }

    public void getDistrict() {
        try {
            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
            inputParams.put("", "");
            controller.handleMessage(ResReqController.GET_DISTRICT, inputParams, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setUserDetails() {
        if (user_detail.isChecked()) {
            String userdetailsDTO = MySharedPreference.readString(getApplicationContext(), MySharedPreference.USER_DETAILS, "");
            responseData = new Gson().fromJson(userdetailsDTO, ResponseDto.class);

            name.setText(responseData.getGeneralVoterDto().getName());
            email.setText(responseData.getGeneralVoterDto().getEmailId());
            address.setText(responseData.getGeneralVoterDto().getAddress());
            ph_number.setText(responseData.getGeneralVoterDto().getMobileNumber());
            spinner_district.setVisibility(View.GONE);
            district_id = responseData.getGeneralVoterDto().getDistrictId();
            userId = responseData.getGeneralVoterDto().getId();
            district.setVisibility(View.VISIBLE);
            district.setText(responseData.getGeneralVoterDto().getDistictName());


            name.setEnabled(false);
            email.setEnabled(false);
            address.setEnabled(false);
            ph_number.setEnabled(false);
            district.setEnabled(false);


        } else {
            name.setText("");
            email.setText("");
            address.setText("");
            ph_number.setText("");

            name.setEnabled(true);
            email.setEnabled(true);
            address.setEnabled(true);
            ph_number.setEnabled(true);
            spinner_district.setVisibility(View.VISIBLE);
            district.setVisibility(View.GONE);

        }


    }

    @Override
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.GET_DISTRICT_SUCCESS:
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();

                    otpresponse = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (otpresponse.getStatus().equalsIgnoreCase("true")) {
                        ArrayList<String> mStringList = new ArrayList<String>();
                        for (int i = 0; i < otpresponse.getDistrictList().size(); i++) {
                            mStringList.add(otpresponse.getDistrictList().get(i).getName());
                        }
                        mStringArray = mStringList.toArray();
                        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mStringArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_district.setAdapter(adapter);
                    }
                    return true;


                case ResReqController.GET_DISTRICT_FAILED:
                    Toast.makeText(PaymentDetailActivity.this, getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public void startCartActivity() {

//        String userdetailsDTO = MySharedPreference.readString(getApplicationContext(), MySharedPreference.USER_DETAILS, "");
//        responseData = new Gson().fromJson(userdetailsDTO, ResponseDto.class);

        Intent payment_activity = new Intent(PaymentDetailActivity.this, PayConformActivity.class);
        payment_activity.putExtra("name", name.getText().toString());
        payment_activity.putExtra("email", email.getText().toString());
        payment_activity.putExtra("address", address.getText().toString());
        payment_activity.putExtra("ph_number", ph_number.getText().toString());

        payment_activity.putExtra("district_id", district_id);
        payment_activity.putExtra("user_id", userId);

        payment_activity.putExtra("subtotal", tv_sub_total.getText().toString());
        payment_activity.putExtra("shipping", tv_shipping_total.getText().toString());
        payment_activity.putExtra("total", tv_total.getText().toString());

        startActivity(payment_activity);
    }

    public void checkUserDetails() {



        if (name.getText().toString().equals(" ")) {
            Toast.makeText(PaymentDetailActivity.this, getString(R.string.snake_please_enter_the_name), Toast.LENGTH_SHORT).show();
        } else if (email.getText().toString().equals(" ")) {
            Toast.makeText(PaymentDetailActivity.this, getString(R.string.snake_please_enter_the_Email_Id), Toast.LENGTH_SHORT).show();
        } else if (address.getText().toString().equals("")) {
            Toast.makeText(PaymentDetailActivity.this, getString(R.string.snake_Please_enter_the_Address), Toast.LENGTH_SHORT).show();
        } else if (ph_number.getText().toString().equals("")) {
            Toast.makeText(PaymentDetailActivity.this, getString(R.string.snake_Please_enter_the_Mobile_Number), Toast.LENGTH_SHORT).show();
        } else {
            startCartActivity();
        }


    }
}
