package com.omneagate.lip.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.lip.R;
import com.omneagate.lip.Adaptor.ViewPagerAdapter;
import com.omneagate.lip.Service.Application;

/**
 * Created by user1 on 25/5/16.
 */
public class DashboardFragment extends Fragment {
    private FragmentActivity myContext;
    private static final String TAG = DashboardFragment.class.getCanonicalName();
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Application.getInstance().setGoogleTracker(TAG);
//        configureInitialPage();
        return inflater.inflate(R.layout.home, container, false);
    }

    private void configureInitialPage() {
//        ViewPager news_viewPager = (ViewPager) getView().findViewById(R.id.new_pager);
//        setupViewPager(news_viewPager);
//        TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(news_viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(myContext.getSupportFragmentManager());
//        adapter.addFragment(new ViewConnectionFragment(), "Connection");
//        adapter.addFragment(new ViewCustomerFragment(), "Consumer");
//        adapter.addFragment(new ViewMeterFragment(), "Meter");
        viewPager.setAdapter(adapter);
    }

    /*@Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }*/
}