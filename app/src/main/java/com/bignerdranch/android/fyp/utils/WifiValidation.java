package com.bignerdranch.android.fyp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import javax.crypto.interfaces.PBEKey;

/**
 * Created by David on 4/6/2017.
 */

public class WifiValidation {

    private Context context;

    public WifiValidation(Context context) {
        this.context = context;
    }

    public WifiValidation() {}

    public int isAutomaticDateAndTime() {
        // 1 = yes, 0 = no
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
           return android.provider.Settings.Global.getInt(context.getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0);
        } else {
            return android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0);
        }
    }

    public int isAutomaticTimeZone() {
        // 1 = yes, 0 = no
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return android.provider.Settings.Global.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0);
        } else {
            return android.provider.Settings.System.getInt(context.getContentResolver(), Settings.System.AUTO_TIME_ZONE, 0);
        }
    }

    public String getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        String x = tz.getID();
        return x;
    }

    public boolean isWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public String getWifi(boolean isSSID) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;

        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            if (isSSID) {
                return wifiInfo.getSSID();
            } else {
               return wifiInfo.getBSSID();
            }
        }
        return null;
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }



}
