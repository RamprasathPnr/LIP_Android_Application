package com.omneagate.lip.Activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Adaptor.AppointmentFixAdapter;
import com.omneagate.lip.Model.ResponseDto;
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


public class AppointmentFixActivity extends BaseActivity implements View.OnClickListener, Handler.Callback {

    private Activity context;

    GridView mGridView;
    TextView txtDayTime;
    ImageView imgNext, imgPrevious;
    AppointmentFixAdapter mCalendaradapter;
    int calendarKey;
    public static TextView PreviousTextView;
    LinearLayout check_date, confim_lay;
    int isClick = 0;
    LinearLayout layout_name;
    public static ResponseDto daysTimesDto;
    private String date_;
    final ResReqController controller = new ResReqController(this);
    ResponseDto response, response_;
    int check_flag;
    public static List<String> position_;
    private String userId;
    private TextView title;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private MaterialSpinner spinner_time;
    private Object[] mStringArray;
    private ArrayAdapter adapter;
    private String selectedLanguage;
    String Year;
    int months;
    Calendar mCalendar;
    SimpleDateFormat MonthName;
    private static final String TAG = AppointmentFixActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage = languageChcek();

        setTitle("");

        title = (TextView) findViewById(R.id.title_toolbar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        spinner_time = (MaterialSpinner) findViewById(R.id.spinner_time);

        if (selectedLanguage.equalsIgnoreCase("en")) {
            spinner_time.setHint("Select Your Time Slot");
        } else if (selectedLanguage.equalsIgnoreCase("ta")) {

            spinner_time.setHint("अपना समय स्लॉट का चयन करें");

        }

        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {


            if (selectedLanguage.equalsIgnoreCase("en")) {
                title.setText("Schedule");
            } else if (selectedLanguage.equalsIgnoreCase("ta")) {

                title.setText("अनुसूची");

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

        context = this;

        check_flag = 0;
        String appointmentDays = getIntent().getExtras().getString("appointmentDetails");
        daysTimesDto = new Gson().fromJson(appointmentDays, ResponseDto.class);
        position_ = new ArrayList<>();
        mGridView = (GridView) findViewById(R.id.gridview);
        txtDayTime = (TextView) findViewById(R.id.text_month);
        imgNext = (ImageView) findViewById(R.id.image_next);
        imgPrevious = (ImageView) findViewById(R.id.image_back);
        imgNext.setOnClickListener(this);
        imgPrevious.setOnClickListener(this);

        confim_lay = (LinearLayout) findViewById(R.id.confim_lay);
//        check_date.setOnClickListener(this);
        confim_lay.setOnClickListener(this);

        controller.addOutboxHandler(new Handler(this));

        String userdetailsDTO = MySharedPreference.readString(getApplicationContext(), MySharedPreference.USER_DETAILS, "");
        response_ = new Gson().fromJson(userdetailsDTO, ResponseDto.class);
        userId = AppointmentActivity.designationUserId;
//        userId = response_.getGeneralVoterDto().getId();


        Calendar mCalendar = Calendar.getInstance();
        SimpleDateFormat YearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        SimpleDateFormat MonthFormat = new SimpleDateFormat("M", Locale.ENGLISH);
        SimpleDateFormat MonthName = new SimpleDateFormat("MMM", Locale.ENGLISH);
        String Year = YearFormat.format(mCalendar.getTime());
        String month = MonthFormat.format(mCalendar.getTime());
        int months = Integer.parseInt(month);
        txtDayTime.setText("" + MonthName.format(mCalendar.getTime()) + " " + Year);
        int pos__ = 33;
        mCalendaradapter = new AppointmentFixAdapter(context, months, Integer.parseInt(Year), pos__, daysTimesDto);
        mGridView.setAdapter(mCalendaradapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String check = "no";
                for (int i = 0; i < position_.size(); i++) {
                    if (position_.get(i).equalsIgnoreCase(String.valueOf(position))) {
                        check = "yes";
                        break;
                    }
                }
                if (check.equalsIgnoreCase("yes")) {
                    PreviousTextView = (TextView) view.findViewById(R.id.text_date);
                    PreviousTextView.setBackgroundResource(R.drawable.shape_circle_calendar_selected_date);
                    PreviousTextView.setTextColor(Color.parseColor("#000000"));

                    Calendar mCalendar = Calendar.getInstance();
                    SimpleDateFormat YearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
                    SimpleDateFormat MonthFormat = new SimpleDateFormat("M", Locale.ENGLISH);
                    SimpleDateFormat MonthName = new SimpleDateFormat("MMM", Locale.ENGLISH);
                    mCalendar.add(Calendar.MONTH, calendarKey);
                    String Year = YearFormat.format(mCalendar.getTime());
                    String month = MonthFormat.format(mCalendar.getTime());
                    int months = Integer.parseInt(month);
                    txtDayTime.setText("" + MonthName.format(mCalendar.getTime()) + " " + Year);
                    mCalendaradapter = new AppointmentFixAdapter(context, months, Integer.parseInt(Year), position, daysTimesDto);
                    mGridView.setAdapter(mCalendaradapter);
                    date_ = AppointmentFixAdapter.mDateString.get(position);
                    Log.d("Date sat=>", "" + AppointmentFixAdapter.mDateString.get(position));
                    AppointmentActivity.selected_date = AppointmentFixAdapter.mDateString.get(position);

                    getTiming();
                } else if (check.equalsIgnoreCase("no")) {
                    Toast.makeText(context, context.getString(R.string.toast_appointment_date_select), Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinner_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
//                    distr_id = otpresponse.getDistrictList().get(position).getId();
//                    getConstituency(distr_id);
                    String text = spinner_time.getSelectedItem().toString();
                    AppointmentActivity.select_time = text;
                    check_flag = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*((LinearLayout) findViewById(R.id.btn_lay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_name = (LinearLayout) findViewById(R.id.radiogroup_lay);
                layout_name.removeAllViews();
                getTiming();
            }
        });*/

        ((LinearLayout) findViewById(R.id.time_lay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_flag != 0) {
                    AppointmentActivity.date_time_tv.setText(AppointmentActivity.selected_date + " " + AppointmentActivity.select_time);
                    onBackPressed();
                } else {
                    Toast.makeText(context, context.getString(R.string.toast_avail_date), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_next:
                calendarKey += 1;
                getSelectedMonth();
                break;

            case R.id.image_back:
                calendarKey -= 1;
                getSelectedMonth();
                break;

//            case R.id.btn_lay:
//                check_date.setVisibility(View.GONE);
//                confim_lay.setVisibility(View.VISIBLE);
//                break;
        }
    }

    public void getSelectedMonth() {
        mCalendar = Calendar.getInstance();
        SimpleDateFormat YearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        SimpleDateFormat MonthFormat = new SimpleDateFormat("M", Locale.ENGLISH);
        MonthName = new SimpleDateFormat("MMM", Locale.ENGLISH);
        mCalendar.add(Calendar.MONTH, calendarKey);
        Year = YearFormat.format(mCalendar.getTime());
        String month = MonthFormat.format(mCalendar.getTime());
        months = Integer.parseInt(month);
        int yr = Integer.parseInt(Year);
        getAvailableDays(yr, months);


//        int pos__ = 90;
//        mCalendaradapter = new AppointmentFixAdapter(context, months, Integer.parseInt(Year), pos__, daysTimesDto);
//        mGridView.setAdapter(mCalendaradapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    public void addRadioButtons(List<String> questionOptions) {
//        public void addRadioButtons(List<OptionsDto> questionOptions) {

        layout_name = (LinearLayout) findViewById(R.id.radiogroup_lay);
        RadioButton[] rb = new RadioButton[questionOptions.size()];
        RadioGroup rg = new RadioGroup(this); //create the RadioGroup


        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
        for (int i = 0; i < questionOptions.size(); i++) {
            rb[i] = new RadioButton(this);
            rg.addView(rb[i]); //the RadioButtons are added to the radioGroup instead of the layout
//            rb[i].setLayoutParams(params);
            rb[i].setId(i);//Setting id for the RadioButton
            rb[i].setTextColor(ContextCompat.getColor(this, R.color.black));
            rb[i].setTextSize(getResources().getDimension(R.dimen.tendp));
//            rb[i].setButtonDrawable(R.drawable.radio_btn);

            rb[i].setPadding(1, 1, 1, 1);
            rb[i].setText(questionOptions.get(i));


           /* if(GlobalAppState.language.equalsIgnoreCase("en")){
                rb[i].setText(questionOptions.get(i).getOptionValue());
            }else if(GlobalAppState.language.equalsIgnoreCase("ta")){
                rb[i].setText(questionOptions.get(i).getRegionalOptionValue());
            }*/
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isClick = 1;
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton btn = (RadioButton) group.getChildAt(i);
                    if (btn.getId() == checkedId) {

                        String text = (String) btn.getText();
                        AppointmentActivity.select_time = text;
                        check_flag = 1;
                        return;
                    }
                }
            }
        });
        layout_name.addView(rg);
    }

    private void getTiming() {
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        inputParams.put("date", date_);
        inputParams.put("userId", userId);
        Object data = "";
        controller.handleMessage(ResReqController.GET_TIMING, inputParams, data);
    }

    @Override
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.GET_TIMING_SUCCESS:
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    response = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                    Log.d("Check", "" + msg.obj.toString());
                    if (response.getStatus().equalsIgnoreCase("true")) {
                        ArrayList<String> mStringList = new ArrayList<String>();
                        for (int i = 0; i < response.getAvailableDay().getStringTimeList().size(); i++) {
                            mStringList.add(response.getAvailableDay().getStringTimeList().get(i));
                        }
                        mStringArray = mStringList.toArray();
                        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mStringArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_time.setAdapter(adapter);

//                    addRadioButtons(response.getAvailableDay().getStringTimeList());
                    }
                    return true;
                case ResReqController.GET_TIMING_FAILED:
                    Toast.makeText(AppointmentFixActivity.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;

                case ResReqController.GET_AVAILABLE_DAYS_SUCCESS:
                    GsonBuilder gsonBuilder_ = new GsonBuilder();
                    Gson gson_ = gsonBuilder_.create();
                    ResponseDto response = gson_.fromJson(msg.obj.toString(), ResponseDto.class);
                    Log.d("Check", "" + msg.obj.toString());
                    if (response.getStatus().equalsIgnoreCase("true")) {
                        daysTimesDto = new Gson().fromJson(msg.obj.toString(), ResponseDto.class);
//                    mCalendaradapter.notifyDataSetChanged();

                        txtDayTime.setText("" + MonthName.format(mCalendar.getTime()) + " " + Year);
                        int pos__ = 90;
                        mCalendaradapter = new AppointmentFixAdapter(context, months, Integer.parseInt(Year), pos__, daysTimesDto);
                        mGridView.setAdapter(mCalendaradapter);
                    }
                    return true;
                case ResReqController.GET_AVAILABLE_DAYS_FAILED:
                    Toast.makeText(AppointmentFixActivity.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void getAvailableDays(int year, int month) {
        try {
            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
//        Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
            inputParams.put("", "");
            Object v_id = month + "/" + year + "/" + userId;
            controller.handleMessage(ResReqController.GET_AVAILABLE_DAYS, inputParams, v_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
