package com.omneagate.lip.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by user on 21/5/16.
 */
public class OtpActivity extends Activity implements View.OnClickListener, Handler.Callback {
    Button btn_verify, btn_resend_otp;
    private BroadcastReceiver mIntentReceiver;
    private String loginResponse;
    private ResponseDto otpresponse;
    public static String mobileNumber;
    int flag;
    final ResReqController controller = new ResReqController(this);
    private static final String TAG = OtpActivity.class.getCanonicalName();
    String otp_result;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Application.getInstance().setGoogleTracker(TAG);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        btn_verify = (Button) findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(this);
        btn_resend_otp = (Button) findViewById(R.id.btn_resend_otp);
        btn_resend_otp.setOnClickListener(this);
        flag = 0;
        loginResponse = getIntent().getStringExtra("" +
                "");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        otpresponse = gson.fromJson(loginResponse, ResponseDto.class);
        controller.addOutboxHandler(new Handler(this));
        countdoun_timer();
        mobileNumber = LoginActivity.mobile_no;
    }


    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_verify:
                    if (((EditText) findViewById(R.id.otp_et)).getText().toString().length() > 3) {
                        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
                        inputParams.put("otpCode", ((EditText) findViewById(R.id.otp_et)).getText().toString());
                        inputParams.put("mobile", LoginActivity.mobile_no);
                        Object data = "";
                        controller.handleMessage(ResReqController.OTP_VERIFY, inputParams, data);
                    } else {
                        Toast.makeText(this, getString(R.string.toast_please_enter_valid_otp), Toast.LENGTH_SHORT).show();

                    }
                    break;
                case R.id.btn_resend_otp:

                    if (flag == 1) {
                        flag = 0;

                        Object data = LoginActivity.mobile_no;

                        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
                        inputParams.put("", "");
                        controller.handleMessage(ResReqController.LOGIN_NUMBER, inputParams, data);
                    } else {
                        Toast.makeText(OtpActivity.this, getString(R.string.please_wait), Toast.LENGTH_SHORT).show();
                    }

                    btn_resend_otp.setVisibility(View.INVISIBLE);
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getOTP() {
        try {
            IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
            mIntentReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        String msg = intent.getStringExtra("otp_pass");
                        otp_result = msg.replace(" ", "");
                        if (otp_result.length() > 3) {
                            ((EditText) findViewById(R.id.otp_et)).setText(otp_result);

                            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
                            inputParams.put("otpCode", ((EditText) findViewById(R.id.otp_et)).getText().toString());
                            inputParams.put("mobile", LoginActivity.mobile_no);
                            Object data = "";
                            controller.handleMessage(ResReqController.OTP_VERIFY, inputParams, data);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            this.registerReceiver(mIntentReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void countdoun_timer() {
        try {
            CountDownTimer cT = new CountDownTimer(300000, 1000) {
                public void onTick(long millisUntilFinished) {

                    String v = String.format("%02d", millisUntilFinished / 60000);
                    int va = (int) ((millisUntilFinished % 60000) / 1000);
                    ((TextView) findViewById(R.id.count_tv)).setText(v + ":" + String.format("%02d", va) + " Minutes");


                }

                public void onFinish() {
//                    timerCount.setText("00:00");
                    flag = 1;
                    ((TextView) findViewById(R.id.count_tv)).setText("00" + ":" + "00" + " : Minutes");
                    btn_resend_otp.setVisibility(View.VISIBLE);
                }
            };
            cT.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOTP();

    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mIntentReceiver);
    }

    @Override
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.OTP_VERIFY_SUCCESS:
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    ResponseDto otpresponse = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (otpresponse.getStatus().equalsIgnoreCase("true")) {
                        if (otpresponse.getLoginStatus().equalsIgnoreCase("ACTIVE")) {
                            Intent i = new Intent(OtpActivity.this, NewsActivity.class);
                            i.putExtra("login_details", msg.obj.toString());
                            MySharedPreference.writeString(getApplicationContext(), MySharedPreference.USER_DETAILS, msg.obj.toString());
                            MySharedPreference.writeBoolean(getApplicationContext(), MySharedPreference.REGISTRATION, true);
                            startActivity(i);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            finish();
                        } else {
                            Intent i = new Intent(OtpActivity.this, RegistrationActivity.class);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        Toast.makeText(OtpActivity.this, otpresponse.getCode(), Toast.LENGTH_SHORT).show();
                        if (otpresponse.getCode().equalsIgnoreCase("8014"))
                            Toast.makeText(OtpActivity.this, getString(R.string.otp_mismatch), Toast.LENGTH_SHORT).show();
                        else if(otpresponse.getCode().equalsIgnoreCase("8012"))
                            Toast.makeText(OtpActivity.this, getString(R.string.session_expired), Toast.LENGTH_SHORT).show();
                    }
                    return true;

                case ResReqController.OTP_VERIFY_FAILED:
                    Toast.makeText(OtpActivity.this, getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;

                case ResReqController.LOGIN_NUMBER_SUCCESS:
                    GsonBuilder gsonBuilder_ = new GsonBuilder();
                    Gson gson_ = gsonBuilder_.create();
                    Log.d("Check", "" + msg.obj.toString());
                    ResponseDto otpresponse_ = gson_.fromJson(msg.obj.toString(), ResponseDto.class);

                    if (otpresponse_.getStatus().equalsIgnoreCase("true")) {
                        countdoun_timer();
                        Toast.makeText(OtpActivity.this, getString(R.string.toast_otp_sent), Toast.LENGTH_SHORT).show();
                    }
                    return true;

                case ResReqController.LOGIN_NUMBER_FAILED:
                    Toast.makeText(OtpActivity.this, getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());

                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void onBackPressed() {
        AlertDialog diaBox = AskOption();
        diaBox.show();

    }

    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("OTP is not registered")
                .setMessage("Are you sure you want go to Login page ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(OtpActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }


}
