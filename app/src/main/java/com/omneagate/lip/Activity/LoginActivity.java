package com.omneagate.lip.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.MySharedPreference;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by user on 21/5/16.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, Handler.Callback {
    Button registration_btn;
    private CoordinatorLayout coordinatorLayout;
    public static String mobile_no;
    final ResReqController controller = new ResReqController(this);
    ImageView menu_language;
    Context context;
    private Locale myLocale;
    String language;
    EditText mobile_ed;
    private LinearLayout tamil_lay, english_lay;
    private static final String TAG = LoginActivity.class.getCanonicalName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        Application.getInstance().setGoogleTracker(TAG);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        language = MySharedPreference.readString(getApplicationContext(), MySharedPreference.LANGUAGE_SELECT, "");
        setUpView();

        controller.addOutboxHandler(new Handler(this));
    }


    public void setUpView() {

        ((ImageView) findViewById(R.id.english_image)).setBackgroundResource(R.drawable.english_img_click);

        registration_btn = (Button) findViewById(R.id.registration_btn);
        // menu_language = (ImageView) findViewById(R.id.menu_language);
        //menu_language.setOnClickListener(this);
        registration_btn.setOnClickListener(this);
        tamil_lay = (LinearLayout) findViewById(R.id.tamil_lay);
        english_lay = (LinearLayout) findViewById(R.id.english_lay);
        mobile_ed = (EditText) findViewById(R.id.mobile_ed);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        mobile_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mobile_ed.getText().toString().length() > 0) {
                    if (mobile_ed.getText().toString().length() == 10) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.mobile_ed)).getWindowToken(), 0);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ((EditText) findViewById(R.id.mobile_ed)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (((EditText) findViewById(R.id.mobile_ed)).getText().toString().length() == 10) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.mobile_ed)).getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        if (language.equalsIgnoreCase("ta")) {

            ((ImageView) findViewById(R.id.english_image)).setBackgroundResource(R.drawable.english_img);
            ((ImageView) findViewById(R.id.tamil_image)).setBackgroundResource(R.drawable.tamil_img_click);
        } else if (language.equalsIgnoreCase("en")) {

            ((ImageView) findViewById(R.id.english_image)).setBackgroundResource(R.drawable.english_img_click);
            ((ImageView) findViewById(R.id.tamil_image)).setBackgroundResource(R.drawable.tamil_img);
        }


        tamil_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                english_lay.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tamil_lay.setBackgroundColor(Color.parseColor("#80FFFFFF"));
                String tamil = "ta";
                MySharedPreference.writeString(getApplicationContext(), MySharedPreference.LANGUAGE_SELECT, tamil);
                setLocale_login(tamil);
            }
        });

        english_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tamil_lay.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                english_lay.setBackgroundColor(Color.parseColor("#80FFFFFF"));
                String english = "en";
                MySharedPreference.writeString(getApplicationContext(), MySharedPreference.LANGUAGE_SELECT, english);
                setLocale_login(english);
            }
        });
        load_defaultlang();
    }

    public void load_defaultlang() {

        String language = MySharedPreference.readString(getApplicationContext(), MySharedPreference.LANGUAGE_SELECT, "");

        if (language == null || language.equals("")) {
            tamil_lay.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            english_lay.setBackgroundColor(Color.parseColor("#80FFFFFF"));
            String english = "en";
            MySharedPreference.writeString(getApplicationContext(), MySharedPreference.LANGUAGE_SELECT, english);


            myLocale = new Locale(english);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.registration_btn:


                    String language = MySharedPreference.readString(getApplicationContext(), MySharedPreference.LANGUAGE_SELECT, "");

                    if (language.equalsIgnoreCase("") || language.equalsIgnoreCase("null")) {

                        Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.snake_select_language), Snackbar.LENGTH_LONG);
                        int snakebar_color = getResources().getColor(R.color.snake_bar_color);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(snakebar_color);
                        snackbar.show();

                    } else if (((EditText) findViewById(R.id.mobile_ed)).getText().toString().equalsIgnoreCase("")) {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.snake_mobile_number), Snackbar.LENGTH_LONG);
                        int snakebar_color = getResources().getColor(R.color.snake_bar_color);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(snakebar_color);
                        snackbar.show();

                    } else if (((EditText) findViewById(R.id.mobile_ed)).getText().toString().length() > 9) {

                        if (((EditText) findViewById(R.id.mobile_ed)).getText().toString().length() == 10) {

                            mobile_no = ((EditText) findViewById(R.id.mobile_ed)).getText().toString();

                            Object data = mobile_no;

                            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
                            inputParams.put("", "");
                            controller.handleMessage(ResReqController.LOGIN_NUMBER, inputParams, data);
                        }

                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.snake_mobile_valid_number), Snackbar.LENGTH_LONG);
                        int snakebar_color = getResources().getColor(R.color.snake_bar_color);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(snakebar_color);
                        snackbar.show();
                    }
                    break;


            /*case R.id.menu_language:
                new LanguageSelectionDialog(this).show();
                break;*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.LOGIN_NUMBER_SUCCESS:
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    Log.d("Check", "" + msg.obj.toString());
                    ResponseDto otpresponse = gson.fromJson(msg.obj.toString(), ResponseDto.class);

                    if (otpresponse.getStatus().equalsIgnoreCase("true")) {
                        Intent i = new Intent(LoginActivity.this, OtpActivity.class);
                        i.putExtra("login_details", msg.obj.toString());
                        startActivity(i);

                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        finish();
                    }
                    return true;

                case ResReqController.LOGIN_NUMBER_FAILED:
                    Toast.makeText(LoginActivity.this, getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());

                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onBackPressed() {
        Intent i = new Intent(LoginActivity.this, GetStartedActivity.class);
        startActivity(i);
        finish();
    }


    public void setLocale_login(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, LoginActivity.class);
        startActivity(refresh);
        finish();
    }
}
