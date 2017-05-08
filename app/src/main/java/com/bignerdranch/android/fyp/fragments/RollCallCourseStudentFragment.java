package com.bignerdranch.android.fyp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.adapters.CourseStudentAdapter;
import com.bignerdranch.android.fyp.models.Course;
import com.bignerdranch.android.fyp.models.Student;
import com.bignerdranch.android.fyp.network.DataRetrievalUser;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 4/9/2017.
 * This fragment is used by teacher
 */

public class RollCallCourseStudentFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private CourseStudentAdapter mAdapter;
    private static final int NUMBER_OF_COLUMN = 2;
    private static final int VERTICAL_ITEM_SPACE = 48;
    private static final String COURSE = "Course";
    private Course course = null;
    private List<String> idList;
    private List<Student> studentList;

    public static RollCallCourseStudentFragment newInstance(Course course) {
        Bundle args = new Bundle();
        args.putSerializable(COURSE, course);
        RollCallCourseStudentFragment fragment = new RollCallCourseStudentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        course = (Course)getArguments().getSerializable(COURSE);
        if (course != null) {
            idList = course.getMembership();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_view, container, false);
        studentList = new ArrayList<>();
        mAdapter = new CourseStudentAdapter(getContext(), studentList);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), NUMBER_OF_COLUMN));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        getCourseStudent();
        return v;
    }

    public void getCourseStudent() {
        for (int i = 0; i < idList.size(); i++) {
            new DataRetrievalUser(getContext()).getStudentInfoToShowInCourseList(idList.get(i), new DataRetrievalUser.OnGetStudentListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(Student s) {
                    studentList.add(s);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed(FirebaseError firebaseError) {

                }
            });
        }
    }

}
