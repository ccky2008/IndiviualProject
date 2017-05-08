package com.bignerdranch.android.fyp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.utils.SharedMethod;
import com.bignerdranch.android.fyp.adapters.QuizCourseCatalogAdapter;
import com.bignerdranch.android.fyp.models.Course;
import com.bignerdranch.android.fyp.network.DataRetrievalUser;
import com.bignerdranch.android.fyp.utils.UserLab;
import com.bignerdranch.android.fyp.utils.VerticalSpaceItemDecoration;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 4/21/2017.
 */

public class QuizCourseCatalogFragment extends Fragment {

    private static final String TAG = "QuizCourse";
    private static final int VERTICAL_ITEM_SPACE = 24;
    DataRetrievalUser mDataRetrievalUser;
    private List<Course> mDataList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private QuizCourseCatalogAdapter mAdapter;
    private View v = null;

    public static QuizCourseCatalogFragment newInstance() {
        return new QuizCourseCatalogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataRetrievalUser = new DataRetrievalUser(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      if (v == null) {
           v = inflater.inflate(R.layout.recycler_view, container, false);


           mAdapter = new QuizCourseCatalogAdapter(getContext(), mDataList);
           mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
           mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
           mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
           mRecyclerView.setAdapter(mAdapter);
           checkLabCourse();
      }
        return v;
    }

    private void checkLabCourse() {
        if (UserLab.get().getCourses() == null || UserLab.get().getCourses().size() == 0) {
            getCourseCode();
            Log.i(TAG, "lab is null");
        } else {
            Log.i(TAG, "lab is not null");
            mDataList.addAll(UserLab.get().getCourses());
            mAdapter.notifyDataSetChanged();
        }
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
                    mDataList.add(course);
                    if (courses.size() == count + 1) {
                        if (UserLab.get().getCourses() == null || UserLab.get().getCourses().size() == 0) {
                            UserLab.get().setCourses(mDataList);
                            mAdapter.notifyDataSetChanged();

                        }
                        //mProgressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailed(FirebaseError firebaseError) {

                }
            });
        }
    }

}
