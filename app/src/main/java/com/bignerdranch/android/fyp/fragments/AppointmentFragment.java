package com.bignerdranch.android.fyp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.adapters.AppointmentAdapter;
import com.bignerdranch.android.fyp.models.Appointment;
import com.bignerdranch.android.fyp.network.DataRetrievalUser;
import com.bignerdranch.android.fyp.network.DataSaving;
import com.bignerdranch.android.fyp.utils.AppointmentLab;
import com.bignerdranch.android.fyp.utils.UserLab;
import com.bignerdranch.android.fyp.utils.VerticalLineDecorator;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 1/16/2017.
 */

public class AppointmentFragment extends Fragment {

    private final String DEFAULT_STATUS = "Default";

    private static final String TAG = "AppointmentFragment";
    private static final int REQUEST_APPOINTMENT = 0;
    private static final int REQUEST_DIALOG_FRAGMENT = 1;
    private static final String ARG_STATUS = "status";
    private String status;
    List<Appointment> appointmentList = null;
    RecyclerView mRecyclerView;
    AppointmentAdapter mAppointmentAdapter;

    public static AppointmentFragment newInstance(String status) {
        Bundle args = new Bundle();
        args.putString(ARG_STATUS, status);
        AppointmentFragment appointmentFragment = new AppointmentFragment();
        appointmentFragment.setArguments(args);
        return appointmentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.appointment_fragment_tab, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        appointmentList = new ArrayList<>();
        mAppointmentAdapter = new AppointmentAdapter(getActivity(), appointmentList);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new VerticalLineDecorator(1));
        mRecyclerView.setAdapter(mAppointmentAdapter);
        /*
        if (!isViewShown) {
            Log.i(TAG, "OncreateViewShown");
            getStatus();
            isFirstTimeLoad();
        }*/
        return v;
    }

    private void isFirstTimeLoad() {
        if (AppointmentLab.get(getContext()).getAllAppointments().size() == 0) {
            //Log.i(TAG, "First Time Loading");
            load();
        } else {
            //Log.i(TAG, "IsNotNull");
            new SeparateTask(status).execute();
        }
    }

    public void load() {
        new DataRetrievalUser(getContext()).getAppointment(UserLab.get().getUserId(), false, new DataRetrievalUser.OnGetAppointmentListener() {
            @Override
            public void onStart() {
//                customProgressDialog.show();
            }

            @Override
            public void onSuccess(List<Appointment> appointments) {
                AppointmentLab.get(getContext()).setAppointmentList(appointments);
                appointmentList.addAll(appointments);
                mAppointmentAdapter.notifyDataSetChanged();
               // customProgressDialog.dismiss();
            }

            @Override
            public void onFailed(FirebaseError firebaseError) {
                Log.i(TAG, firebaseError.toString());
            }
        });
    }

    // ---------------------------------------
    // This part is used by All Fragment
    // --------------------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getContext(), R.string.invalidBooking, Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(getContext(), R.string.validBooking, Toast.LENGTH_SHORT).show();
            Appointment appointment = (Appointment) data.getSerializableExtra(AppointmentDialogNewFragment.EXTRA_APPOINTMENT);
            new DataSaving().saveAppointment(appointment);
            new DataSaving().saveNotificationToTeacher(appointment.getTeacherId());
            AppointmentLab.get(getContext()).getAllAppointments().add(appointment);
            AppointmentHolderTabFragment.mPagerAdapter.notifyDataSetChanged();

        }
    }


    private class SeparateTask extends AsyncTask<Void, Void, List<Appointment>> {

        String query;
        List<Appointment> statusList;

        public SeparateTask(String query) {
            this.query = query;
            statusList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //customProgressDialog.show();
        }

        @Override
        protected List<Appointment> doInBackground(Void... params) {
            List<Appointment> appointmentListLab = AppointmentLab.get(getContext()).getAllAppointments();
            if (query.equalsIgnoreCase(DEFAULT_STATUS)) {
                return appointmentListLab;
            } else {
                for (Appointment appointment : appointmentListLab) {
                    if (appointment.getBookingStatus().equalsIgnoreCase(query)) {
                        Log.i(TAG, appointment.getBookingStatus());
                        statusList.add(appointment);
                    }
                }
                return statusList;
            }
        }

        @Override
        protected void onPostExecute(List<Appointment> appointments) {
            appointmentList.clear();
            appointmentList.addAll(appointments);
            mAppointmentAdapter.notifyDataChanged();
            //customProgressDialog.dismiss();
        }
    }

    private void getStatus() {
        status = getArguments().getString(ARG_STATUS);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getStatus();
            isFirstTimeLoad();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.appoinment_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_appointment:
                addAppointment();
                return true;
            default:
                break;
        }
        return false;
    }

    private void addAppointment() {
        AppointmentDialogNewFragment dialog = AppointmentDialogNewFragment.newInstance();
        dialog.setTargetFragment(AppointmentFragment.this, REQUEST_DIALOG_FRAGMENT);
        dialog.show(getActivity().getSupportFragmentManager(),"Show");
    }


}
