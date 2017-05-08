package com.bignerdranch.android.fyp.models;

/**
 * Created by David on 3/6/2017.
 */

public class Student extends User{
    private String classID;

    public Student() {super();}

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }
}
