package com.omneagate.lip.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Adaptor.SellerAdaptor;
import com.omneagate.lip.Service.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER1 on 04-07-2016.
 */
public class SellerList extends BaseActivity implements Handler.Callback {

    RecyclerView seller_rc;
    Button select_seller_btn;
    private SellerAdaptor sellerAdaptor;
    private String selectedLanguage;

    private static final String TAG = SellerList.class.getCanonicalName();
    private TextView title;
    private Toolbar mToolbar;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_list);
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage = languageChcek();
        viewId();
    }

    private void viewId() {
//        Filter_Activity.supplierData


        title = (TextView) findViewById(R.id.title_toolbar);
        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {
            title.setText(getString(R.string.title_seller));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Filter_Activity.supplierId.clear();
        Filter_Activity.suppliperName.clear();

        seller_rc = (RecyclerView) findViewById(R.id.seller_rc);
        select_seller_btn = (Button) findViewById(R.id.select_seller_btn);
        seller_rc.setLayoutManager(new LinearLayoutManager(SellerList.this));
        sellerAdaptor = new SellerAdaptor(this, Filter_Activity.supplierData, selectedLanguage);
        seller_rc.setAdapter(sellerAdaptor);

        ((Button) findViewById(R.id.select_seller_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
