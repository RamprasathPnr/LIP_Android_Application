package com.omneagate.lip.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.client.android.Intents;
import com.omneagate.lip.Model.AadhaarSeedingDto;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.AadhaarVerhoeffAlgorithm;
import com.omneagate.lip.Utility.MySharedPreference;
import com.omneagate.lip.Utility.Validation;
import com.omneagate.lip.Utility.XmlParsing;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by user on 22/5/16.
 */
public class RegistrationActivity extends BaseActivity
        implements View.OnClickListener, Handler.Callback {


    private String[] PROOF_OF_IDENTITY;
    private String[] GENDER;
    private TextView dob;
    private String selectedLanguage;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private MaterialBetterSpinner spinner;
    private CoordinatorLayout coordinatorLayout;
    private MaterialSpinner spinner2, spinner_district, spinner_constituency, spinner_gender;
    private ArrayAdapter adapter;
    private Object[] mStringArray, mConstitutency;
    public static ResponseDto otpresponse;
    private ResponseDto constitutency, registration_response;
    private Object distr_id, constituency_id;
    private String datetoserver;
    final ResReqController controller = new ResReqController(this);
    AadhaarSeedingDto beneMemberUpdate;
    //Edittext for Aadhar Number
    private EditText aadharNumberPartOneEdt, aadharNumberPartTwoEdt, aadharNumberPartThreeEdt;

    View view = this.getCurrentFocus();

    Context context = RegistrationActivity.this;
    private static final String TAG = RegistrationActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedLanguage = languageChcek();
        setContentView(R.layout.activity_registration);

        Application.getInstance().setGoogleTracker(TAG);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setUpView();
        controller.addOutboxHandler(new Handler(this));
        getDistrict();
        ((EditText) findViewById(R.id.id_num_edtxt)).setVisibility(View.GONE);
        ((RelativeLayout) findViewById(R.id.aadharLayout)).setVisibility(View.GONE);

        aadharNumberPartOneEdt = (EditText) findViewById(R.id.edtAadharNumberPartOne);
        aadharNumberPartTwoEdt = (EditText) findViewById(R.id.edtAadharNumberPartTwo);
        aadharNumberPartThreeEdt = (EditText) findViewById(R.id.edtAadharNumberPartThree);

        ((ImageView) findViewById(R.id.ivScan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beneMemberUpdate = new AadhaarSeedingDto();
                launchQRScanner();
            }
        });

    }

    public void setUpView() {

        if (selectedLanguage.equalsIgnoreCase("en")) {
            PROOF_OF_IDENTITY = new String[]{"Voter Id"};

            GENDER = new String[]{"MALE", "FEMALE"};
        } else if (selectedLanguage.equalsIgnoreCase("ta")) {

            PROOF_OF_IDENTITY = new String[]{"मतदाता पहचान पत्र"};

            GENDER = new String[]{"पुरुष", "महिला"};

        }

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        Button submit_btn = (Button) findViewById(R.id.btn_submit);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PROOF_OF_IDENTITY);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2 = (MaterialSpinner) findViewById(R.id.spinner_idproof);
        spinner_district = (MaterialSpinner) findViewById(R.id.spinner_district);
        spinner_constituency = (MaterialSpinner) findViewById(R.id.spinner_constituency);
        spinner2.setAdapter(adapter);

        if (selectedLanguage.equalsIgnoreCase("en")) {
            spinner2.setHint("Select ID type");
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else if (selectedLanguage.equalsIgnoreCase("ta")) {
            spinner2.setHint("आईडी प्रकार का चयन करें");
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, GENDER);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender = (MaterialSpinner) findViewById(R.id.spinner_gender);

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_gender.setAdapter(adapter);

        if (selectedLanguage.equalsIgnoreCase("en")) {
            spinner_gender.setHint("Select Gender");

        } else if (selectedLanguage.equalsIgnoreCase("ta")) {


            spinner_gender.setHint("लिंग चुनें");
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        if (selectedLanguage.equalsIgnoreCase("en")) {
            spinner_constituency.setHint("Select Location");
            spinner_district.setHint("Select District");
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else if (selectedLanguage.equalsIgnoreCase("ta")) {
            spinner_constituency.setHint("स्थान चुनें");
            spinner_district.setHint("जिले का चयन करें");
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        submit_btn.setOnClickListener(this);
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel();
            }

        };
        dob = (TextView) findViewById(R.id.dob_edtxt);
        dob.setOnClickListener(this);

        ((TextView) findViewById(R.id.mobile_edtxt)).setText(OtpActivity.mobileNumber);

        spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    distr_id = otpresponse.getDistrictList().get(position).getId();
                    getConstituency(distr_id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                ((EditText) findViewById(R.id.id_num_edtxt)).setVisibility(View.GONE);
                ((RelativeLayout) findViewById(R.id.aadharLayout)).setVisibility(View.GONE);
                aadharNumberPartOneEdt.setText("");
                aadharNumberPartThreeEdt.setText("");
                aadharNumberPartTwoEdt.setText("");
                ((EditText) findViewById(R.id.id_num_edtxt)).setText("");

                if (spinner2.getSelectedItem().toString().equalsIgnoreCase("Aadhar Card")) {
                    ((RelativeLayout) findViewById(R.id.aadharLayout)).setVisibility(View.VISIBLE);

                } else if (spinner2.getSelectedItem().toString().equalsIgnoreCase("Voter Id")) {
                    ((EditText) findViewById(R.id.id_num_edtxt)).setVisibility(View.VISIBLE);

                } else if (spinner2.getSelectedItem().toString().equalsIgnoreCase("Ration Card")) {
                    ((EditText) findViewById(R.id.id_num_edtxt)).setVisibility(View.VISIBLE);

                } else if (spinner2.getSelectedItem().toString().equalsIgnoreCase("Driving License")) {
                    ((EditText) findViewById(R.id.id_num_edtxt)).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_constituency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    constituency_id = constitutency.getConstituencyList().get(position).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ((EditText) findViewById(R.id.name_edtxt)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (((EditText) findViewById(R.id.name_edtxt)).getText().toString().length() == 50) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.name_edtxt)).getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) findViewById(R.id.email_edtxt)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (((EditText) findViewById(R.id.email_edtxt)).getText().toString().length() == 50) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.email_edtxt)).getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) findViewById(R.id.address_edtxt)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (((EditText) findViewById(R.id.address_edtxt)).getText().toString().length() == 50) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.address_edtxt)).getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_submit:

                InputMethodManager imm_ = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm_.hideSoftInputFromWindow(((EditText)
                        findViewById(R.id.name_edtxt)).getWindowToken(), 0);

                imm_.hideSoftInputFromWindow(((EditText)
                        findViewById(R.id.email_edtxt)).getWindowToken(), 0);
                imm_.hideSoftInputFromWindow(((EditText)
                        findViewById(R.id.address_edtxt)).getWindowToken(), 0);
                imm_.hideSoftInputFromWindow(((EditText)
                        findViewById(R.id.pincode_edtxt)).getWindowToken(), 0);
                imm_.hideSoftInputFromWindow(((EditText)
                        findViewById(R.id.id_num_edtxt)).getWindowToken(), 0);

                String name = ((EditText) findViewById(R.id.name_edtxt)).getText().toString();
                String email = ((EditText) findViewById(R.id.email_edtxt)).getText().toString();
                String mobile_num = ((TextView)
                        findViewById(R.id.mobile_edtxt)).getText().toString();
                String date_of_birth = dob.getText().toString();
                String address = ((EditText) findViewById(R.id.address_edtxt)).getText().toString();
                String pincode = ((EditText) findViewById(R.id.pincode_edtxt)).getText().toString();
                String id_number = ((EditText) findViewById(R.id.id_num_edtxt)).getText().toString();
                String aadharPartOne = "";
                if (aadharNumberPartOneEdt.getText().toString() != null) {
                    aadharPartOne = aadharNumberPartOneEdt.getText().toString();
                }
                String aadharPartTwo = "";
                if (aadharNumberPartTwoEdt.getText().toString() != null) {
                    aadharPartTwo = aadharNumberPartTwoEdt.getText().toString();
                }
                String aadharPartThree = "";
                if (aadharNumberPartThreeEdt.getText().toString() != null) {
                    aadharPartThree = aadharNumberPartThreeEdt.getText().toString();
                }
                String aadharNumber = aadharPartOne + aadharPartTwo + aadharPartThree;

                if (name.equals("")) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                            context.getString(R.string.snake_please_enter_the_name),
                            Snackbar.LENGTH_LONG);
                    int snakebar_color = getResources().getColor(R.color.snake_bar_color);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(snakebar_color);
                    snackbar.show();

                } else if (name.length() < 3) {

                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                            context.getString(R.string.snake_name_short),
                            Snackbar.LENGTH_LONG);
                    int snakebar_color = getResources().getColor(R.color.snake_bar_color);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(snakebar_color);
                    snackbar.show();

                } else if (email.equals("")) {

                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                            context.getString(R.string.snake_please_enter_the_Email_Id),
                            Snackbar.LENGTH_LONG);
                    int snakebar_color = getResources().getColor(R.color.snake_bar_color);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(snakebar_color);
                    snackbar.show();

                } else if (!Validation.isValidEmailAddress(email)) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                            context.getString(R.string.snake_Please_enter_valid_Email_Id),
                            Snackbar.LENGTH_LONG);
                    int snakebar_color = getResources().getColor(R.color.snake_bar_color);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(snakebar_color);
                    snackbar.show();

                } else if (mobile_num.equals("")) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                            context.getString(R.string.snake_Please_enter_the_Mobile_Number),
                            Snackbar.LENGTH_LONG);
                    int snakebar_color = getResources().getColor(R.color.snake_bar_color);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(snakebar_color);
                    snackbar.show();

                } else if (date_of_birth.equals("")) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                            context.getString(R.string.snake_Please_enter_the_Date_of_birth),
                            Snackbar.LENGTH_LONG);
                    int snakebar_color = getResources().getColor(R.color.snake_bar_color);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(snakebar_color);
                    snackbar.show();

                } else if (spinner_district.getSelectedItem().toString().
                        equalsIgnoreCase("Select District")) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                            context.getString(R.string.toast_please_choose_the_District),
                            Snackbar.LENGTH_LONG);
                    int snakebar_color = getResources().getColor(R.color.snake_bar_color);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(snakebar_color);
                    snackbar.show();

                } else if (address.equals("")) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                            context.getString(R.string.snake_Please_enter_the_Address),
                            Snackbar.LENGTH_LONG);
                    int snakebar_color = getResources().getColor(R.color.snake_bar_color);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(snakebar_color);
                    snackbar.show();

                } else if (spinner_constituency.getSelectedItem().toString().
                        equalsIgnoreCase("Select Location")) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                            context.getString(R.string.snake_Please_select_gender),
                            Snackbar.LENGTH_LONG);
                    int snakebar_color = getResources().getColor(R.color.snake_bar_color);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(snakebar_color);
                    snackbar.show();

                } else if (spinner_gender.getSelectedItem().toString().
                        equalsIgnoreCase("Select Gender")) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                            context.getString(R.string.snake_Please_select_gender),
                            Snackbar.LENGTH_LONG);
                    int snakebar_color = getResources().getColor(R.color.snake_bar_color);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(snakebar_color);
                    snackbar.show();

                } else if (pincode.equals("")) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                            context.getString(R.string.snake_Please_enter_pin_number),
                            Snackbar.LENGTH_LONG);
                    int snakebar_color = getResources().getColor(R.color.snake_bar_color);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(snakebar_color);
                    snackbar.show();

                } else if (spinner2.getSelectedItem().toString().equalsIgnoreCase("Aadhar Card")) {
                    if (aadharNumber.equals("")) {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout,
                                context.getString(R.string.snake_Please_enter_Aadhar_Id_Number),
                                Snackbar.LENGTH_LONG);
                        int snakebar_color = getResources().getColor(R.color.snake_bar_color);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(snakebar_color);
                        snackbar.show();
                    } else {
                        submitUserDetails();
                    }
                } else if (spinner2.getSelectedItem().toString().equalsIgnoreCase("Voter Id")
                        || spinner2.getSelectedItem().toString().equalsIgnoreCase("Ration Card")
                        || spinner2.getSelectedItem().toString().equalsIgnoreCase("Driving License")) {
                    if (id_number.equals("")) {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout,
                                context.getString(R.string.snake_Please_enter_the_Id_Number),
                                Snackbar.LENGTH_LONG);
                        int snakebar_color = getResources().getColor(R.color.snake_bar_color);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(snakebar_color);
                        snackbar.show();
                    } else {
                        submitUserDetails();
                    }
                }
                /*
                * Adhar Validation Future purpose
                * */
                try {
                   /* if (!aadharNumber.equals("")) {
                        if (StringUtils.isNotEmpty(aadharNumber)) {
                            if (StringUtils.isNotEmpty(aadharPartOne)) {
                                if (aadharPartOne.length() <= 4) {
                                    if (aadharPartOne.length() <= 3) {

                                        if (selectedLanguage.equalsIgnoreCase("en")) {
                                            displayToast("Enter correct Aadhar");
                                        } else if (selectedLanguage.equalsIgnoreCase("ta")) {
                                            displayToast("सही आधार संख्या दर्ज करें");
                                        }
                                        aadharNumberPartOneEdt.requestFocus();
                                        return;
                                    }
                                    if (aadharPartOne.length() == 4 && (aadharPartOne.substring(0, 1).equals("0") || aadharPartOne.substring(0, 1).equals("1"))) {
                                        //  Util.messageBar(this,new Util().unicodeToLocalLanguage(getResources().getString(R.string.enterAadharNumber)));
                                        if (selectedLanguage.equalsIgnoreCase("en")) {
                                            displayToast("Enter correct Aadhar Number");
                                        } else if (selectedLanguage.equalsIgnoreCase("ta")) {
                                            displayToast("सही आधार संख्या दर्ज करें");
                                        }
                                        displayToast("");
                                        aadharNumberPartOneEdt.requestFocus();
                                        return;
                                    }

                                }
                            }
                            if (aadharPartTwo.length() < 4) {
                                if (selectedLanguage.equalsIgnoreCase("en")) {
                                    displayToast("Enter correct Aadhar Number");
                                } else if (selectedLanguage.equalsIgnoreCase("ta")) {
                                    displayToast("सही आधार संख्या दर्ज करें");
                                }
                                aadharNumberPartTwoEdt.requestFocus();
                                return;
                            }
                            if (aadharPartThree.length() < 4) {
                                if (selectedLanguage.equalsIgnoreCase("en")) {
                                    displayToast("Enter correct Aadhar Number");
                                } else if (selectedLanguage.equalsIgnoreCase("ta")) {
                                    displayToast("सही आधार संख्या दर्ज करें");
                                }
                                aadharNumberPartThreeEdt.requestFocus();
                                return;
                            }

                            AadhaarVerhoeffAlgorithm aadhaarVerhoeffAlgorithm = new AadhaarVerhoeffAlgorithm();
                            Boolean isAadharNumber = aadhaarVerhoeffAlgorithm.validateVerhoeff(aadharNumber);
                            if (!isAadharNumber) {
                                if (selectedLanguage.equalsIgnoreCase("en")) {
                                    displayToast("Checksum Validation Failed.Enter Valid Aadhar Number");
                                } else if (selectedLanguage.equalsIgnoreCase("ta")) {
                                    displayToast("चेकसम मान्यकरण फेल्ड. वैध आधार संख्या दर्ज करें");
                                }
                                aadharNumberPartThreeEdt.requestFocus();
                                return;
                            }
                        } else {
                            if (selectedLanguage.equalsIgnoreCase("en")) {
                                displayToast("Enter correct Aadhar Number");
                            } else if (selectedLanguage.equalsIgnoreCase("ta")) {
                                displayToast("सही आधार संख्या दर्ज करें");
                            }
                            //Util.messageBar(this,new Util().unicodeToLocalLanguage(getResources().getString(R.string.entryAadharNumber)));
                            aadharNumberPartOneEdt.requestFocus();
                            return;
                        }
                    }*/


                } catch (Exception e) {
                    Log.e("Error", e.toString(), e);
                }

                break;

            case R.id.dob_edtxt:
                new DatePickerDialog(RegistrationActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;
        }
    }

    private void displayToast(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    private void launchQRScanner() {
        Log.e("AadharSeedingScanning", "QR Scan Called");
        String packageString = getApplicationContext().getPackageName();
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.setPackage(packageString);
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
    }


    /**
     * QR code response received for card
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                //  Log.e("LanguageCode",GlobalAppState.language);
                //  new Utility().changeLanguage(this, GlobalAppState.language);
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        String contents = data.getStringExtra(Intents.Scan.RESULT);
                        XmlParsing xmlParsing = new XmlParsing();
                        Log.e("EncryptedUFC", contents);
                        if (contents.contains("<PrintLetterBarcodeData")) {
                            String resultString = null;
                            StringBuilder sb = new StringBuilder(contents);
                            if ((sb.charAt(1) == '/')) {
                                sb.deleteCharAt(1);
                                resultString = sb.toString();
                            } else {
                                resultString = contents;
                            }
                            beneMemberUpdate = xmlParsing.getXmlParsingValue(resultString);
                        } else {
                            beneMemberUpdate = xmlParsing.getStringParsing(contents);
                        }
                        String aadharNum = beneMemberUpdate.getUid();
                        aadharNumberPartOneEdt.setText(aadharNum.substring(0, 4));
                        aadharNumberPartTwoEdt.setText(aadharNum.substring(4, 8));
                        aadharNumberPartThreeEdt.setText(aadharNum.substring(8, 12));
                        Log.e("aadhaarSeedingDto ", beneMemberUpdate.toString());
                    } catch (Exception e) {
                        // Utilities.messageBar(this, getString(R.string.qrCodeInvalid));

                        Toast.makeText(this, getString(R.string.qrCodeInvalid), Toast.LENGTH_SHORT).show();
                        Log.e("Exception ", "QR exception called:" + e.toString());

                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.e(RegistrationActivity.class.getSimpleName(), "Scan cancelled");
                }
                break;

            default:
                break;
        }
    }

    private void getDistrict() {
        try {
            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
            inputParams.put("", "");
            controller.handleMessage(ResReqController.GET_DISTRICT, inputParams, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getConstituency(Object d_id) {
        try {
            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
            inputParams.put("", "");
            controller.handleMessage(ResReqController.GET_CONSTITUENCY, inputParams, d_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitUserDetails() {
        try {
            HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
            inputParams.put("name", ((EditText) findViewById(R.id.name_edtxt)).getText().toString());
            inputParams.put("emailId", ((EditText) findViewById(R.id.email_edtxt)).getText().toString());
            inputParams.put("mobileNumber", ((TextView) findViewById(R.id.mobile_edtxt)).getText().toString());
            String date = datetoserver + " " + "00:00:00";
            inputParams.put("dob", date);
            inputParams.put("address", ((EditText) findViewById(R.id.address_edtxt)).getText().toString());
            inputParams.put("pinCode", ((EditText) findViewById(R.id.pincode_edtxt)).getText().toString());
            inputParams.put("gender", spinner_gender.getSelectedItem().toString());
            inputParams.put("districtId", distr_id);
            inputParams.put("constituencyId", constituency_id);
            inputParams.put("identityProofType", spinner2.getSelectedItem().toString());
            if (!((EditText) findViewById(R.id.id_num_edtxt)).getText().toString().equals("")) {
                inputParams.put("identityProofNumber", ((EditText) findViewById(R.id.id_num_edtxt)).getText().toString());
            } else {
                inputParams.put("identityProofNumber", aadharNumberPartOneEdt.getText().toString() + aadharNumberPartTwoEdt.getText().toString() + aadharNumberPartThreeEdt.getText().toString());
            }
            Log.d("JsonCheck", "" + new JSONObject(inputParams).toString());
            Log.d("Check", "" + inputParams.toString());

            controller.handleMessage(ResReqController.REGISTRATION, inputParams, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateLabel() {
        String myFormat = "MMM-dd-yyyy";
        String fmt = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat mydat = new SimpleDateFormat(fmt, Locale.US);
        datetoserver = mydat.format(myCalendar.getTime());
        dob.setText(sdf.format(myCalendar.getTime()));
        dob.setTextSize(16);
    }

    public void onBackPressed() {
        AlertDialog diaBox = AskOption();
        diaBox.show();
    }

    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("Registration is not complete")
                .setMessage("Are you sure you want go to Login page?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
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

                case ResReqController.GET_CONSTITUENCY_SUCCESS:

                    GsonBuilder gsonBuilder_ = new GsonBuilder();
                    Gson gson_ = gsonBuilder_.create();

                    constitutency = gson_.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (constitutency.getStatus().equalsIgnoreCase("true")) {
                        ArrayList<String> mStringList = new ArrayList<String>();
                        for (int i = 0; i < constitutency.getConstituencyList().size(); i++) {
                            mStringList.add(constitutency.getConstituencyList().get(i).getName());
                        }
                        mConstitutency = mStringList.toArray();
                        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mConstitutency);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_constituency.setAdapter(adapter);
                    }
                    return true;

                case ResReqController.GET_CONSTITUENCY_FAILED:
                    Toast.makeText(RegistrationActivity.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;

                case ResReqController.GET_DISTRICT_FAILED:
                    Toast.makeText(RegistrationActivity.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;

                case ResReqController.REGISTRATION_SUCCESS:
                    GsonBuilder gsonBuilder_reg = new GsonBuilder();
                    Gson gson_reg = gsonBuilder_reg.create();

                    registration_response = gson_reg.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (registration_response.getStatus().equalsIgnoreCase("true")) {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, context.getString(R.string.snake_Successfully_Registered), Snackbar.LENGTH_LONG);
                        int snakebar_color = getResources().getColor(R.color.get_started);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(snakebar_color);
                        snackbar.show();

                        Intent i = new Intent(RegistrationActivity.this, NewsActivity.class);
                        i.putExtra("login_details", msg.obj.toString());
                        MySharedPreference.writeString(getApplicationContext(), MySharedPreference.USER_DETAILS, msg.obj.toString());
                        MySharedPreference.writeBoolean(getApplicationContext(), MySharedPreference.REGISTRATION, true);
                        startActivity(i);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        finish();
                    }
                    return true;

                case ResReqController.REGISTRATION_FAILED:
                    Toast.makeText(RegistrationActivity.this, context.getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar userAge = new GregorianCalendar(year, month, day);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);
        if (minAdultAge.before(userAge)) {
//            SHOW_ERROR_MESSAGE;
        }
    }
}
