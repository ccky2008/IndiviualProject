package com.bignerdranch.android.fyp.utils;

import android.util.Log;

import com.google.firebase.database.PropertyName;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by David on 3/23/2017.
 */

public class GenericDate {

    private static final String TAG = "GenericDate";
    public static String getToday() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(new Date());
        return date;
    }

    public static String getTodayForRollCall() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(new Date());
        return date;
    }

    public static String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String time = dateFormat.format(new Date());
        return time;
    }

    public static String getTodayDayandTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dayAndTime = dateFormat.format(new Date());
        return  dayAndTime;
    }

    public static long getMilliSec(String x) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date d1 = null;
        try {
            d1 = format.parse(x);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d1.getTime();
    }

    public static boolean comparedDay(String startDateTime, String endDateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String todayTime = getTodayForRollCall() + " " + getTime();
        Log.i("Date", "Today " + todayTime);
        Log.i("Date", "Test day " + startDateTime);
        Log.i("Date", "Test End Date Time " + endDateTime);
        Date date1 = null;
        Date date2 = null;
        Date date3 = null;
        try {
            date1 = sdf.parse(todayTime);
            date2 = sdf.parse(startDateTime);
            date3 = sdf.parse(endDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date1.after(date2) && date1.before(date3)){
            Log.i("Date", "Date1 is after Date2");
            return true;
        } else{
            Log.i("Date", "Not test time");
            return false;
        }
    }

    public static boolean isEqualTime(String bookTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        Date book = null;
        Date date = null;
        try {
            book = sdf.parse(bookTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String addMinutesToTime(String time, int timeLimit) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d = null;
        try {
            d = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, timeLimit);
       return df.format(cal.getTime());
    }

    public static String findTwoDaysDistance(String time) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String currentTimeStr = getTodayDayandTime();
        Date postTimeDate = null;
        Date currentTimeDate = null;
        try {
            postTimeDate = format.parse(time);
            currentTimeDate = format.parse(currentTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateTime dateTime = new DateTime(postTimeDate);
        DateTime currentTime = new DateTime(currentTimeDate);
        Period period = new Period(dateTime, currentTime);
        if (period.getDays() > 0) {
            return  Integer.toString(period.getDays()) +"d";
        } else if (period.getHours() > 0) {
            return Integer.toString(period.getHours()) +"h";
        } else if (period.getMinutes() > 0) {
            return Integer.toString(period.getMinutes()) + "m";
        } else {
            return Integer.toString(period.getSeconds()) + "s";
        }
    }

    /*
    public String comparedTwoTime(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(startTime);
            date2 = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/
}
