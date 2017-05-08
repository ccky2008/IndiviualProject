package com.bignerdranch.android.fyp.models;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by David on 4/3/2017.
 */

public class Course implements Serializable {

    private String id;
    private String key;
    private String name;

    @PropertyName("no_of_users")
    private long noOfStudents;

    private String day;
    private String startTime;
    private String endTime;
    private String lectuerId;
    private List<String> membership;

    @PropertyName("rollCall")
    private List<String> todayRollCallStudent;

    private long todayNumStudent;

    public Course() {
    }

    public Course(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNoOfStudents() {
        return noOfStudents;
    }

    public void setNoOfStudents(long noOfStudents) {
        this.noOfStudents = noOfStudents;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLectuerId() {
        return lectuerId;
    }

    public void setLectuerId(String lectuerId) {
        this.lectuerId = lectuerId;
    }

    public List<String> getMembership() {
        return membership;
    }

    public void setMembership(List<String> membership) {
        this.membership = membership;
    }

    public List<String> getTodayRollCallStudent() {
        return todayRollCallStudent;
    }

    public void setTodayRollCallStudent(List<String> todayRollCallStudent) {
        this.todayRollCallStudent = todayRollCallStudent;
    }

    public long getTodayNumStudent() {
        return todayNumStudent;
    }

    public void setTodayNumStudent(long todayNumStudent) {
        this.todayNumStudent = todayNumStudent;
    }
}
