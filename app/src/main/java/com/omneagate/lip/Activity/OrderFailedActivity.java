package com.omneagate.lip.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.lip.R;

/**
 * Created by user on 6/7/16.
 */
public class OrderFailedActivity extends BaseActivity  implements View.OnClickListener{

    private ImageView close;
    private TextView home;
    private Button retry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_failed);

        close=(ImageView)findViewById(R.id.image_close);
        home=(TextView) findViewById(R.id.txt_home);
        retry=(Button) findViewById(R.id.btn_retry);

        close.setOnClickListener(this);
        home.setOnClickListener(this);
        retry.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.txt_home :
                Intent i=new Intent(OrderFailedActivity.this,ShoppingActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;

            case R.id.image_close :
                finish();
                break;

            case R.id.btn_retry :
                finish();
                break;

        }
    }
}
