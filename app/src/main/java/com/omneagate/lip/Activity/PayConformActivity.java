package com.omneagate.lip.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Adaptor.CartPayAdapter;
import com.omneagate.lip.Model.CartRequestDto;
import com.omneagate.lip.Model.ProductDetails;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.MySharedPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 4/7/16.
 */
public class PayConformActivity extends BaseActivity implements Handler.Callback, View.OnClickListener {
    private String selectedLanguage;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private TextView title;
    private ResReqController controller;
    private ProductDetails pdt_dto;
    private String language;
    private CartPayAdapter cart_adapter;
    private String userId;
    private RecyclerView recyclerView;
    private TextView sub_total;
    private float total_amount_;
    private TextView total;
    private List<ProductDetails> cart_list_pdt_;
    private TextView shipping_total;

    private float total_;
    private Button place_order;
    private TextView name_tv, email_tv, address_tv, ph_number_tv;
    private String name;
    private String email;
    private String address;
    private String ph_number;
    private TextView mobile_tv;
    private TextView sub_tot;
    private String sub_total_intent;
    private String shipping_amt_intent;
    private String total_intent;
    private TextView shipping_tot;
    private TextView total_tot;
    private String district_id;
    private CartRequestDto cartRequest;
    private String us_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirm);

        controller = new ResReqController(PayConformActivity.this);
        setUpView();
        controller.addOutboxHandler(new Handler(this));

