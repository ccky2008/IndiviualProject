package com.bignerdranch.android.fyp.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.fragments.AppointmentTeacherFragment;
import com.bignerdranch.android.fyp.fragments.QuestionListFragment;
import com.bignerdranch.android.fyp.fragments.RollCallCourseListFragment;
import com.bignerdranch.android.fyp.fragments.RollCallFragment;

/**
 * Created by David on 3/31/2017.
 */

public class TeacherContainerActivity extends AppCompatActivity{

    private static final String TAG = "TeacherContainerActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("X", "Container");
        retrievalData();
        setContentView(R.layout.teacher_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void retrievalData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int position = extras.getInt(TeacherMainPageActivity.key);
            fillContent(position);
        }
    }

    private void fillContent(int position) {
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (position) {
            case 0:
                fragmentClass = QuestionListFragment.class;
                break;
            case 1:
                fragmentClass = AppointmentTeacherFragment.class;
                break;
            case 2:
                fragmentClass = RollCallCourseListFragment.class;
                break;
            default:
                break;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();}

        if (fragmentClass != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fContent, fragment).commit(); }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            getSupportFragmentManager().popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }
}
