package com.omneagate.lip.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Model.DesignationDto;
import com.omneagate.lip.Model.GrievanceCategoryDto;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Model.UserDetailsDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.MySharedPreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by user on 25/5/16.
 */
public class AppointmentActivity extends BaseActivity implements Handler.Callback {

    Context context;
    final ResReqController controller = new ResReqController(this);
    ResponseDto response;
    public static String userId, appointmentFreeDaysDto;
    private EditText purpose_ed;
    private TextView max_length_tv, title_toolbar;
    public static String selected_date, select_time;
    public static TextView date_time_tv;
    private TextView title;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private String selectedLanguage;
    private MaterialSpinner mSpDepartments;
    private MaterialSpinner mSpDesignation;
    private MaterialSpinner mSpUser;
    private List<String> departmentNameList;
    private List<String> designationNameList;
    private List<String> userNameList;
    private List<GrievanceCategoryDto> departmentList;
    private List<DesignationDto> designationList;
    private List<UserDetailsDto> userList;

    private String departmentId = null;
    private String designationId = null;
    public static String designationUserId = null;

    private ArrayAdapter departmentAdapter;
    private ArrayAdapter designationAdapter;
    private ArrayAdapter userAdapter;
    private static final String TAG = AppointmentActivity.class.getCanonicalName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage = languageChcek();
        context = this;
        controller.addOutboxHandler(new Handler(this));
        departmentNameList = new ArrayList<>();

        purpose_ed = (EditText) findViewById(R.id.purpose_ed);
        max_length_tv = (TextView) findViewById(R.id.max_length_tv);
        date_time_tv = (TextView) findViewById(R.id.date_time_tv);


        title = (TextView) findViewById(R.id.title_toolbar);
        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {

            if (selectedLanguage.equalsIgnoreCase("en")) {
                title.setText("Appointment");
            } else if (selectedLanguage.equalsIgnoreCase("ta")) {
                title.setText("नियुक्ति");
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

        String userDetailsDTO = MySharedPreference.readString(getApplicationContext(), MySharedPreference.USER_DETAILS, "");
        response = new Gson().fromJson(userDetailsDTO, ResponseDto.class);
        userId = response.getGeneralVoterDto().getId();

        // As per sathiya request to hide hole layoutss
       /* if (selectedLanguage.equalsIgnoreCase("en")) {
            ((TextView) findViewById(R.id.mla_txt)).setText(
                    response.getGeneralVoterDto().getAdminName());
        } else if (selectedLanguage.equalsIgnoreCase("ta")) {
            ((TextView) findViewById(R.id.mla_txt)).setText(
                    response.getGeneralVoterDto().getAdminRegionalName());
        }*/

        //getAvailableDays();

        ((LinearLayout) findViewById(R.id.lay_check_availability)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isCheckButtonClick()) {
                    Intent i = new Intent(AppointmentActivity.this, AppointmentFixActivity.class);
                    i.putExtra("appointmentDetails", appointmentFreeDaysDto);
                    startActivity(i);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                } else {
                    Toast.makeText(AppointmentActivity.this, "Please select fields...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        purpose_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = purpose_ed.getText().toString().length();
                int target_count = 200;
                int count_ = target_count - length;
                max_length_tv.setText(String.valueOf(count_) + " Words left");
                if(length == target_count){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.purpose_ed)).getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((Button) findViewById(R.id.submit_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentData();
            }
        });

        ((Button) findViewById(R.id.appointment_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appointment_history = new Intent(AppointmentActivity.this, AppointmentHistory.class);
                startActivity(appointment_history);
            }
        });

        setupView();
        getAllDepartments();
    }

