package com.bignerdranch.android.fyp.models;

/**
 * Created by David on 3/31/2017.
 */

public class Teacher extends User{
    private int salary;

    public Teacher() {super();}

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}
