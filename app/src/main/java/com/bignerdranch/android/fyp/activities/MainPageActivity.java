package com.bignerdranch.android.fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.bignerdranch.android.fyp.utils.WifiValidation;
import com.bignerdranch.android.fyp.fragments.AlertDialogFragment;
import com.bignerdranch.android.fyp.fragments.AppointmentHolderTabFragment;
import com.bignerdranch.android.fyp.fragments.QuizCourseCatalogFragment;
import com.bignerdranch.android.fyp.fragments.RollCallFragment;
import com.bignerdranch.android.fyp.network.DataRemoval;
import com.bignerdranch.android.fyp.network.DataRetrievalUser;
import com.bignerdranch.android.fyp.utils.QueryPreferences;
import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.fragments.QuestionListFragment;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by David on 11/7/2016.
 */
    public class MainPageActivity extends AppCompatActivity
          implements NavigationView.OnNavigationItemSelectedListener, AlertDialogFragment.OnDataPassForLogout{

    private static final String TAG = "MainPageActivity";
    private static final String ARG_FRAGMENT = "fragment";
    private static final String DIALOG_LOGOUT = "Logout";
    private boolean mActionIsActive;
    private FirebaseAuth mFireBaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFireBaseAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.home_activity_navagationbar_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String fragment = "Default";
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            fragment = intent.getExtras().getString(ARG_FRAGMENT);
        } else {
        }

        switch (fragment) {
            case "QuestionListFragment":
                QuestionListFragment questionListFragment = new QuestionListFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fContent, questionListFragment).commit();
                break;
            default:
               //// Log.i("Test", "Default page is shown");
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mActionIsActive) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                onBackPressed();
                mActionIsActive = false;
                return true; // consumes the back key event - ActionMode is not finished
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_logout) {
            FragmentManager manager = getSupportFragmentManager();
            AlertDialogFragment dialog = AlertDialogFragment.newInstance("Logout", 3) ;
            dialog.show(manager, DIALOG_LOGOUT);
            return true;
        }

       if (id == R.id.nav_forum) {
            // Test display of list of questions
            fragmentClass = QuestionListFragment.class;
        } else if(id == R.id.nav_appoinment){
            fragmentClass = AppointmentHolderTabFragment.class;
       } else if(id == R.id.nav_timetable) {
           fragmentClass = RollCallFragment.class;
       } else if (id == R.id.nav_quiz) {
           fragmentClass = QuizCourseCatalogFragment.class;
       } else if (id == R.id.nav_chat) {
           Intent i = new Intent(getApplicationContext(), ChatroomActivity.class);
           startActivity(i);
       }

        try {
            fragment = (Fragment) fragmentClass.newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fragmentClass != null) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fContent, fragment).commit(); }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDataPassForLogout(Boolean logout) {
        if (logout) {
            //QueryPreferences.setStoredHasLoggedIn(this, false);
            finish();
            new DataRemoval().RemoveToken();
            QueryPreferences.setIsStudent(this, false);
            mFireBaseAuth.signOut();
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
    }
}
