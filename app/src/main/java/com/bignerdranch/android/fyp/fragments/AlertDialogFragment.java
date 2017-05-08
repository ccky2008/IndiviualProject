package com.bignerdranch.android.fyp.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.models.Appointment;

/**
 * Created by David on 11/10/2016.
 */
public class AlertDialogFragment extends DialogFragment {

    public static final String ARG_TITLE = "Title";
    public static final String ARG_MESSAGE = "Message";
    private final String LOGOUT = "Logout";
    private final String ERROR = "Error";
    private final String SUBMISSION = "Submission";
    private final String TIMER = "timer";

    OnDataPassForLogout dataPasser;

    public interface OnDataPassForLogout {
        public void onDataPassForLogout(Boolean data);
    }

    public static AlertDialogFragment newInstance(String title, int message) {
        AlertDialogFragment fragmentNotification = new AlertDialogFragment();
        Bundle args  = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_MESSAGE, message);
        fragmentNotification.setArguments(args);
        return fragmentNotification;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String title = getArguments().getString(ARG_TITLE);
        int message = getArguments().getInt(ARG_MESSAGE);
        int showMessage = setMessage(message);
        int showTitle = setTitle(title);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth);
        builder.setTitle(showTitle);
        builder.setMessage(showMessage);
        if(title.equals(LOGOUT) || title.equals(SUBMISSION)) {
            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            boolean logout = true;
                            dataPasser.onDataPassForLogout(logout);
                        }
                    })
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dismiss();
                                }
                            });
        } else if (title.equals(TIMER)){
            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            boolean logout = true;
                            dataPasser.onDataPassForLogout(logout);
                        }
                    });
        } else {
            builder.setNegativeButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //dataPasser.onDataPassForLogout(true);
                        }
                    });
        }

        AlertDialog dialog = builder.create();
       return dialog;
    }

    @Override
    public void onAttach (Context ctx) {
        super.onAttach(ctx);
        Activity a;
        if (ctx instanceof Activity) {
            a = (Activity) ctx;
            dataPasser = (OnDataPassForLogout) a;
        }
    }

    private int setMessage(int message) {
        int messageNum;
        switch (message) {
            case 1:
                messageNum = R.string.error_invalid_email;
                break;
            case 2:
                messageNum = R.string.error_invalid_password;
                break;
            case 3:
                messageNum = R.string.logoutMessage;
                break;
            case 4:
                messageNum = R.string.error_invalid_appointment;
                break;
            case 5:
                messageNum = R.string.rollCallWrongUser;
                break;
            case 6:
                messageNum = R.string.invalid_date_and_time;
                break;
            case 7:
                messageNum = R.string.wrong_time_zone;
                break;
            case 8:
                messageNum = R.string.wifi_connection;
                break;
            case 10:
                messageNum = R.string.noCourseToday;
                break;
            case 11:
                messageNum = R.string.quiz_submission_warning;
                break;
            case 12:
                messageNum = R.string.quiz_time_is_up;
                break;
            case 13:
                messageNum = R.string.quiz_time_error;
                break;
            default:
                messageNum = R.string.error_message;
                break;
        }
        return messageNum;
    }

    private int setTitle(String title) {
        int titleNum = R.string.error_message;
        if (title.equals(LOGOUT)) {
            titleNum = R.string.logout;
        } else if (title.equals(SUBMISSION)) {
            titleNum = R.string.quiz_submission_title;
        }
        return titleNum;
    }
}