    private void getAllDepartments() {
        try {
            Object data = userId;
            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
            inputParams.put("", "");
            controller.handleMessage(ResReqController.GET_DEPARTMENT_LIST, inputParams, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isCheckButtonClick() {

        if (departmentId != null
                && designationId != null
                && designationUserId != null) {
            return true;
        } else {
            return false;
        }
    }

    private void setupView() {

        mSpDepartments = (MaterialSpinner) findViewById(R.id.department_spinner);
        mSpDesignation = (MaterialSpinner) findViewById(R.id.designation_spinner);
        mSpUser = (MaterialSpinner) findViewById(R.id.user_spinner);

        departmentNameList = new ArrayList<>();
        designationNameList = new ArrayList<>();
        userNameList = new ArrayList<>();

        setDepartmentSpinnerAdapter(departmentNameList);
        setDesignationSpinnerAdapter(designationNameList);
        setUserSpinnerAdapter(userNameList);

    }

    private void getAvailableDays(String designationUserId) {
        try {
            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
            Calendar mCalendar = Calendar.getInstance();
            SimpleDateFormat YearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            SimpleDateFormat MonthFormat = new SimpleDateFormat("M", Locale.ENGLISH);
            SimpleDateFormat MonthName = new SimpleDateFormat("MMM", Locale.ENGLISH);
            String Year = YearFormat.format(mCalendar.getTime());
            String month = MonthFormat.format(mCalendar.getTime());

//            Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
            inputParams.put("", "");
            Object v_id = month + "/" + Year + "/" + designationUserId;
//        Object v_id = "";
            controller.handleMessage(ResReqController.GET_AVAILABLE_DAYS, inputParams, v_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.GET_AVAILABLE_DAYS_SUCCESS:
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    ResponseDto response = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                    Log.d("Check", "" + msg.obj.toString());
                    if (response.getStatus().equalsIgnoreCase("true")) {
                        appointmentFreeDaysDto = msg.obj.toString();
                    }
                    return true;
                case ResReqController.GET_AVAILABLE_DAYS_FAILED:
                    Toast.makeText(AppointmentActivity.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;
                case ResReqController.APPOINTMENT_SUCCESS:
                    GsonBuilder gsonBuilder_ = new GsonBuilder();
                    Gson gson_ = gsonBuilder_.create();
                    ResponseDto response_ = gson_.fromJson(msg.obj.toString(), ResponseDto.class);
                    Log.d("Check", "" + msg.obj.toString());
                    if (response_.getStatus().equalsIgnoreCase("true")) {
                        Toast.makeText(context, context.getString(R.string.toast_appointment_success), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    return true;
                case ResReqController.APPOINTMENT_FAILED:
                    Toast.makeText(AppointmentActivity.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;

                case ResReqController.DESIGNATION_USER_FAILED:
                    Toast.makeText(AppointmentActivity.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;

                case ResReqController.GET_DEPARTMENT_FAILED:
                    Toast.makeText(AppointmentActivity.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;

                case ResReqController.DEPARTMENT_DESIGNATION_FAILED:
                    Toast.makeText(AppointmentActivity.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;

                case ResReqController.GET_DEPARTMENT_SUCCESS:
                    GsonBuilder builder = new GsonBuilder();
                    Gson deparmentGson = builder.create();
                    ResponseDto departmentRepose = deparmentGson.fromJson(msg.obj.toString(),
                            ResponseDto.class);
                    if (departmentRepose != null) {

                        departmentList = departmentRepose.getGrievanceCategoryList();

                        if (departmentList != null
                                && departmentList.size() > 0) {
                            setupDepartmentDetails();
                        }
                    }
                    return true;

                case ResReqController.DEPARTMENT_DESIGNATION_SUCCESS:
                    GsonBuilder designationBuilder = new GsonBuilder();
                    Gson designationGson = designationBuilder.create();
                    ResponseDto designationRepose = designationGson.fromJson(msg.obj.toString(),
                            ResponseDto.class);
                    if (designationRepose != null) {
                        designationList = designationRepose.getDesignationDtoList();
                        setupDesignationDetails();

                    }
                    return true;

                case ResReqController.DESIGNATION_USER_SUCCESS:
                    GsonBuilder userBuilder = new GsonBuilder();
                    Gson userGson = userBuilder.create();
                    ResponseDto userRepose = userGson.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (userRepose != null) {

                        userList = userRepose.getUserDtoList();
                        setupUserDetails();
                    }
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    private void setupDepartmentDetails() {

        departmentNameList.clear();
        for (GrievanceCategoryDto department : this.departmentList) {
            departmentNameList.add(department.getName());
        }
        setDepartmentSpinnerAdapter(departmentNameList);
    }


    private void setupDesignationDetails() {

        designationNameList.clear();
        userNameList.clear();
        if (designationList != null
                && designationList.size() > 0) {
            for (DesignationDto designation : designationList) {
                designationNameList.add(designation.getName());
            }
        }
        setDesignationSpinnerAdapter(designationNameList);
        setUserSpinnerAdapter(userNameList);
    }

    private void setupUserDetails() {

        userNameList.clear();
        if (userList != null
                && userList.size() > 0) {
            for (UserDetailsDto department : userList) {
                userNameList.add(department.getName());
            }
        }
        setUserSpinnerAdapter(userNameList);
    }

    private class GetDesignation extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params) {

            try {
                Object data = userId + "/" + departmentId;
                HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
                inputParams.put("", "");
                controller.handleMessage(ResReqController.DEPARTMENT_DESIGNATION, inputParams, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetDesignationUser extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params) {
            try {

                Object data = userId + "/" + departmentId + "/" + designationId;
                HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
                inputParams.put("", "");
                controller.handleMessage(ResReqController.DESIGNATION_USER, inputParams, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void appointmentData() {
        try {
            String date_time = ((TextView) findViewById(R.id.date_time_tv)).getText().toString();
            String numberOfPerson = ((EditText) findViewById(R.id.number_of_person_et)).getText().toString();
            String purpose = ((EditText) findViewById(R.id.purpose_ed)).getText().toString();

            if (date_time.length() != 0) {

                if (numberOfPerson.length() != 0) {

                    if (purpose.length() != 0) {
                        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
                        inputParams.put("appointmentDateString", date_time);
                        inputParams.put("requestBy", userId);
                        inputParams.put("reason", purpose);
                        inputParams.put("userId", designationUserId);
                        inputParams.put("numberOfPeople", numberOfPerson);
                        Object v_id = "";
                        controller.handleMessage(ResReqController.APPOINTMENT, inputParams, v_id);
                    } else {
                        Toast.makeText(context, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, context.getString(R.string.toast_purpose), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, context.getString(R.string.toast_appointment_date), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDepartmentSpinnerAdapter(List<String> departmentList) {

        departmentAdapter = new ArrayAdapter(this,
                R.layout.spinner_item, departmentList);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpDepartments.setAdapter(departmentAdapter);

        mSpDepartments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.number_of_person_et)).getWindowToken(), 0);
                imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.purpose_ed)).getWindowToken(), 0);

                if (position > -1) {
                    departmentId = AppointmentActivity.this.departmentList.get(position).getId();
                    designationNameList.clear();
                    userNameList.clear();

                    designationId = null;
                    designationUserId = null;

                    new GetDesignation().
                            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setDesignationSpinnerAdapter(List<String> designationList_) {

        designationAdapter = new ArrayAdapter(this,
                R.layout.spinner_item, designationList_);
        designationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpDesignation.setAdapter(designationAdapter);

        mSpDesignation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.number_of_person_et)).getWindowToken(), 0);
                imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.purpose_ed)).getWindowToken(), 0);

                if (position > -1) {
                    designationId = designationList.get(position).getId();
                    designationUserId = null;
                    new GetDesignationUser().
                            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setUserSpinnerAdapter(List<String> nameList) {

        userAdapter = new ArrayAdapter(this,
                R.layout.spinner_item, nameList);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpUser.setAdapter(userAdapter);

        mSpUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.number_of_person_et)).getWindowToken(), 0);
                imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.purpose_ed)).getWindowToken(), 0);
                if (position > -1) {
                    designationUserId = userList.get(position).getId();
                    getAvailableDays(designationUserId);
                    /*new GetDesignationUser().
                            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
