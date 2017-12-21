package com.omneagate.lip.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omneagate.lip.Adaptor.FilterReqDto;
import com.omneagate.lip.Adaptor.ProductCategoryAdaptor;
import com.omneagate.lip.Adaptor.ScaleInAnimationAdapter;
import com.omneagate.lip.Adaptor.ShoppingAdapter;
import com.omneagate.lip.Adaptor.SingleStringAdaptor;
import com.omneagate.lip.Model.CategoryListDto;
import com.omneagate.lip.Model.DiscountRAngeDto;
import com.omneagate.lip.Model.PriceRangeDto;
import com.omneagate.lip.Model.ProductDetails;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Service.OnLoadMoreListener;
import com.omneagate.lip.Service.ResReqController;
import com.omneagate.lip.Service.Utilities;
import com.omneagate.lip.Utility.MySharedPreference;
import com.omneagate.lip.Utility.NoDefaultSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ShoppingActivity extends BaseActivity implements Handler.Callback {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private ShoppingAdapter adapter;
    private String language;
    private ListView search_lv;
    private NoDefaultSpinner spinner_category;
    private SingleStringAdaptor adaptor;
    private ResponseDto responseData;
    private ProductCategoryAdaptor productCategory;
    private List<ProductDetails> productList_;
    public static List<CategoryListDto> searchCategoryList, categoryList;
    final ResReqController controller = new ResReqController(this);
    public static String categoryId, sortedType, flagCallAPI = "noactivite", categoryName;
    public static int count = 0;
    public static List<PriceRangeDto> selectedPrice;
    public static List<DiscountRAngeDto> selectedDiscount;
    public static List<String> supplierId;
    public static String catId, catName;
    private FilterReqDto filterDto;
    private int newsCount;
    private static final String TAG = ShoppingActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Application.getInstance().setGoogleTracker(TAG);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        customizeActionBar(mActionBar, mToolbar);
        language = MySharedPreference.readString(getApplicationContext(), MySharedPreference.LANGUAGE_SELECT, "");
        controller.addOutboxHandler(new Handler(this));
        newsCount = 0;

        selectedPrice = new ArrayList<>();
        selectedDiscount = new ArrayList<>();
        supplierId = new ArrayList<>();

        setupView();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getListOfProduct();
        getCategoryList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shopping, menu);

        MenuItem menuItem = menu.findItem(R.id.action_shopping_cart);
        menuItem.setIcon(buildCounterDrawable(count, R.drawable.ic_add_shopping_cart));
        return true;
    }

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.counter_menuitem_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    private void setupView() {

        productList_ = new ArrayList<>();
        categoryList = new ArrayList<>();
        searchCategoryList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.book_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        search_lv = (ListView) findViewById(R.id.search_lv);
        mSearchView = (SearchView) findViewById(R.id.book_searchView);
        spinner_category = (NoDefaultSpinner) findViewById(R.id.spinner_category);
        mSearchView.setQueryHint("Search by Categories");
        setAdapter();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 1) {
                    search_lv.setVisibility(View.VISIBLE);
                    categorySearch(newText);
                } else if (newText.length() == 0) {
                    search_lv.setVisibility(View.GONE);
                }
                return false;
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(super.getApplicationContext(),
                mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //Toast.makeText(getContext(), "CLICKED", Toast.LENGTH_LONG).show();
                /*Intent i = new Intent(ShoppingActivity.this, ProductDetailsViewFragment.class);
                String p_details = new Gson().toJson(productList_.get(position));
                i.putExtra(ConstantsKeys.PRODUCT_DETAILS, p_details);
                startActivity(i);*/
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        search_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoryName = searchCategoryList.get(position).getValue();
                categoryId = searchCategoryList.get(position).getId();
                selectedPrice.clear();
                selectedDiscount.clear();
                supplierId.clear();
//                sortedType = "";
                mSearchView.setQuery(categoryName, false);
                search_lv.setVisibility(View.GONE);
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                getSortedList();
            }
        });

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortedType = spinner_category.getSelectedItem().toString();
                if (sortedType.equalsIgnoreCase("Select")) {

                } else {
                    selectedPrice.clear();
                    selectedDiscount.clear();
                    supplierId.clear();

                    getFilterList();
//                    getSortedList();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getListOfProduct() {
        Object data = "";
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        inputParams.put("index", "0");
        controller.handleMessage(ResReqController.GETLIST_PRODUCT, inputParams, data);
    }

    private void getLoadMoreNews() {
        //String object = newsCount + "/" + userId;
        Object data = "";
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        inputParams.put("index", newsCount);
        controller.handleMessage(ResReqController.GETLIST_PRODUCT_LOADMORE, inputParams, data);
    }

    private void getCategoryList() {
        Object data = "";
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        inputParams.put("", "");
        controller.handleMessage(ResReqController.GET_CATEGORY_LIST, inputParams, data);
    }

    private void getSortedList() {
        Object data = "";
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        inputParams.put("categoryId", categoryId);
        inputParams.put("sortingType", sortedType);
        inputParams.put("index", "0");
        controller.handleMessage(ResReqController.GETLIST_PRODUCT, inputParams, data);
    }

    public void addToCart(String p_id, String type) {
        String userdetailsDTO = MySharedPreference.readString(getApplicationContext(), MySharedPreference.USER_DETAILS, "");

        responseData = new Gson().fromJson(userdetailsDTO, ResponseDto.class);
        Object data = "";
        HashMap<String, Object> inputParams = Utilities.getInstance().getRequestParams();
        inputParams.put("generalVoterId", responseData.getGeneralVoterDto().getId());
        inputParams.put("itemId", p_id);
        inputParams.put("itemType", type);
        inputParams.put("quantity", "1");
        controller.handleMessage(ResReqController.ADD_CART, inputParams, data);
    }


    private void setAdapter() {


    }

   /* private void setupViewpager(ViewPager mViewpager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ClothingFragment(), getResources().getString(R.string.clothing));
        adapter.addFragment(new ElectronicsFragment(), getResources().getString(R.string.electronics));
        adapter.addFragment(new FurnitureFragment(), getResources().getString(R.string.furniture));
        adapter.addFragment(new BookFragment(), getResources().getString(R.string.books));
        adapter.addFragment(new ClothingFragment(), getResources().getString(R.string.clothing));
        mViewpager.setAdapter(adapter);
    }*/

    protected void customizeActionBar(ActionBar actionBar, Toolbar toolbar) {

        actionBar.setHomeAsUpIndicator(R.drawable.arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.title_activity_shopping));
        //actionBar.setHomeAsUpIndicator(R.drawable.back);
    }


    private void addTabs() {

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shopping, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {

            case android.R.id.home: {
                categoryId = null;
                sortedType = null;
                count = 0;
                finish();
                return true;
            }
            case R.id.action_shopping_cart:
                Intent cart_intent = new Intent(ShoppingActivity.this, CartActivity.class);
                startActivity(cart_intent);
                return true;
            case R.id.action_search:
                Intent filter_intent = new Intent(ShoppingActivity.this, Filter_Activity.class);
                filter_intent.putExtra("cat_id", categoryId);
                filter_intent.putExtra("cat_name", categoryName);
                startActivity(filter_intent);
                return true;
            case R.id.action_refresh:

                selectedDiscount.clear();
                selectedPrice.clear();
                selectedPrice.clear();
                categoryId = null;
               /* filterDto = new FilterReqDto();
                filterDto.setCategoryId(categoryId);
                filterDto.setDiscountRangeDtoList(selectedDiscount);
                filterDto.setIndex("0");
                filterDto.setPriceRangeDtoList(selectedPrice);
                filterDto.setSupplierIdList(supplierId);
                filterDto.setSortingType(sortedType);*/

                getListOfProduct();
                return true;
            case R.id.action_orders_history:
                Intent order_intent = new Intent(ShoppingActivity.this, OrdersHistoryActivity.class);
                startActivity(order_intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case ResReqController.GETLIST_PRODUCT_SUCCESS:
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    Log.d("Check", "" + msg.obj.toString());
                    ResponseDto productList = gson.fromJson(msg.obj.toString(), ResponseDto.class);

                    if (productList.getStatus().equalsIgnoreCase("true")) {
                        productList_ = productList.getSearchItemDtoList();

                        adapter = new ShoppingAdapter(this, productList_, language, mRecyclerView);
                        ScaleInAnimationAdapter scaleInAdapter = new ScaleInAnimationAdapter(adapter);
                        scaleInAdapter.setFirstOnly(true);
                        scaleInAdapter.setDuration(500);
                        scaleInAdapter.setInterpolator(new OvershootInterpolator(.5f));
                        mRecyclerView.setAdapter(scaleInAdapter);

                        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                //add null , so the adapter will check view_type and show progress bar at bottom
                                productList_.add(null);
                                adapter.notifyItemInserted(productList_.size() - 1);

                                getLoadMoreNews();
                            }
                        });
                        newsCount = ++newsCount;
                    }
                    return true;
                case ResReqController.GETLIST_PRODUCT_FAILED:
                    Toast.makeText(ShoppingActivity.this, getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());

                    return true;

                case ResReqController.GET_CATEGORY_LIST_SUCCESS:
                    GsonBuilder gsonBuilder_ = new GsonBuilder();
                    Gson gson_ = gsonBuilder_.create();
                    Log.d("Check", "" + msg.obj.toString());
                    ResponseDto categoryList_ = gson_.fromJson(msg.obj.toString(), ResponseDto.class);
                    categoryList.clear();
                    if (categoryList_.getStatus().equalsIgnoreCase("true")) {
                        categoryList = categoryList_.getServiceListAndroductList();
                        searchCategoryList.clear();
                        searchCategoryList.addAll(categoryList);
                        productCategory = new ProductCategoryAdaptor(this, categoryList, language);
                        search_lv.setAdapter(productCategory);

                        adaptor = new SingleStringAdaptor(this, categoryList_.getSortList(), language);
                        spinner_category.setAdapter(adaptor);

                    }
                    return true;
                case ResReqController.GET_CATEGORY_LIST_FAILED:
                    Toast.makeText(ShoppingActivity.this, getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());

                    return true;

                case ResReqController.ADD_CART_LIST_SUCCESS:
                    GsonBuilder gsonBuilder__ = new GsonBuilder();
                    Gson gson__ = gsonBuilder__.create();
                    Log.d("Check", "" + msg.obj.toString());
                    ResponseDto addCart = gson__.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (addCart.getStatus().equalsIgnoreCase("true")) {
                        count = Integer.parseInt(addCart.getItemCount());
                        invalidateOptionsMenu();
                        Toast.makeText(ShoppingActivity.this, getString(R.string.added_into_cart), Toast.LENGTH_SHORT).show();
                    } else if (addCart.getStatus().equalsIgnoreCase("false")) {
                        Toast.makeText(ShoppingActivity.this, addCart.getDescription(), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                case ResReqController.ADD_CART_LIST_FAILED:
                    Toast.makeText(ShoppingActivity.this, getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());

                    return true;

                case ResReqController.GETLIST_PRODUCT_LOADMORE_SUCCESS:
                    GsonBuilder gsonBuilder___ = new GsonBuilder();
                    Gson gson___ = gsonBuilder___.create();
                    Log.d("Check", "" + msg.obj.toString());
                    ResponseDto product_loadmore = gson___.fromJson(msg.obj.toString(), ResponseDto.class);
                    if (product_loadmore.getStatus().equalsIgnoreCase("true")) {

                        if (product_loadmore.getSearchItemDtoList() != null
                                && product_loadmore.getSearchItemDtoList().size() > 0) {

                            //   remove progress item
                            productList_.remove(productList_.size() - 1);
                            adapter.notifyItemRemoved(productList_.size());
                            //add news data
                            productList_.addAll(product_loadmore.getSearchItemDtoList());
                            adapter.notifyItemInserted(productList_.size());
                            adapter.setLoaded();
                            adapter.notifyDataSetChanged();

                            newsCount = ++newsCount;
                        } else {
                            productList_.remove(productList_.size() - 1);
                            adapter.notifyItemRemoved(productList_.size());
                        }
                    }
                    return true;
                case ResReqController.GETLIST_PRODUCT_LOADMORE_FAILED:
                    Toast.makeText(ShoppingActivity.this, getString(R.string.toast_server_unreachable), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + msg.obj.toString());

                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void categorySearch(String categoryName) {
        Log.d("Check list size", "" + searchCategoryList.size());
        if (searchCategoryList != null) {
            searchCategoryList.clear();
            Log.d("Check list size", "" + categoryList.size());
            for (CategoryListDto selecteAgentList : categoryList) {
                if (selecteAgentList.getValue().toLowerCase(Locale.ENGLISH).contains(categoryName.toLowerCase())) {
                    searchCategoryList.add(selecteAgentList);
                }
            }
            productCategory = new ProductCategoryAdaptor(this, searchCategoryList, language);
            search_lv.setAdapter(productCategory);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        categoryId = null;
        sortedType = null;
        count = 0;
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        count = Integer.parseInt(addCart.getItemCount());
        invalidateOptionsMenu();
    }

    private void getFilterList() {

        filterDto = new FilterReqDto();
        filterDto.setCategoryId(categoryId);
        filterDto.setDiscountRangeDtoList(selectedDiscount);
        filterDto.setIndex("0");
        filterDto.setPriceRangeDtoList(selectedPrice);
        filterDto.setSupplierIdList(supplierId);
        filterDto.setSortingType(sortedType);
        String filterData = new Gson().toJson(filterDto);
        controller.handleMessage_(ResReqController.GETLIST_PRODUCT, filterData, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flagCallAPI.equalsIgnoreCase("activite") && flagCallAPI != null) {
            flagCallAPI = "noactivite";
            categoryId = catId;
//            mSearchView.setQuery(catName,false);
            getFilterList();
        }
    }
}