//        getCartProducts();
    }

    public void setUpView() {
        selectedLanguage = languageChcek();
        cart_list_pdt_ = new ArrayList<>();
        cart_list_pdt_.addAll(CartActivity.cart_list_pdt);

        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.title_toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {
            title.setText(getString(R.string.title_Payment_Confirm));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getuserDetails();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        language = MySharedPreference.readString(getApplicationContext(), MySharedPreference.LANGUAGE_SELECT, "");

        name_tv = (TextView) findViewById(R.id.deliver_name);
        address_tv = (TextView) findViewById(R.id.deliver_address);
        mobile_tv = (TextView) findViewById(R.id.deliver_mobile_number);


        name_tv.setText(name);
        address_tv.setText(address);
        mobile_tv.setText(ph_number);

        sub_tot = (TextView) findViewById(R.id.text_view_subtotal);
        shipping_tot = (TextView) findViewById(R.id.text_view_shipping_amount);
        total_tot = (TextView) findViewById(R.id.text_view_total_amount);


        sub_tot.setText(sub_total_intent);
        shipping_tot.setText(shipping_amt_intent);
        total_tot.setText(sub_total_intent);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(PayConformActivity.this));

        cart_adapter = new CartPayAdapter(this, CartActivity.cart_list_pdt, language);
        recyclerView.setAdapter(cart_adapter);


        sub_total = (TextView) findViewById(R.id.text_view_subtotal);
        shipping_total = (TextView) findViewById(R.id.text_view_shipping_amount);
        total = (TextView) findViewById(R.id.text_view_total_amount);
        //total.setText(String.valueOf(cart_list_pdt.get(0).getQty()));

        place_order = (Button) findViewById(R.id.button_Proceed_to_pay);
        place_order.setOnClickListener(this);


    }

    public void getCartProducts() {

        String userDetailsDTO = MySharedPreference.readString(PayConformActivity.this, MySharedPreference.USER_DETAILS, "");
        ResponseDto responseData = new Gson().fromJson(userDetailsDTO, ResponseDto.class);
        userId = responseData.getGeneralVoterDto().getId();
        Object data = userId;
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        controller.handleMessage(ResReqController.GET_CART_PRODUTS, inputParams, data);
    }

    @Override
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.GET_CART_PRODUTS_SUCCESS:

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    Log.d("Check", "" + msg.obj.toString());

                    ResponseDto cartList = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (cartList.getStatus().equalsIgnoreCase("true")) {
                        cart_list_pdt_ = cartList.getSearchItemDtoList();
                        cart_adapter = new CartPayAdapter(this, cart_list_pdt_, language);
                        recyclerView.setAdapter(cart_adapter);
                        sub_total.setText(cartList.getTotalCost());

                        String shipping_amt = shipping_total.getText().toString();
                        String sub_tt = sub_total.getText().toString();
                        total_amount_ = Float.valueOf(shipping_amt) + Float.valueOf(sub_tt);
                        total.setText("" + total_amount_);

                    }
                    return true;

                case ResReqController.GET_CART_PRODUTS_FAILED:
                    Toast.makeText(PayConformActivity.this, getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;

                case ResReqController.GET_CONFIRM_SUCCESS:

                    GsonBuilder gsonBuilder_ = new GsonBuilder();
                    Gson gson_ = gsonBuilder_.create();
                    Log.d("Check", "" + msg.obj.toString());

                    ResponseDto cartList_ = gson_.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (cartList_.getStatus().equalsIgnoreCase("true")) {

                        if (cartList_.getPaymentStatus().equalsIgnoreCase("true")) {
                            Intent success_intent = new Intent(PayConformActivity.this, OrderConfirmActivity.class);
                            success_intent.putExtra("date", cartList_.getOrderDate());
                            success_intent.putExtra("ordernumber", cartList_.getOrderNo());
                            success_intent.putExtra("amount", cartList_.getTotalCost());
                            success_intent.putExtra("name", name_tv.getText().toString());
                            success_intent.putExtra("address", address_tv.getText().toString());
                            success_intent.putExtra("mobile_number", mobile_tv.getText().toString());
                            success_intent.putExtra("total_cost", cartList_.getTotalCost());
                            success_intent.putExtra("total_cost",cartList_.getTotalCost());
                            success_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(success_intent);
                        } else if (cartList_.getPaymentStatus().equalsIgnoreCase("false")) {
                            Intent success_intent = new Intent(PayConformActivity.this, OrderFailedActivity.class);
                            startActivity(success_intent);
                        }



                        /*cart_list_pdt_ = cartList.getSearchItemDtoList();
                        cart_adapter = new CartPayAdapter(this, cart_list_pdt_, language);
                        recyclerView.setAdapter(cart_adapter);
                        sub_total.setText(cartList.getTotalCost());

                        String shipping_amt = shipping_total.getText().toString();
                        String sub_tt = sub_total.getText().toString();
                        total_amount_ = Float.valueOf(shipping_amt) + Float.valueOf(sub_tt);
                        total.setText("" + total_amount_);*/

                    }
                    return true;

                case ResReqController.GET_CONFIRM_FAILED:
                    Toast.makeText(PayConformActivity.this, getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void total(String amt) {

        String amt_ = amt;
        String pre_amt = sub_total.getText().toString();

        total_ = Float.valueOf(amt_) + Float.valueOf(pre_amt);
        sub_total.setText("" + total_);


        String shipping_amt = shipping_total.getText().toString();
        float total_amount_ = Float.valueOf(shipping_amt) + Float.valueOf(total_);
        total.setText("" + total_amount_);


    }


    public void subtract_total(String amt) {

        String amt_ = amt;
        String pre_amt = sub_total.getText().toString();

        total_ = Float.valueOf(pre_amt) - Float.valueOf(amt_);
        sub_total.setText("" + total_);


        String shipping_amt = shipping_total.getText().toString();
        float total_amount_ = Float.valueOf(shipping_amt) + Float.valueOf(total_);
        total.setText("" + total_amount_);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_Proceed_to_pay:

                cartRequest = new CartRequestDto();
                cartRequest.setAddress(address);
                cartRequest.setDistrictId(district_id);
                cartRequest.setEmail(email);
                cartRequest.setGeneralVoterId(us_id);
                cartRequest.setMobile(ph_number);
                cartRequest.setName(name);
                cartRequest.setItemDtoList(CartActivity.cart_list_pdt);
                String cartconfirm = new Gson().toJson(cartRequest);
                controller.handleMessage_(ResReqController.GET_CONFIRM, cartconfirm, null);

                /*boolean success = true;

                if (success) {
                    Intent success_intent = new Intent(PayConformActivity.this, OrderConfirmActivity.class);

                    success_intent.putExtra("name",name_tv.getText().toString());
                    success_intent.putExtra("address",address_tv.getText().toString());
                    success_intent.putExtra("mobile_number",mobile_tv.getText().toString());
                    startActivity(success_intent);
                    success = false;

                } else {
                    Intent failed = new Intent(PayConformActivity.this, OrderFailedActivity.class);
                    startActivity(failed);
                    success = true;
                }*/

                break;
        }
    }

    public void getuserDetails() {

        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        email = extras.getString("email");
        address = extras.getString("address");
        ph_number = extras.getString("ph_number");
        district_id = extras.getString("district_id");
        us_id = extras.getString("user_id");


        sub_total_intent = extras.getString("subtotal");
        shipping_amt_intent = extras.getString("shipping");
        total_intent = extras.getString("total");

    }


}
