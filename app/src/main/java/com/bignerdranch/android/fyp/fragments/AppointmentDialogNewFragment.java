package com.bignerdranch.android.fyp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.models.Appointment;
import com.bignerdranch.android.fyp.network.DataRetrievalUser;
import com.bignerdranch.android.fyp.utils.QueryPreferences;
import com.firebase.client.FirebaseError;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 1/8/2017.
 */

public class AppointmentDialogNewFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private static final String TAG = "AppointmentDialogNewFragment";
    public static final String EXTRA_APPOINTMENT =
            "com.bignerdranch.android.FYP.appoinment";
    public static final String EXTRA_TEACHER_ID =
            "com.bignerdranch.android.FYP.AppointmentDialogNewFragment.teacherId";

    private boolean initLocationDisplay = true; // for spinner initialization
    private boolean initTeacherDisplay = true; // for spinner initialization
    private boolean isValidTeacher = false;
    private boolean isValidLocation = false;
    private boolean isValidDate = false;
    private boolean isValidTime = false;

    private ProgressBar mProgressbar;
    private EditText mTimeEditText;
    private EditText mDateEditText;
    private Spinner mLocationSpinner;
    private Spinner mTeacherSpinner;
    private TextView mTimeText;
    private TextView mDateText;
    Appointment mAppointment;

    private DataRetrievalUser mDataRetrievalUser;
    private List<String> mTeacherNameList;
    private List<String> mTeacherIdList;

   // private ArrayList<String> mIdList;
    private Map<String,String> mTeacherInfoMap;
    private int decidedTeacherId;

    public static AppointmentDialogNewFragment newInstance() {
        AppointmentDialogNewFragment fragment = new AppointmentDialogNewFragment();
        return fragment;
    }

    private void init_appointment() {
        mAppointment = new Appointment("Progress");
        String studentId = QueryPreferences.getStoredEmail(getContext());
        mAppointment.setStudentId(studentId);

    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDataRetrievalUser = new DataRetrievalUser(getContext());
        getMap();
        init_appointment();
       LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.appointment_dialogfragment_new, null);
        //AlertDialog.Builder appointmentDialog = new AlertDialog.Builder(getActivity());
        //appointmentDialog.setView(v);
        mTimeEditText = (EditText) v.findViewById(R.id.time_picker);
        mDateEditText = (EditText) v.findViewById(R.id.date_picker);
        mLocationSpinner = (Spinner) v.findViewById(R.id.location_select);
        mTeacherSpinner = (Spinner) v.findViewById(R.id.teacher_select);
        mTimeText = (TextView) v.findViewById(R.id.timeText);
        mDateText = (TextView) v.findViewById(R.id.dateText);
        mProgressbar = (ProgressBar) v.findViewById(R.id.progressBar);
        setDateListener();
        setTimeListener();

        initLocationSpinner();
        setLocationListener();

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (isValidDate && isValidTime && isValidTeacher && isValidLocation) {


                                    checkAppointmentValidation();
                                    //Log.i("Result", "OK");
                                } else {
                                    AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance("Invalid Appointment", 4);
                                    alertDialogFragment.show(getActivity().getSupportFragmentManager(), "Invalid");
                                }
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                 //Log.i("cancel", "cancel");
                    }
                })
                .create();
    }

    private void initTeacherSpinner() {
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, (List<String>) mTeacherNameList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTeacherSpinner.setAdapter(locationAdapter);
    }

    private void initLocationSpinner() {
        ArrayAdapter<CharSequence> teacherAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.appointment_location_array, android.R.layout.simple_spinner_item);
        teacherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLocationSpinner.setAdapter(teacherAdapter);
    }

    private void setTeacherListener() {
        mTeacherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (initTeacherDisplay) {
                    initTeacherDisplay = false;
                    isValidTeacher = false;
                    return;
                } else {
                    int selection = parent.getSelectedItemPosition(); // get the number
                    isValidTeacher = selection == 0 ? false : true;
                    if (isValidTeacher) {
                        String selectTeacherName = parent.getItemAtPosition(position).toString();
                        mAppointment.setTeacherName(selectTeacherName);
                        mAppointment.setTeacherId(mTeacherIdList.get(selection - 1));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isValidTeacher = false;
                return;
            }
        });
    }

    private void setLocationListener() {
        mLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (initLocationDisplay) {
                    initLocationDisplay = false;
                    return;
                } else {
                    decidedTeacherId = parent.getSelectedItemPosition(); // get the number
                    isValidLocation = decidedTeacherId == 0 ? false : true;
                    if (isValidLocation) {
                        String selectLocation = parent.getItemAtPosition(position).toString();
                        mAppointment.setBookingLocation(selectLocation);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isValidLocation = false;
                return;
            }
        });
    }

    private void setDateListener() {
        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePicker();
            }
        });
    }

    private void setVisibleElement() {
        mProgressbar.setVisibility(View.GONE);
        mTimeEditText.setVisibility(View.VISIBLE);
        mDateEditText.setVisibility(View.VISIBLE);
        mLocationSpinner.setVisibility(View.VISIBLE);
        mTeacherSpinner.setVisibility(View.VISIBLE);
        mDateText.setVisibility(View.VISIBLE);
        mTimeText.setVisibility(View.VISIBLE);
    }

    private void initDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AppointmentDialogNewFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setMinDate(findMinDate());
        dpd.setMaxDate(findMaxDate());
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    private Calendar findMinDate() {
        int year;
        int month;
        int day;
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Calendar minDate = Calendar.getInstance();
        year = minDate.get(Calendar.YEAR);
        month = minDate.get(Calendar.MONTH) + 1;
        day = minDate.get(Calendar.DAY_OF_MONTH) + 1;
        String parseDate = Integer.toString(month)+'/'+Integer.toString(day)+"/"+Integer.toString(year);
        try {
            Date date = formatter.parse(parseDate);
            minDate.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return minDate;
    }

    private Calendar findMaxDate() {
        int year;
        int month;
        int day;
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Calendar maxDate = Calendar.getInstance();
        year = maxDate.get(Calendar.YEAR);
        month = maxDate.get(Calendar.MONTH) + 1;
        day = maxDate.get(Calendar.DAY_OF_MONTH) + 6;
        String parseDate = Integer.toString(month)+'/'+Integer.toString(day)+"/"+Integer.toString(year);
        try {
            Date date = formatter.parse(parseDate);
            maxDate.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return maxDate;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = +dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        mDateEditText.setText(date);
        mAppointment.setBookingDate(date);
        isValidDate = true;
    }

    private void setTimeListener() {
        mTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTimePicker();
            }
        });
    }

    private void initTimePicker() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                AppointmentDialogNewFragment.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true);
        Timepoint minTime = new Timepoint(8);
        Timepoint maxTime = new Timepoint(16);
        tpd.setMinTime(minTime);
        tpd.setMaxTime(maxTime);
        tpd.setTimeInterval(1,30);
        tpd.show(getActivity().getFragmentManager(), "TimePicker");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = hourString+":"+minuteString;
        mTimeEditText.setText(time);
        mAppointment.setBookingTime(time);
        isValidTime = true;
    }

    private void sendResult(int resultCode, Appointment appointment) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_APPOINTMENT, appointment);
        getTargetFragment().
                onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private void getMap() {
        mDataRetrievalUser.getTeacherNameList(new DataRetrievalUser.OnGetTeacherListListener() {
            @Override
            public void onStart() {
                //customProgressDialog.show();
            }

            @Override
            public void onSuccess(Map<String,String> teacherInfo) {
                mTeacherInfoMap = teacherInfo;
                getTeacherEmailList();
                initTeacherSpinner();
                setTeacherListener();
                setVisibleElement();
            }

            @Override
            public void onFailed(FirebaseError firebaseError) {

            }
        });
    }

    private void checkAppointmentValidation() {
        Log.i("Dialog", "OncheckAppointment");
        mDataRetrievalUser.getAppointmentTime(mAppointment.getBookingDate(), mAppointment.getBookingTime(), mAppointment.getTeacherId(), new DataRetrievalUser.OnGetDataListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(boolean value) {
                Log.i("Dialog", "OnSuccess");
                if(value) {
                    sendResult(Activity.RESULT_OK, mAppointment);
                } else {
                   sendResult(Activity.RESULT_CANCELED, mAppointment);
                }
            }

            @Override
            public void onFailed(FirebaseError firebaseError) {

            }
        });
    }

    private void getTeacherEmailList() {
        mTeacherNameList = new ArrayList<>();
        mTeacherIdList = new ArrayList<>();
        mTeacherNameList.add("Select A Teacher");
        for(Map.Entry<String, String> entry: mTeacherInfoMap.entrySet()) {
            mTeacherIdList.add(entry.getKey());
            mTeacherNameList.add(entry.getValue());
        }
    }

}

