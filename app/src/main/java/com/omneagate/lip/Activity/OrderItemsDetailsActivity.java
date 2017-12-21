package com.omneagate.lip.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.omneagate.lip.Adaptor.OrderItemsDetailsAdapter;
import com.omneagate.lip.Model.OrderedDetailsDto;

/**
 * Created by user on 8/7/16.
 */
public class OrderItemsDetailsActivity extends BaseActivity {

    OrderedDetailsDto customerOrderDetailsDto;

    TextView order_number, order_date, ordered_items, order_amount;
    RecyclerView details_list;
    OrderItemsDetailsAdapter adapter;
    private Toolbar mToolbar;
    private TextView title;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items_details);

        setUpView();


    }

    public void setUpView() {
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String items_dto = extra.getString("order_items");
            customerOrderDetailsDto = new Gson().fromJson(items_dto, OrderedDetailsDto.class);

        }

        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.title_toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {
            title.setText(getString(R.string.title_My_Orders));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        details_list = (RecyclerView) findViewById(R.id.recycler_view_details_list);
        details_list.setLayoutManager(new LinearLayoutManager(OrderItemsDetailsActivity.this));

        adapter = new OrderItemsDetailsAdapter(this, customerOrderDetailsDto.getOrderItemList());
        details_list.setAdapter(adapter);

        order_number = (TextView) findViewById(R.id.order_number);
        order_date = (TextView) findViewById(R.id.order_date);
        ordered_items = (TextView) findViewById(R.id.ordered_items);
        order_amount = (TextView) findViewById(R.id.order_amount);

        order_number.setText(customerOrderDetailsDto.getOrderNo());
        order_date.setText(customerOrderDetailsDto.getCreatedDate());
        ordered_items.setText(customerOrderDetailsDto.getTotalQuantity());
        order_amount.setText(customerOrderDetailsDto.getTotalAmount());

    }
}
