package com.bignerdranch.android.fyp.utils;

import android.content.Context;

import com.bignerdranch.android.fyp.models.Appointment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 2/14/2017.
 */

public class AppointmentLab {

    private final String SUCCESS_STATUS = "Success";
    private final String FAILURE_STATUS = "Failure";

    private static AppointmentLab appointmentLab;
    private List<Appointment> mAppointmentList;
    public static AppointmentLab get(Context context) {
        if (appointmentLab == null) {
            appointmentLab = new AppointmentLab(context);
        }
        return appointmentLab;
    }

    private AppointmentLab(Context context) {
        mAppointmentList = new ArrayList<>();
    }

    public List<Appointment> getAllAppointments() {
        return mAppointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        mAppointmentList = appointmentList;
    }

}
