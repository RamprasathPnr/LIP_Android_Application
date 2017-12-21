package com.omneagate.lip.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.user.lip.R;
import com.omneagate.lip.Adaptor.ProductCategoryAdaptor;
import com.omneagate.lip.Service.Application;

/**
 * Created by USER1 on 04-07-2016.
 */
public class ListofProductActivity extends BaseActivity {

    private static final String TAG = ListofProductActivity.class.getCanonicalName();
    private String selectedLanguage;
    private RecyclerView category_list;
    private ListView category_lv;
    private ProductCategoryAdaptor productCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage = languageChcek();
        category_list = (RecyclerView) findViewById(R.id.category_list);
//        ShoppingActivity.searchCategoryList;
        category_lv = (ListView) findViewById(R.id.category_lv);
        productCategory = new ProductCategoryAdaptor(this, ShoppingActivity.categoryList,selectedLanguage);
        category_lv.setAdapter(productCategory);

        category_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Filter_Activity.categoryName = ShoppingActivity.categoryList.get(position).getValue();
                Filter_Activity.categoryId = ShoppingActivity.categoryList.get(position).getId();
                Filter_Activity.falg = "yes";
                finish();
            }
        });
    }
}
