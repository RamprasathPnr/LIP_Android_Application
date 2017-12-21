package com.omneagate.lip.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.user.lip.R;
import com.omneagate.lip.Service.ResReqController;

/**
 * Created by USER1 on 30-06-2016.
 */
public class CartListActivity extends BaseActivity implements Handler.Callback {

    private static final String TAG = CartListActivity.class.getCanonicalName();
    final ResReqController controller = new ResReqController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        controller.addOutboxHandler(new Handler(this));
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
