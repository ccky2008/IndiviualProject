package com.bignerdranch.android.fyp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.adapters.TabPagerAdapter;
import com.bignerdranch.android.fyp.utils.CustomViewPager;

/**
 * Created by David on 1/2/2017.
 */

public class AppointmentHolderTabFragment extends Fragment{

    TabLayout tabLayout;
    static PagerAdapter mPagerAdapter;
    public static AppointmentHolderTabFragment newInstance() {
        return new AppointmentHolderTabFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.appointment_fragment_main, container, false);

        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Accepted"));
        tabLayout.addTab(tabLayout.newTab().setText("Unaccepted"));

        final CustomViewPager viewPager = (CustomViewPager) v.findViewById(R.id.tab_viewpager);
        viewPager.setPagingEnabled(false);
        mPagerAdapter = new TabPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(mPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return v;
    }


}
