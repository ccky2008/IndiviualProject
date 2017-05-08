package com.bignerdranch.android.fyp.adapters;

import android.content.Context;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.models.Appointment;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by David on 1/19/2017.
 */

public class AppointmentSwapAdapter extends SwipeMenuAdapter<AppointmentSwapAdapter.AppointmentHolder> {

    static Context context;
    List<Appointment> appointments;


    public AppointmentSwapAdapter(Context context, List<Appointment> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(context).inflate(R.layout.appointment_row_item, parent, false);
    }

    @Override
    public AppointmentHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new AppointmentHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(AppointmentHolder holder, int position) {
        holder.bindData(appointments.get(position));
    }

    @Override
    public int getItemCount() {
        return appointments == null ? 0 : appointments.size();
    }


    static class AppointmentHolder extends RecyclerView.ViewHolder  {
        TextView mDate;
        TextView mLocation;
        TextView mTutor;
        LinearLayout mLinearLayout;

        public AppointmentHolder(View itemView) {
            super(itemView);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.appointment_row_box);
            mDate = (TextView) itemView.findViewById(R.id.appointment_date);
            mLocation = (TextView) itemView.findViewById(R.id.appointment_location);
            mTutor = (TextView) itemView.findViewById(R.id.appointment_tutor);
        }

        void bindData(Appointment appointment){
            setRowBorder(mLinearLayout, appointment.getBookingStatus());
            mDate.setText("Time : " + appointment.getBookingTime() + " (" + appointment.getBookingDate() + ")");
            mLocation.setText("Location : " + appointment.getBookingLocation());
            //mTutor.setText("Lecturer : " + appointment.getTeacherEmail());
        }
    }

    private static void setRowBorder (LinearLayout linearLayout, String status) {
        switch (status) {
            case "Success":
                linearLayout.setBackgroundResource(R.drawable.tab_row_inset_success_border);
                break;
            case "Failure":
                linearLayout.setBackgroundResource(R.drawable.tab_row_inset_failure_border);
                break;
            default:
                linearLayout.setBackgroundResource(R.drawable.tab_row_inset_inprogress_border);
                break;
        }
    }
}
