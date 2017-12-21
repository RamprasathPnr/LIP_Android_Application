package com.omneagate.lip.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Model.DiscountRAngeDto;
import com.omneagate.lip.Model.PriceRangeDto;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Model.SupplierDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by USER1 on 04-07-2016.
 */
public class Filter_Activity extends BaseActivity implements Handler.Callback {

    private Toolbar toolbar;
    private ActionBar mActionBar;
    private String selectedLanguage;
    private TextView title;
    private Toolbar mToolbar;
    ArrayList<String> values, discountValues;
    private List<PriceRangeDto> checkBoxValues, selectedPrice;
    private List<DiscountRAngeDto> discountList, selectedDiscount;
    private LinearLayout checkbox_lay_price, checkbox_lay_discount;
    public static List<SupplierDto> supplierData;
    int isClick = 0;
    final ResReqController controller = new ResReqController(this);
    public static String categoryName, categoryId;
    public static List<String> supplierId, suppliperName;
    ResponseDto response;
    public static String falg;
    private static final String TAG = Filter_Activity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);
        falg = "no";
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage = languageChcek();
        controller.addOutboxHandler(new Handler(this));
        title = (TextView) findViewById(R.id.title_toolbar);
        values = new ArrayList<String>();
        discountValues = new ArrayList<>();
        checkBoxValues = new ArrayList<>();
        discountList = new ArrayList<>();
        selectedPrice = new ArrayList<>();
        supplierData = new ArrayList<>();
        supplierId = new ArrayList<>();
        suppliperName = new ArrayList<>();
        selectedDiscount = new ArrayList<>();
        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {
            if (selectedLanguage.equalsIgnoreCase("en")) {
                title.setText("Filter");
            } else if (selectedLanguage.equalsIgnoreCase("ta")) {
                title.setText("फ़िल्टर");

            }
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

        ((ImageView)findViewById(R.id.category_img)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Filter_Activity.this, ListofProductActivity.class);
                startActivity(i);
            }
        });

        ((ImageView)findViewById(R.id.suplier_img)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Filter_Activity.this, SellerList.class);
                startActivity(i);
            }
        });


        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            categoryId = null;
            categoryName = null;
        } else {
            categoryId = extras.getString("cat_id");
            categoryName = extras.getString("cat_name");
        }
        getFilters(categoryId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.action_tick:
//                if(selectedPrice.size()>0){
                if(categoryId != null){
                    ShoppingActivity.selectedPrice.addAll(selectedPrice);
                    ShoppingActivity.selectedDiscount.addAll(selectedDiscount);
                    ShoppingActivity.supplierId.addAll(supplierId);
                    ShoppingActivity.catId = categoryId;
                    ShoppingActivity.catName = categoryName;
                }
                ShoppingActivity.flagCallAPI = "activite";
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(falg.equalsIgnoreCase("yes")) {
            falg = "no";
            suppliperName.clear();

            if (checkbox_lay_price != null) {
                checkbox_lay_price.removeAllViews();
            }
            if (checkbox_lay_discount != null) {
                checkbox_lay_discount.removeAllViews();
            }

            getFiltersByID(categoryId);
        }

        /*if(suppliperName.size()>0){
            if(response != null) {
                addCheckBox(response.getPriceRangeList());
                addDiscount(response.getDiscountRangeList());
            }
        }*/

        if(suppliperName.size()>0){
            StringBuilder builder = new StringBuilder();

            for (String s : suppliperName) {
                builder.append(s + ",");
            }
            String supplierName = TextUtils.join(",", suppliperName);
            ((TextView)findViewById(R.id.supplier_tv)).setText(supplierName);
        }else {
            ((TextView)findViewById(R.id.supplier_tv)).setText("");
        }
        if(categoryName != null) {
            ((TextView) findViewById(R.id.category_tv)).setText(categoryName);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.GET_FILTER_SUCCESS:
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    Log.d("Check", "" + msg.obj.toString());
                    ResponseDto filterList = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                    supplierData.clear();
                    if (filterList.getStatus().equalsIgnoreCase("true")) {
                        response = filterList;
                        addCheckBox(filterList.getPriceRangeList());
                        addDiscount(filterList.getDiscountRangeList());
                        supplierData.addAll(filterList.getSupplierList());
                    }
                    return true;
                case ResReqController.GET_FILTER_FAILED:
                    Toast.makeText(Filter_Activity.this, getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void getFilters(String catid){
        Object data = "";
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        inputParams.put("categoryId", catid);
        controller.handleMessage(ResReqController.GET_FILTER, inputParams, data);
    }

    private void getFiltersByID(String catId){
        Object data = "";
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        inputParams.put("categoryId",catId);
        controller.handleMessage(ResReqController.GET_FILTER, inputParams, data);
    }


    public void addCheckBox(List<PriceRangeDto> surveyOptionss) {
        checkBoxValues.addAll(surveyOptionss);
        int Array_Count = surveyOptionss.size();
        checkbox_lay_price = (LinearLayout) findViewById(R.id.checkbox_lay_price);
        CheckBox checkBox;

        for (int i = 0; i < Array_Count; i++) {
            TableRow row = new TableRow(this);
            row.setId(i);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            checkBox = new CheckBox(this);
//            checkBox.setOnCheckedChangeListener(this);
            checkBox.setId(i);
            checkBox.setPadding(10, 15, 5, 5);
            checkBox.setTextColor(ContextCompat.getColor(this, R.color.black));
//            checkBox.setTextSize(getResources().getDimension(R.dimen.tendp));
            checkBox.setButtonDrawable(R.drawable.checkbox_button_bg);

            if (selectedLanguage.equalsIgnoreCase("ta")) {
                checkBox.setText(surveyOptionss.get(i).getLabel());
            } else {
                checkBox.setText(surveyOptionss.get(i).getLabel());
            }

            /*if (selectedLanguage.equalsIgnoreCase("en")) {
                checkBox.setText(surveyOptionss.get(i).getOptionValue());
            } else if (selectedLanguage.equalsIgnoreCase("ta")) {
                checkBox.setText(surveyOptionss.get(i).getRegionalOptionValue());
            }*/

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 2;

            row.addView(checkBox);
            checkbox_lay_price.addView(row);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isClick = 1;

                    if (isChecked) {
//                        values.add(checkBoxValues.get(buttonView.getId()).getId());
                        selectedPrice.add(checkBoxValues.get(buttonView.getId()));

                    } else {
                        for (int i = 0; i < values.size(); i++) {
                            if (selectedPrice.get(i) == checkBoxValues.get(buttonView.getId())) {
                                selectedPrice.remove(i);
                                Log.d("size2==>", "" + values.size());
                            }
                        }
                    }

                    StringBuilder builder = new StringBuilder();

                    for (String s : values) {
                        builder.append(s + "-");
                    }

                    String allIds = TextUtils.join("-", values);

                   /* answer = new SurveyRequest();
                    answer.setAnswer(builder.toString());
                    answer.setGeneralVoterId(String.valueOf(generalVoterDto.getId()));
                    answer.setSurveyQuestionId(String.valueOf(questionsOptionsDto.get(nextCount).getId()));
                    answer.setSurveyOptionId("CHECK_BOX");*/
                }
            });
        }
    }

    public void addDiscount(List<DiscountRAngeDto> surveyOptionss) {
        discountList.addAll(surveyOptionss);
        int Array_Count = surveyOptionss.size();
        checkbox_lay_discount = (LinearLayout) findViewById(R.id.checkbox_lay_discount);
        CheckBox checkBox;

        for (int i = 0; i < Array_Count; i++) {
            TableRow row = new TableRow(this);
            row.setId(i);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            checkBox = new CheckBox(this);
//            checkBox.setOnCheckedChangeListener(this);
            checkBox.setId(i);
            checkBox.setPadding(10, 15, 5, 5);
            checkBox.setTextColor(ContextCompat.getColor(this, R.color.black));
//            checkBox.setTextSize(getResources().getDimension(R.dimen.tendp));
            checkBox.setButtonDrawable(R.drawable.checkbox_button_bg);

            //checkBox.setText(surveyOptionss.get(i).getOptionValue());


            if (selectedLanguage.equalsIgnoreCase("ta")) {
                checkBox.setText(surveyOptionss.get(i).getLabel());
            } else {
                checkBox.setText(surveyOptionss.get(i).getLabel());
            }

            /*if (selectedLanguage.equalsIgnoreCase("en")) {
                checkBox.setText(surveyOptionss.get(i).getOptionValue());
            } else if (selectedLanguage.equalsIgnoreCase("ta")) {
                checkBox.setText(surveyOptionss.get(i).getRegionalOptionValue());
            }*/

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 2;

            row.addView(checkBox);
            checkbox_lay_discount.addView(row);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isClick = 1;

                    if (isChecked) {
                        selectedDiscount.add(discountList.get(buttonView.getId()));

                    } else {
                        for (int i = 0; i < selectedDiscount.size(); i++) {
                            if (selectedDiscount.get(i) == discountList.get(buttonView.getId())) {
                                selectedDiscount.remove(i);
                                Log.d("size2==>", "" + selectedDiscount.size());
                            }
                        }
                    }

                    StringBuilder builder = new StringBuilder();

                    for (String s : values) {
                        builder.append(s + "-");
                    }

                    String allIds = TextUtils.join("-", values);

                   /* answer = new SurveyRequest();
                    answer.setAnswer(builder.toString());
                    answer.setGeneralVoterId(String.valueOf(generalVoterDto.getId()));
                    answer.setSurveyQuestionId(String.valueOf(questionsOptionsDto.get(nextCount).getId()));
                    answer.setSurveyOptionId("CHECK_BOX");*/
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        categoryName=null;
        categoryId = null;
    }
}
