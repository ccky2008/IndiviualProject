package com.bignerdranch.android.fyp.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import java.util.Calendar;

/**
 * Created by David on 1/31/2017.
 */

public abstract class NotificationFragment extends Fragment{
    private static final String TAG = "NotificationFragment";

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mMessageReceiver, new IntentFilter("event-name"));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "canceling notification");
        }
    };
}
