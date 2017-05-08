package com.bignerdranch.android.fyp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.adapters.CourseListAdapter;
import com.bignerdranch.android.fyp.models.Course;
import com.bignerdranch.android.fyp.network.DataRetrievalUser;
import com.bignerdranch.android.fyp.utils.UserLab;
import com.bignerdranch.android.fyp.utils.VerticalSpaceItemDecoration;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by David on 4/9/2017.
 * This fragment is used by teacher
 */

public class RollCallCourseListFragment extends Fragment{

    private static final String TAG = "RollCallCourseListFragment";
    RecyclerView mRecyclerView;
    CourseListAdapter mAdapter;
    private List<Course> mCourses;
    private static final int VERTICAL_ITEM_SPACE = 24;

    public static RollCallCourseListFragment newInstance() {
        return new RollCallCourseListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getCourseList();
        View v = inflater.inflate(R.layout.recycler_view, container, false);

        mCourses = new ArrayList<>();
        mAdapter = new CourseListAdapter(getContext(), mCourses);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    private void getCourseList() {
        new DataRetrievalUser(getContext()).getListOfCourse(UserLab.get().getUserId(), new DataRetrievalUser.OnGetCourseListListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(List<Course> courseList) {
                mCourses.addAll(courseList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(FirebaseError firebaseError) {

            }
        });
    }
}
