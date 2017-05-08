package com.bignerdranch.android.fyp.utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by David on 11/13/2016.
 */
public class QueryPreferences {
    private static final String PREF_LOGIN_STUDENT_ID = "loginID";
    private static final String PREF_FIRST_TIME_LOGIN = "firsttimelogin";
    private static final String PREF_SEARCH_QUERY = "searchQuery";
    private static final String PREF_GOOGLE_ACCOUNT = "Account";
    private static final String PREF_IS_STUDENT = "student";

    public static boolean getIsStudent(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_STUDENT, false);
    }

    public static void setIsStudent(Context context, boolean isStudent) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_STUDENT, isStudent)
                .apply();
    }

    public static String getStoredGoogleAccount(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_GOOGLE_ACCOUNT, null);
    }

    public static void setGoogleAccount(Context context, String googleAccount) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_GOOGLE_ACCOUNT, googleAccount)
                .apply();
    }

    public static String getStoredSearchQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY, null);
    }

    public static void setStoredSearchQuery(Context context, String searchQuery) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_QUERY, searchQuery)
                .apply();
    }

    public static String getStoredEmail (Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LOGIN_STUDENT_ID, null);
    }

    public static void setStoredEmail (Context context, String email) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LOGIN_STUDENT_ID, email)
                .apply();
    }

    public static boolean getIsFirstTimeLogin (Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_FIRST_TIME_LOGIN, false);
    }

    public static void setIsFirstTimeLogin (Context context, boolean hasLoggedIn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_FIRST_TIME_LOGIN, hasLoggedIn)
                .apply();
    }

}
