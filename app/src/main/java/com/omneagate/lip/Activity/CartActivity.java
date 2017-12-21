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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Adaptor.CartAdapter;
import com.omneagate.lip.Model.ProductDetails;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.MySharedPreference;

import java.util.HashMap;
import java.util.List;


/**
 * Created by user on 1/7/16.
 */
public class CartActivity extends BaseActivity implements Handler.Callback, View.OnClickListener {

    private String selectedLanguage;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private TextView title;
    private ResReqController controller;
    public static List<ProductDetails> cart_list_pdt;
    private ProductDetails pdt_dto;
    private String language;
    private CartAdapter cart_adapter;
    private ListView cart_list_view;
    private String userId;
    private RecyclerView recyclerView;
    private TextView sub_total;
    private TextView shipping_amt;
    private TextView total_amount;

    private float total_amount_;
    private TextView total;
    private TextView shipping_total;

    private float total_;
    private Button place_order;
    int final_Quantity;
    private String final_Cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setUpView();
        controller = new ResReqController(CartActivity.this);
        controller.addOutboxHandler(new Handler(this));
        selectedLanguage = languageChcek();
        getCartProducts();
    }

    public void setUpView() {
        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.title_toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {
            title.setText(getString(R.string.title_cart));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        language = MySharedPreference.readString(getApplicationContext(), MySharedPreference.LANGUAGE_SELECT, "");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        sub_total = (TextView) findViewById(R.id.text_view_subtotal);
        shipping_total = (TextView) findViewById(R.id.text_view_shipping_amount);
        total = (TextView) findViewById(R.id.text_view_total_amount);
        place_order = (Button) findViewById(R.id.button_place_order);
        place_order.setOnClickListener(this);
    }

    public void getCartProducts() {
        String userDetailsDTO = MySharedPreference.readString(CartActivity.this, MySharedPreference.USER_DETAILS, "");
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
                        cart_list_pdt = cartList.getSearchItemDtoList();
                        cart_adapter = new CartAdapter(this, cart_list_pdt, language);
                        recyclerView.setAdapter(cart_adapter);
                        sub_total.setText(cartList.getTotalCost());
                        String shipping_amt = shipping_total.getText().toString();
                        String sub_tt = sub_total.getText().toString();
                        total_amount_ = Float.valueOf(shipping_amt) + Float.valueOf(sub_tt);
                        total.setText("" + total_amount_);

                    }
                    return true;

                case ResReqController.GET_CART_PRODUTS_FAILED:
                    Toast.makeText(CartActivity.this, getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
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
        cart_list_pdt.get(0).setTotal(String.valueOf(total_amount_));
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

    public void finalCost(String cost) {
        final_Cost = cost;
        System.out.println("final_Cost" + final_Cost);
    }

    public void send_Qty(int qtt) {
        final_Quantity = qtt;
        System.out.println("final_Quantity" + final_Quantity);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_place_order:

                String subtotal_intent = sub_total.getText().toString();
                String Shipping_amt_intent = shipping_total.getText().toString();
                String total_intent = total.getText().toString();
                int final_quantity = final_Quantity;
                String final_cost = final_Cost;


                Intent detail_intent = new Intent(CartActivity.this, PaymentDetailActivity.class);
                detail_intent.putExtra("details_cart", new Gson().toJson(cart_list_pdt));


                detail_intent.putExtra("subtotal", subtotal_intent);
                detail_intent.putExtra("shipping", Shipping_amt_intent);
                detail_intent.putExtra("total", total_intent);
                detail_intent.putExtra("final_quantity", final_quantity);
                detail_intent.putExtra("final_cost", final_cost);
                startActivity(detail_intent);
                break;
        }
    }
}
