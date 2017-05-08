package com.bignerdranch.android.fyp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.utils.WifiValidation;
import com.bignerdranch.android.fyp.adapters.TimeLineAdapter;
import com.bignerdranch.android.fyp.models.Course;
import com.bignerdranch.android.fyp.network.DataRetrievalUser;
import com.bignerdranch.android.fyp.utils.SharedMethod;
import com.bignerdranch.android.fyp.utils.UserLab;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David on 4/3/2017.
 * This fragment is used by student
 */

public class RollCallFragment extends Fragment {

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private static final String DIALOG_NOTIFICATION = "DialogNotification";
    private static final String TAG = "RollCallFragment";
    private static final int INVALID_MAC = 5;
    private static final int INVALID_DATE_TIME = 6;
    private static final int WRONG_TIME_ZONE = 7;
    private static final int NO_WIFI = 8;
    private static final int INVALID_WIFI = 9;
    private static final int NO_COURSE = 10;
    private static final String DESIRED_TIME_ZONE = "Hong_Kong";
    private static final String DESIRED_WIFI = "OUHK-Student";
    private TimeLineAdapter mTimeLineAdapter;
    private List<Course> mDataList = new ArrayList<>();
    private DataRetrievalUser mDataRetrievalUser;
    private String dayOfWeek;
    WifiValidation mWifi;

    public static RollCallFragment newInstance() {
        return new RollCallFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataRetrievalUser = new DataRetrievalUser(getContext());
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.timetable_fragment_container, container, false);
        mWifi = new WifiValidation(getContext());
        ButterKnife.bind(this, v);
        checkValidation();
        return v;
    }

    private void checkValidation() {
        if (WifiValidation.getMacAddr().equalsIgnoreCase(UserLab.get().getMacAddress())) {
            if (mWifi.isAutomaticDateAndTime() == 1 && mWifi.isAutomaticTimeZone() == 1) {
                if (mWifi.getTimeZone().contains(DESIRED_TIME_ZONE)) {
                    if (mWifi.isWifiConnected()) {
                        validUserUI();
                    } else {
                        invalidUserUI(NO_WIFI);
                    }
                } else {
                    invalidUserUI(WRONG_TIME_ZONE);
                }
            } else {
                invalidUserUI(INVALID_DATE_TIME);
            }
        } else {
            invalidUserUI(INVALID_MAC);
        }
    }

    private void validUserUI() {
        getCourseCode();
        dayOfWeek = SharedMethod.findDayOfWeek();

        //mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        initView();
    }

    private void invalidUserUI(int mesNum) {
        mProgressBar.setVisibility(View.GONE);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        AlertDialogFragment dialog = AlertDialogFragment
                .newInstance("InvalidUser", mesNum);
        dialog.show(manager, DIALOG_NOTIFICATION);
    }

    private void initView() {
        mTimeLineAdapter = new TimeLineAdapter(mDataList, getContext());
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    private void getCourseCode() {
        mDataRetrievalUser.getCourseCode(new DataRetrievalUser.OnGetStringListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String data) {
                List<String> courseCode = SharedMethod.findCourses(data);
                getCourses(courseCode);
            }

            @Override
            public void onFailed(FirebaseError firebaseError) {

            }
        });
    }

    private void getCourses(final List<String> courses) {
        for (int i = 0 ; i < courses.size() ; i++) {
            final int count = i;
            mDataRetrievalUser.getCourses(courses.get(i), new DataRetrievalUser.OnGetCourseListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(Course course) {
                    findTodayCourse(course);
                    if (courses.size() == count + 1) {
                        if (UserLab.get().getCourses() == null || UserLab.get().getCourses().size() == 0) {
                            UserLab.get().setCourses(mDataList);
                        }
                        mProgressBar.setVisibility(View.GONE);
                        checkTodayCourse();
                    }
                }

                @Override
                public void onFailed(FirebaseError firebaseError) {

                }
            });
        }
    }

    private void findTodayCourse(Course course) {
        if (SharedMethod.isTodayCourse(course.getDay(), dayOfWeek)) {
            mDataList.add(course);
        }
    }

    private void checkTodayCourse() {
        if (mDataList.size() == 0) {
            invalidUserUI(NO_COURSE);
        } else {
            mTimeLineAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
