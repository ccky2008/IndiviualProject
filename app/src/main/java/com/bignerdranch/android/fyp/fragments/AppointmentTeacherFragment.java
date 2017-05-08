package com.bignerdranch.android.fyp.fragments;

import android.graphics.Color;
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

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.adapters.AppointmentSwapAdapter;
import com.bignerdranch.android.fyp.models.Appointment;
import com.bignerdranch.android.fyp.network.DataRetrievalUser;
import com.bignerdranch.android.fyp.network.DataSaving;
import com.bignerdranch.android.fyp.utils.QueryPreferences;
import com.bignerdranch.android.fyp.utils.UserLab;
import com.bignerdranch.android.fyp.utils.VerticalLineDecorator;

import com.firebase.client.FirebaseError;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 1/17/2017.
 */

// Teacher Part
// Need to implement as a teacher account
//studentId need to change;

public class AppointmentTeacherFragment extends Fragment {

    private static final String TAG = "AppointmentTeacherFragment";
    private static final String ARG_STATUS = "appointmentTeacherFragment.status";
    private static String status = "Progress";
    private static final String SUCCESS = "Success";
    private static final String FAILURE = "Failure";
    private boolean isProgress = true;

    List<Appointment> mAppointmentList = null;
    List<Appointment> mStatusAppointmentList;
    AppointmentSwapAdapter mAppointmentSwapAdapter;
    LinearLayoutManager mLinearLayoutManger;

    private SwipeMenuRecyclerView mSwipeMenuRecyclerView;

   public static AppointmentTeacherFragment newInstance() {
       return new AppointmentTeacherFragment();
   }

    @Override
    public void onResume() {
        super.onResume();
        isProgress = true;
        status = "Progress";
        setSwapMenu();
        if (mAppointmentList == null) {
            load();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.appointment_fragment_swap, container, false);
        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) v.findViewById(R.id.recycler_view_swap);

        return v;
    }

    private void setSwapMenu() {

        if (isProgress) {
            mSwipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
            mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);
        } else {
            mSwipeMenuRecyclerView.setSwipeMenuCreator(null);
            mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(null);
        }
        mStatusAppointmentList = new ArrayList<>();
        mAppointmentSwapAdapter = new AppointmentSwapAdapter (getActivity(), mStatusAppointmentList);
        mSwipeMenuRecyclerView.setAdapter(mAppointmentSwapAdapter);
        mSwipeMenuRecyclerView.setHasFixedSize(true);
        mLinearLayoutManger = new LinearLayoutManager(getActivity());
        mSwipeMenuRecyclerView.setLayoutManager(mLinearLayoutManger);
        mSwipeMenuRecyclerView.addItemDecoration(new VerticalLineDecorator(1));
        //setScrollListener();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.appoinment_fragment_teacher, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_success:
                if (isProgress) {
                    isProgress = false;
                    item.setTitle(R.string.teacher_fragment_sorting_in_progress);
                    status = "Success"; // change search status
                    initList();
                } else {
                    isProgress = true;
                    item.setTitle(R.string.teacher_fragment_sorting_accepted);
                    status = "Progress";
                    initList();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initList() {
        mStatusAppointmentList.clear();
        setSwapMenu();
        setWantedList(status);
    }

    private void load() {
      new DataRetrievalUser(getContext()).getAppointment(UserLab.get().getUserId(), true, new DataRetrievalUser.OnGetAppointmentListener() {
          @Override
          public void onStart() {
              Log.i("teacher", "onStart");
          }

          @Override
          public void onSuccess(List<Appointment> appointmentList) {
              Log.i("teacher", Integer.toString(appointmentList.size()));
              mAppointmentList = appointmentList;
              setWantedList(status);
          }

          @Override
          public void onFailed(FirebaseError firebaseError) {

          }
      });
    }

    private void setWantedList(String status) {
        for(Appointment appointment: mAppointmentList) {
            if (appointment.getBookingStatus().equals(status)) {
                mStatusAppointmentList.add(appointment);
            }
        }
        mAppointmentSwapAdapter.notifyDataSetChanged();
    }

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelOffset(R.dimen.swap_menu_item_size);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            {
                SwipeMenuItem acceptAppointment = new SwipeMenuItem(getContext())
                        .setBackgroundDrawable(R.color.colorGreen)
                        .setImage(R.drawable.ic_action_tick)
                        .setText("Accept")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(acceptAppointment); // 添加一个按钮到右侧菜单。

                SwipeMenuItem refuseAppointment = new SwipeMenuItem(getContext())
                        .setBackgroundDrawable(R.color.colorRed)
                        .setImage(R.drawable.ic_action_cross)
                        .setText("Refuse") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(refuseAppointment);// 添加一个按钮到右侧侧菜单。

            }
        }
    };

    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// close the menu
            if (menuPosition == 0) {// An item is click from the menu
                changeListStatus(mStatusAppointmentList.get(adapterPosition).getId(), SUCCESS);
                updateStatus(mStatusAppointmentList.get(adapterPosition).getId(), SUCCESS);
            } else if (menuPosition == 1) {
                changeListStatus(mStatusAppointmentList.get(adapterPosition).getId(), FAILURE);
                updateStatus(mStatusAppointmentList.get(adapterPosition).getId(), FAILURE);
            }
            sendNotificationToStudent(mStatusAppointmentList.get(adapterPosition).getStudentId());
            mStatusAppointmentList.remove(adapterPosition);
            mAppointmentSwapAdapter.notifyDataSetChanged();
        }
    };

    private void changeListStatus(String id, String status) {
        for(Appointment ap: mAppointmentList) {
            if(ap.getId().equals(id)) {
                ap.setBookingStatus(status);
            }
        }
    }

    private void updateStatus(String id, String status) {
        new DataSaving().changeAppointmentStatus(id, status);
    }

    private void sendNotificationToStudent(String studentId) {
        new DataSaving().saveNotificationToStudent(studentId);
    }

}
