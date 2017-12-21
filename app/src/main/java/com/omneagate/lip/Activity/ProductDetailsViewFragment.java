package com.omneagate.lip.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Adaptor.ProductCategoryAdaptor;
import com.omneagate.lip.Adaptor.ScaleInAnimationAdapter;
import com.omneagate.lip.Adaptor.ShoppingAdapter;
import com.omneagate.lip.Adaptor.SingleStringAdaptor;
import com.omneagate.lip.Model.ProductDetails;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.ConstantsKeys;
import com.omneagate.lip.Utility.MySharedPreference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * Created by USER1 on 29-06-2016.
 */
public class ProductDetailsViewFragment extends BaseActivity implements Handler.Callback{

    private ImageView product_img;
    private TextView product_title_tv, descrp_tv,value_of_product_tv, seller_name_tv,tv_description;
    private Button descp_cart_btn;
    private ProductDetails p_details_;
    private Context context;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private TextView title;
    private String selectedLanguage;
    private int count = 0;
    private ResponseDto responseData;
    final ResReqController controller = new ResReqController(this);
    private static final String TAG = ProductDetailsViewFragment.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        context = this;
        Application.getInstance().setGoogleTracker(TAG);
        controller.addOutboxHandler(new Handler(this));
        selectedLanguage = languageChcek();
        title = (TextView) findViewById(R.id.title_toolbar);

        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {
            if (selectedLanguage.equalsIgnoreCase("en")) {
                title.setText("Details");
            } else if (selectedLanguage.equalsIgnoreCase("ta")) {
                title.setText("मेरी प्रोफाइल");

            }

            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String p_details = getIntent().getExtras().getString(ConstantsKeys.PRODUCT_DETAILS);
        p_details_ = new Gson().fromJson(p_details, ProductDetails.class);


        product_img = (ImageView) findViewById(R.id.product_img);
        product_title_tv = (TextView) findViewById(R.id.product_title_tv);
        tv_description = (TextView) findViewById(R.id.text_view_description);
        value_of_product_tv = (TextView) findViewById(R.id.value_of_product_tv);
        descp_cart_btn = (Button) findViewById(R.id.descp_cart_btn);
        seller_name_tv = (TextView) findViewById(R.id.seller_name_tv);

        product_title_tv.setText(p_details_.getName());
        tv_description.setText(p_details_.getDescription());
        value_of_product_tv.setText("₹"+p_details_.getCost());
        seller_name_tv.setText(p_details_.getSupplierName());

        Picasso.with(context)
                .load(p_details_.getImageUrl())
                .placeholder(R.drawable.no_available_image_150x150)
                .into(product_img);

        descp_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
    }

    public void addToCart(){
        String userdetailsDTO = MySharedPreference.readString(getApplicationContext(), MySharedPreference.USER_DETAILS, "");

        responseData = new Gson().fromJson(userdetailsDTO, ResponseDto.class);
        Object data = "";
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        inputParams.put("generalVoterId", responseData.getGeneralVoterDto().getId());
        inputParams.put("itemId", p_details_.getId());
        inputParams.put("itemType", p_details_.getType());
        inputParams.put("quantity", "1");
        controller.handleMessage(ResReqController.ADD_CART, inputParams, data);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.ADD_CART_LIST_SUCCESS:
                    GsonBuilder gsonBuilder__ = new GsonBuilder();
                    Gson gson__ = gsonBuilder__.create();
                    Log.d("Check", "" + msg.obj.toString());
                    ResponseDto addCart = gson__.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (addCart.getStatus().equalsIgnoreCase("true")) {
                        ShoppingActivity.count = Integer.parseInt(addCart.getItemCount());
                        Toast.makeText(ProductDetailsViewFragment.this, getString(R.string.added_into_cart), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    return true;
                case ResReqController.ADD_CART_LIST_FAILED:
                    Toast.makeText(ProductDetailsViewFragment.this, getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());

                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
