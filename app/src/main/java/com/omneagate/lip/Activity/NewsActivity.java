package com.omneagate.lip.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.google.gson.Gson;
import com.omneagate.lip.Fragment.EventsFragment;
import com.omneagate.lip.Fragment.NewsFragment;
import com.omneagate.lip.Model.ResponseDto;
import com.omneagate.lip.Service.Application;
import com.omneagate.lip.Utility.MySharedPreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 27/5/16.
 */
public class NewsActivity extends BaseActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FrameLayout frame;
    boolean hidden = true;
    FrameLayout mRevealView;
    String selectedLanguage;
    ImageButton appointment_img_btn, grievance_img_btn, setting_img_btn;
    ImageView profile_img_btn;
    TextView tv_prf, tv_app, tv_gre, tv_set;

    private ResponseDto responseData;
    private TextView mTvUserName;
    private TextView mTvUserPhone;
    Context context = this;
    private static final String TAG = NewsActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_home);
        Application.getInstance().setGoogleTracker(TAG);
        selectedLanguage = languageChcek();
        setTitle("LIP");
        // frame=(FrameLayout)findViewById(R.id.frame);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);3
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //findViewById(R.id.fab).setOnClickListener(this);

        /*mRevealView = (FrameLayout) findViewById(R.id.reveal_items);
        profile_img_btn = (ImageView) findViewById(R.id.profile_img_btn);
        appointment_img_btn = (ImageButton) findViewById(R.id.appointment_img_btn);
        grievance_img_btn = (ImageButton) findViewById(R.id.grievance_img_btn);
        setting_img_btn = (ImageButton) findViewById(R.id.setting_img_btn);
        profile_img_btn.setOnClickListener(this);
        appointment_img_btn.setOnClickListener(this);
        grievance_img_btn.setOnClickListener(this);
        setting_img_btn.setOnClickListener(this);*/


       /* tv_prf = (TextView) findViewById(R.id.tv_prf);

        tv_app = (TextView) findViewById(R.id.tv_app);
        tv_gre = (TextView) findViewById(R.id.tv_gre);
        tv_set = (TextView) findViewById(R.id.tv_set);*/

        setSupportActionBar(toolbar);
        //mRevealView.setVisibility(View.GONE);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        View header = navigationView.getHeaderView(0);
        mTvUserName = (TextView) header.findViewById(R.id.txt_user_name);
        mTvUserPhone = (TextView) header.findViewById(R.id.txt_user_phone);

        Menu menu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);


        if (selectedLanguage.equalsIgnoreCase("ta")) {
            /*tv_prf.setText("என் சுயவிவரம்");
            tv_app.setText("நியமனம்");
            tv_gre.setText("குறை தீர்");
            tv_set.setText("அமைப்புகள");*/

            MenuItem nav_profile = menu.findItem(R.id.profile);

            MenuItem nav_grievance = menu.findItem(R.id.nav_grievance);
            MenuItem nav_appointment = menu.findItem(R.id.nav_appointment);
            MenuItem nav_survey = menu.findItem(R.id.nav_survey);
            MenuItem nav_municipal = menu.findItem(R.id.nav_municipal);
            MenuItem nav_shopping = menu.findItem(R.id.nav_shopping);
            MenuItem nav_library = menu.findItem(R.id.nav_library);
            MenuItem nav_house = menu.findItem(R.id.nav_house);
            MenuItem nav_logout = menu.findItem(R.id.nav_logout);
            MenuItem nav_policy = menu.findItem(R.id.nav_policy);

            nav_profile.setTitle("प्रोफाइल");
            nav_grievance.setTitle("शिकायत ");
            nav_appointment.setTitle("नियुक्ति");
            nav_survey.setTitle("सर्वेक्षण & Poll");
            nav_municipal.setTitle("विभागों");
            nav_policy.setTitle("शासन की नीत");
            nav_shopping.setTitle("ई- शॉपिंग");
            nav_library.setTitle("ई- लाइब्रेरी");
            nav_house.setTitle("HFA हाउस");
            nav_logout.setTitle("लोग आउट");

        }

        setupProfile();

    }

    private void setupProfile() {
        String userDetailsDTO = MySharedPreference.readString(getApplicationContext(),
                MySharedPreference.USER_DETAILS, "");

        responseData = new Gson().fromJson(userDetailsDTO, ResponseDto.class);

        mTvUserName.setText(responseData.getGeneralVoterDto().getName());
        mTvUserPhone.setText("+91 " + responseData.getGeneralVoterDto().getMobileNumber());
    }

    private void setupViewPager(ViewPager viewPager) {

        if (selectedLanguage.equalsIgnoreCase("en")) {

            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new NewsFragment(), "Live Feeds");
            adapter.addFragment(new EventsFragment(), "Events");
//            adapter.addFragment(new NewsFragment(), "Announcement");
            viewPager.setAdapter(adapter);

        } else if (selectedLanguage.equalsIgnoreCase("ta")) {

            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new NewsFragment(), "लाइव फीड्स");
            adapter.addFragment(new EventsFragment(), "स्पर्धाएँ");
