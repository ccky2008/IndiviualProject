package com.bignerdranch.android.fyp.utils;

import com.bignerdranch.android.fyp.network.DataSaving;
import com.bignerdranch.android.fyp.utils.GenericDate;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by David on 3/31/2017.
 */

public class SharedMethod {

    private static final String TAG = "SharedMethod";
    public static void sendToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        new DataSaving().saveUserToken(token);
    }

    public static List<Integer> randomList(List<Integer> list) {
        long seed = System.nanoTime();
        Collections.shuffle(list, new Random(seed));
        return list;
    }

    public static List<String> findCourses(String data) {
        String firstStage = data.substring(1, data.length()-1);
        String[] stageTwo = firstStage.split(Pattern.quote(","));
        ArrayList<String> stageThree = new ArrayList<>();
        for (String s: stageTwo) {
            String[] stageThreeAux = s.split(Pattern.quote("="));
            for(String results : stageThreeAux) {
                stageThree.add(results.trim());
            }
        }
        List<String> lastStage = new ArrayList<>();
        for(int i = 0; i < stageThree.size(); i+=2) {
            lastStage.add(stageThree.get(i));
        }
        return lastStage;
    }

    public static List<String> getAttributeName(HashMap<String, String> map) {
        List<String> idList = new ArrayList<>();
        for(String x: map.keySet()) {
            idList.add(x);
        }
        return idList;
    }

    public static List<Integer> getMapValue(HashMap<String, Object> map) {
        List<Integer> list = new ArrayList<>();
        for(String x: map.keySet()) {
            list.add(((Long)map.get(x)).intValue());
        }
        return list;
    }

    public static String findDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
        String day;
        switch (dayNum) {
            case Calendar.SUNDAY:
                day = "Sunday";
                break;
            case Calendar.MONDAY:
                day = "Monday";
                break;
            case Calendar.TUESDAY:
                day = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                day = "Wednesday";
                break;
            case Calendar.THURSDAY:
                day = "Thursday";
                break;
            case Calendar.FRIDAY:
                day = "Friday";
                break;
            case Calendar.SATURDAY:
                day = "Saturday";
                break;
            default:
                day = "WrongDay";
                break;

        }
        return day;
    }

    public static int findCorrectAnswer(String position) {
        switch (position) {
            case "Zero":
                return 0;
            case "One":
                return 1;
            case "Two":
                return 2;
            case "Three":
                return 3;
            case "Four":
                return 4;
            case "Five":
                return 5;
        }
        return 6;
    }


    public static boolean isTodayCourse(String dayOfWeek, String courseDay) {
        if (courseDay.equalsIgnoreCase(dayOfWeek)) {
            return true;
        } else {
            return false;
        }
    }

    public static long compareTimeMilli(String time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String currentTimeStr = GenericDate.getTime();
        Date courseTime = null;
        Date currentTime = null;
        try {
            courseTime = format.parse(time);
            currentTime = format.parse(currentTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return courseTime.getTime() - currentTime.getTime();
    }

    public static boolean isBeforeCourseStartTime(long dif) {
        if (dif > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAfterCourseTime(long milliOfCurrentTime, long courseEndTime) {
        //Log.i(TAG, "current  " + Long.toString(milliOfCurrentTime));
        //Log.i(TAG, "course " + Long.toString(courseEndTime));
        if (milliOfCurrentTime > courseEndTime) {
            return true;
        } else {
            return false;
        }
    }

   public static String timeDistance(long millis) {
       long hours = TimeUnit.MILLISECONDS.toHours(millis);
       millis -= TimeUnit.HOURS.toMillis(hours);
       long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
       String time = "";

       if ( hours > 0 && minutes > 0) {
           time = hours + "  Hours " + minutes + " Minutes";
       } else if (hours <= 0 && minutes > 0) {
           time = minutes + " Minutes";
       } else if (hours > 0 && minutes <= 0) {
           time = hours + "  Hours ";
       }
       return time;
   }
}
