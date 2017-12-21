package com.omneagate.lip.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.user.lip.R;

/**
 * Created by user on 6/7/16.
 */
public class OrderConfirmActivity extends BaseActivity implements View.OnClickListener {

    private TextView name, address, mobilenumber;
    private TextView order_number;
    private TextView order_date;
    private TextView home;
    private TextView amount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_confirm);


        name = (TextView) findViewById(R.id.text_view_name);
        address = (TextView) findViewById(R.id.text_view_address);
        mobilenumber = (TextView) findViewById(R.id.text_view_mobile);
        order_number = (TextView) findViewById(R.id.text_view_order_number);
        order_date = (TextView) findViewById(R.id.text_view_order_date);
        home = (TextView) findViewById(R.id.text_home);
        amount = (TextView) findViewById(R.id.text_view_amount);
        home.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        String nm = extras.getString("name");
        String ad = extras.getString("address");
        String mn = extras.getString("mobile_number");
        String date = extras.getString("date");
        String ordernumber = extras.getString("ordernumber");
        String total_cost = extras.getString("total_cost");

        name.setText(nm);
        address.setText(ad);
        mobilenumber.setText(mn);

        order_number.setText(ordernumber);
        order_date.setText(date);
        amount.setText("Your Order of RS."+ total_cost +" has been placed");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.text_home :
                Intent i=new Intent(OrderConfirmActivity.this,ShoppingActivity.class);
                startActivity(i);
                finish();
                break;

        }
    }
}
