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
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Adaptor.OrdersAdapter;
import com.omneagate.lip.Model.OrderHistoryDto;
import com.omneagate.lip.Model.OrderedDetailsDto;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.MySharedPreference;

import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 7/7/16.
 */
public class OrdersHistoryActivity extends BaseActivity implements Handler.Callback, View.OnClickListener {

    private String sectedLanguage;
    private String userId;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private List<OrderHistoryDto> orders_history_dto;
    private RecyclerView order_recyclerView;
    private OrdersAdapter orders_adapter;
    final ResReqController controller = new ResReqController(this);
    private TextView title;
//    private OrderedDetailsDto orders_items_dto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        setUpView();
    }

    public void setUpView() {
        sectedLanguage = languageChcek();
        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.title_toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {
            title.setText(getString(R.string.title_My_Orders));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        order_recyclerView = (RecyclerView) findViewById(R.id.history_list);
        order_recyclerView.setLayoutManager(new LinearLayoutManager(OrdersHistoryActivity.this));
        controller.addOutboxHandler(new Handler(this));
        get_order_history();
    }

    public void get_order_history() {
        String userDetailsDTO = MySharedPreference.readString(OrdersHistoryActivity.this, MySharedPreference.USER_DETAILS, "");
        ResponseDto responseData = new Gson().fromJson(userDetailsDTO, ResponseDto.class);
        userId = responseData.getGeneralVoterDto().getId();
        Object data = "0/" + userId;
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        controller.handleMessage(ResReqController.GET_ORDER_HISTORY, inputParams, data);
    }

    public void get_ordered_details(String orderId) {
        Object data = orderId;
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        controller.handleMessage(ResReqController.GET_ORDER_DETAILS, inputParams, data);
    }


    @Override
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.GET_ORDER_HISTORY_SUCCESS:

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    Log.d("Check", "" + msg.obj.toString());
                    ResponseDto orders_history = gson.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (orders_history.getStatus().equalsIgnoreCase("true")) {
                        orders_history_dto = orders_history.getCustomerOrderList();
                        orders_adapter = new OrdersAdapter(this, orders_history_dto);
                        order_recyclerView.setAdapter(orders_adapter);
                    }
                    return true;
                case ResReqController.GET_ORDER_HISTORY_FAILED:
                    Toast.makeText(OrdersHistoryActivity.this, getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;

                case ResReqController.GET_ORDER_DETAILS_SUCCESS:

                    GsonBuilder gsonBuilder_ = new GsonBuilder();
                    Gson gson_ = gsonBuilder_.create();
                    Log.d("Check", "" + msg.obj.toString());
                    ResponseDto orders_details = gson_.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (orders_details.getStatus().equalsIgnoreCase("true")) {
                        String order_details=new Gson().toJson(orders_details.getCustomerOrderDetailsDto());
                        Intent order_item_details_intent = new Intent(OrdersHistoryActivity.this, OrderItemsDetailsActivity.class);
                        order_item_details_intent.putExtra("order_items",order_details);
                        startActivity(order_item_details_intent);
                    }
                    return true;
                case ResReqController.GET_ORDER_DETAILS_FAILED:
                    Toast.makeText(OrdersHistoryActivity.this, getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());
                    return true;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
