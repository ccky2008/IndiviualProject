package com.bignerdranch.android.fyp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.bignerdranch.android.fyp.R;

/**
 * Created by David on 11/24/2016.
 */
public class LogoutFragment extends DialogFragment{

    public static LogoutFragment newInstance() {
        LogoutFragment fragment = new LogoutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

   @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

       return new AlertDialog.Builder(getActivity(), android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth)
               .setTitle("Logout")
               .setMessage(R.string.logoutMessage)
               .setPositiveButton(R.string.logout,
                       new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               Log.i("Test","OK");
                           }
                       })
               .setNegativeButton(R.string.cancel,
                       new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               dismiss();
                           }
                       })
               .create();
   }
}
