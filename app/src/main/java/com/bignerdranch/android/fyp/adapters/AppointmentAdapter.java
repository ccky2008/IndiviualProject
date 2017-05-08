package com.bignerdranch.android.fyp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.models.Appointment;
import java.util.List;

/**
 * Created by David on 1/3/2017.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_APPOINTMENT = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
    List<Appointment> appointments;
    OnLoadMoreListener loadMoreListener;
    Activity activity;
    boolean isLoading = false, isMoreDataAvailable = true;

    public AppointmentAdapter(Context context, List<Appointment> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType == TYPE_APPOINTMENT){
                return new AppointmentHolder(inflater.inflate(R.layout.appointment_row_item, parent, false));
        } else {
            return new LoadHolder(inflater.inflate(R.layout.row_load_indicator,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener != null){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if(getItemViewType(position) == TYPE_APPOINTMENT){
                ((AppointmentHolder) holder).bindData(appointments.get(position));
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {
        if(appointments.get(position).getBookingStatus().equals("load")){
            return TYPE_LOAD;
        }else{
            return TYPE_APPOINTMENT;
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

     /* VIEW HOLDERS */

     static class AppointmentHolder extends RecyclerView.ViewHolder {
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
            mTutor.setText("Lecturer : " + appointment.getTeacherName());
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

    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
