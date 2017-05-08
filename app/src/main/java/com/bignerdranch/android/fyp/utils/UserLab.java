package com.bignerdranch.android.fyp.utils;

import com.bignerdranch.android.fyp.models.Course;
import com.bignerdranch.android.fyp.models.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by David on 2/16/2017.
 */

public class UserLab {

    private static UserLab userLab;
    private String userId = null;
    private String userName = null;
    private String userKey = null;
    private List<Course> Courses = null;
    private List<String> rollCall = null;
    private String mMacAddress = null;
    private LinkedHashMap<String, String> studentAnswer = null;
    private LinkedHashMap<String, Boolean> isCorrectAnswer = null;
    public static UserLab get() {
        if (userLab == null) {
            userLab = new UserLab();
        }
        return userLab;
    }

    private UserLab() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public List<Course> getCourses() {
        return Courses;
    }

    public void setCourses(List<Course> courses) {
        Courses = courses;
    }

    public List<String> getRollCall() {
        return rollCall;
    }

    public void setRollCall(List<String> rollCall) {
        this.rollCall = rollCall;
    }

    public String getMacAddress() {
        return mMacAddress;
    }

    public void setMacAddress(String mMacAddress) {
        this.mMacAddress = mMacAddress;
    }

    public LinkedHashMap<String, String> getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(LinkedHashMap<String, String> studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public LinkedHashMap<String, Boolean> getIsCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setIsCorrectAnswer(LinkedHashMap<String, Boolean> isCorrectAnswer) {
        this.isCorrectAnswer = isCorrectAnswer;
    }
}
