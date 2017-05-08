package com.bignerdranch.android.fyp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.bignerdranch.android.fyp.R;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Created by David on 3/23/2017.
 */

public class CustomProgressDialog extends Dialog{

    public Activity activity;
    public Dialog dialog;
    public TextView loadingText;
    public CircularProgressBar circularProgressBar;

    public CustomProgressDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress_dialog);
        loadingText = (TextView) findViewById(R.id.loading);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.diologCircularProgressBar);
    }
}
