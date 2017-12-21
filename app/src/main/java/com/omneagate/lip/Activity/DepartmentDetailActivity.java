package com.omneagate.lip.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Model.GrievanceCategoryDto;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.MySharedPreference;

import java.util.HashMap;

/**
 * Created by user1 on 11/6/16.
 */
public class DepartmentDetailActivity extends BaseActivity implements Handler.Callback {
    String language, data;
    private ResponseDto responseData;
    final ResReqController controller = new ResReqController(this);
    private TextView title;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    TextView header, subheaders;
    EditText commands;
    Button submitfeeddback;
    String userId;
    GrievanceCategoryDto departmentdetail;
    RatingBar ratingBar;
    Context context;
    String cnmts, selectedLanguage;
    float ratting;
    private TextView commandss;
    private static final String TAG = DepartmentDetailActivity.class.getCanonicalName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.department_detail);
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage = languageChcek();
        context = this;
        controller.addOutboxHandler(new Handler(this));
        configureInitialPage();


    }

    private void configureInitialPage() {

        title = (TextView) findViewById(R.id.title_toolbar);
        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        commandss = (TextView) findViewById(R.id.commandss);




        if (selectedLanguage.equalsIgnoreCase("en")) {
            title.setText("Departments ");
        } else if (selectedLanguage.equalsIgnoreCase("ta")) {

            title.setText(" विभागों");

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
        header = (TextView) findViewById(R.id.header);
        subheaders = (TextView) findViewById(R.id.descriptions);
        commands = (EditText) findViewById(R.id.commandss);


        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, getResources().getColor(R.color.colorPrimary));

        submitfeeddback = (Button) findViewById(R.id.submit_command);
        data = getIntent().getStringExtra("department_detail");


        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        departmentdetail = gson.fromJson(data, GrievanceCategoryDto.class);





        commandss.setText(departmentdetail.getFeedBackComments());


        if (departmentdetail.getFeetBackStatus()==null) {

            ratingBar.setRating((float) 0.0);
        } else  if (departmentdetail.getFeetBackStatus().equals("BAD")) {

            ratingBar.setRating((float) 1.0);
        }else  if (departmentdetail.getFeetBackStatus().equals("AVERAGE")) {

            ratingBar.setRating((float) 2.0);
        }else  if (departmentdetail.getFeetBackStatus().equals("GOOD")) {

            ratingBar.setRating((float) 3.0);
        }else  if (departmentdetail.getFeetBackStatus().equals("VERYGOOD")) {

            ratingBar.setRating((float) 4.0);
        }else  if (departmentdetail.getFeetBackStatus().equals("EXCELLENT")) {

            ratingBar.setRating((float) 5.0);
        }


        header.setText(departmentdetail.getName());


        subheaders.setText(departmentdetail.getDescription());


        submitfeeddback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.commandss)).getWindowToken(), 0);
                submit_feedback();
            }
        });
    }


    /*public void onRead() {




        if (departmentdetail.getFeetBackStatus().equals("BAD")) {

            ratingBar.setRating((float) 1.0);
        }else if (departmentdetail.getFeetBackStatus().equals("BAD")) {

            ratingBar.setRating((float) 1.0);
        }
    }*/


    private void submit_feedback() {
        try {

            String userdetailsDTO = MySharedPreference.readString(getApplicationContext(), MySharedPreference.USER_DETAILS, "");
            responseData = new Gson().fromJson(userdetailsDTO, ResponseDto.class);
            userId = responseData.getGeneralVoterDto().getId();
            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();

            cnmts = commands.getText().toString();

            inputParams.put("departmentId", departmentdetail.getId());
            inputParams.put("generalVoterId", userId);
            inputParams.put("description", "" + commands.getText().toString());
            ratting = ratingBar.getRating();
            Log.e("ratting", "" + ratting);

            if (ratting == 0.0) {
                inputParams.put("feedbackType", "BAD");

            } else if (ratting == 1.0) {
                inputParams.put("feedbackType", "BAD");
            } else if (ratting == 2.0) {
                inputParams.put("feedbackType", "AVERAGE");
            } else if (ratting == 3.0) {
                inputParams.put("feedbackType", "GOOD");
            } else if (ratting == 4.0) {
                inputParams.put("feedbackType", "VERYGOOD");
            } else if (ratting == 5.0) {
                inputParams.put("feedbackType", "EXCELLENT");
            }

            if (ratting == 0.0) {
                Toast.makeText(DepartmentDetailActivity.this, context.getString(R.string.toast_rating), Toast.LENGTH_SHORT).show();
            } else if (commands.getText().toString().equals("")) {
                Toast.makeText(DepartmentDetailActivity.this, context.getString(R.string.toast_rating_comments), Toast.LENGTH_SHORT).show();
            } else {
                Log.e("requestdata", inputParams.toString());
                Object v_id = "";
                controller.handleMessage(ResReqController.DEPARTMENT_FEEDBACK, inputParams, v_id);
                // rate_dialog(inputParams);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.DEPARTMENT_FEEDBACK__SUCCESS:
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    ResponseDto responseDto = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (responseDto != null && responseDto.getStatus().equalsIgnoreCase("true")) {

                        Toast.makeText(DepartmentDetailActivity.this, context.getString(R.string.toast_rating_feedback), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public void rate_dialog(final HashMap<String, Object> inputParams_) {

        Button declineButton, ok_btn;
        final Dialog dialog = new Dialog(DepartmentDetailActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rating_dialog);
        dialog.show();
        TextView comment = (TextView) dialog.findViewById(R.id.rating);
        comment.setText("Rating : " + ratting);
        TextView comments = (TextView) dialog.findViewById(R.id.comments);
        comments.setText(commands.getText().toString());


        declineButton = (Button) dialog.findViewById(R.id.button_Cancel);

        ok_btn = (Button) dialog.findViewById(R.id.buttonNwOk);


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Log.e("requestdata", inputParams_.toString());
                Object v_id = "";
                controller.handleMessage(ResReqController.DEPARTMENT_FEEDBACK, inputParams_, v_id);*/
                onBackPressed();
            }
        });


        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


   /* private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("Rating")
                .setMessage("Comments : " + cnmts + "\n" + "Rating : " + ratting)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent backpage = new Intent(getApplicationContext(), DepartmentActivity.class);
        startActivity(backpage);
        finish();
    }
}