//            adapter.addFragment(new NewsFragment(), "அறிவிப்பு");
            viewPager.setAdapter(adapter);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.profile:
                startProfileActivity();
                break;
            case R.id.nav_grievance:
                startGrievanceActivity();
                break;

            case R.id.nav_appointment:
                startAppointmentActivity();
                break;

            case R.id.nav_municipal:
                startMunipalActivity();
                break;

            case R.id.nav_policy:
                startPoliciesActivity();
                break;

            case R.id.nav_survey:
                startSurveyActivity();
                break;

            case R.id.nav_shopping:
                startShoppingActivity();
                break;

            case R.id.nav_library:
                Toast.makeText(NewsActivity.this, "launching soon....", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_house:
                Toast.makeText(NewsActivity.this, "launching soon....", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:


                AlertDialog diaBox = AskOption();
                diaBox.show();

                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void startShoppingActivity() {
        Intent i = new Intent(NewsActivity.this, ShoppingActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    private void startProfileActivity() {
        Intent i = new Intent(NewsActivity.this, MyProfileActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    private void startSurveyActivity() {
        Intent i = new Intent(NewsActivity.this, SurveyStartActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    private void startAppointmentActivity() {
        Intent i = new Intent(NewsActivity.this, AppointmentActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    private void startMunipalActivity() {
        Intent i = new Intent(NewsActivity.this, DepartmentActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    private void startPoliciesActivity() {
        Intent i = new Intent(NewsActivity.this, PolicesListActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }


    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to Logout ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        startLogoutActivity();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }


    private void startNewsActivity() {

        Intent i = new Intent(NewsActivity.this, NewsDetailActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }


    private void startLogoutActivity() {


        MySharedPreference.writeString(getApplicationContext(), MySharedPreference.USER_DETAILS, "");
        MySharedPreference.writeBoolean(getApplicationContext(), MySharedPreference.REGISTRATION, false);
        Intent i = new Intent(NewsActivity.this, LoginActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private void startGrievanceActivity() {

        Intent grievance = new Intent(NewsActivity.this, GrievancesActivity.class);
        startActivity(grievance);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       /* if (id == R.id.action_settings) {
            return true;
        }
        // attachment icon click event
        else*/
        /*if (id == R.id.action_attachment) {
            // finding X and Y co-ordinates
            int cx = (mRevealView.getLeft() + mRevealView.getRight());
            int cy = (mRevealView.getTop());
            // to find  radius when icon is tapped for showing layout
            int startradius = 0;
            int endradius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());
            // performing circular reveal when icon will be tapped
            Animator animator = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                animator = ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, startradius, endradius);
            }
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(400);
            //reverse animation
            // to find radius when icon is tapped again for hiding layout
            //  starting radius will be the radius or the extent to which circular reveal animation is to be shown
            int reverse_startradius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());
            //endradius will be zero
            int reverse_endradius = 0;
            // performing circular reveal for reverse animation
            Animator animate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                animate = ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, reverse_startradius, reverse_endradius);
            }
            if (hidden) {
                // to show the layout when icon is tapped
                mRevealView.setVisibility(View.VISIBLE);
                animator.start();
                hidden = false;
            } else {
                mRevealView.setVisibility(View.VISIBLE);
                // to hide layout on animation end
                animate.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mRevealView.setVisibility(View.GONE);
                        hidden = true;

                    }
                });
                animate.start();
            }
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }


    /*@Override
    public void onBackPressed() {

        finish();
        System.exit(0);

    }*/

    public void onBackPressed() {

        finish();
        // AlertDialog diaBox = AskOption();
        //diaBox.show();

    }

   /* private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("OTP is not registered")
                .setMessage("Are you sure you want Exit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }*/

}
