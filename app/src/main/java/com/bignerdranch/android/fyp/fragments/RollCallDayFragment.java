package com.bignerdranch.android.fyp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.fyp.utils.GenericDate;
import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.adapters.CourseStudentAdapter;
import com.bignerdranch.android.fyp.models.Course;
import com.bignerdranch.android.fyp.models.Student;
import com.bignerdranch.android.fyp.network.DataRetrievalUser;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David on 4/12/2017.
 */

public class RollCallDayFragment extends Fragment {

    private static final String COURSE = "Course";
    private static final int NUMBER_OF_COLUMN = 3;
    private final int PRESENT_HEADER = 0;
    public final int TYPE_STUDENT = 1;
    private CourseStudentAdapter mAdapter;
    private RecyclerView mRecyclerView;
    @BindView(R.id.present_student)
    TextView presentStudent;
    @BindView(R.id.total_student)
    TextView totalStudent;
    @BindView(R.id.absent_student)
    TextView absentStudent;
    @BindView(R.id.date_today)
    TextView date;
    private List<String> todayRollCallList = null;
    private List<String> memberList;
    private List<Student> presentList;
    private List<Student> absentList;
    private Course mCourse = null;

    public static RollCallDayFragment newInstance(Course c) {
        Bundle args = new Bundle();
        args.putSerializable(COURSE, c);
        RollCallDayFragment fragment = new RollCallDayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCourse = (Course) getArguments().getSerializable(COURSE);
        if (mCourse != null) {
            memberList = mCourse.getMembership();
            todayRollCallList = mCourse.getTodayRollCallStudent();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.rollcall_list_item_day, container, false);
        ButterKnife.bind(this, v);
        initList();

        mAdapter = new CourseStudentAdapter(getContext(), presentList);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        GridLayoutManager layout = new GridLayoutManager(getContext(), NUMBER_OF_COLUMN);
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mAdapter.getItemViewType(position) == TYPE_STUDENT ? 1 : NUMBER_OF_COLUMN;
            }
        });

        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        setTextForHeader();
        getCourseStudent();
        return v;
    }

    private void initList() {
        presentList = new ArrayList<>();
        Student s = new Student();
        s.setId("Text");
        s.setName("Present :");
        presentList.add(s);
        absentList = new ArrayList<>();
        Student ss = new Student();
        ss.setId("Text");
        ss.setName("Absent :");
        absentList.add(ss);
    }
    private void setTextForHeader() {
        presentStudent.setText(getString(R.string.presentStudent,  (int)mCourse.getNoOfStudents() - calculateAbsentNumberStudent()));
        absentStudent.setText(getString(R.string.absentStudent, calculateAbsentNumberStudent()));
        totalStudent.setText(getString(R.string.totalStudent, (int)mCourse.getNoOfStudents()));
        date.setText(GenericDate.getTodayForRollCall());
    }

    private int calculateAbsentNumberStudent() {
        return (int) mCourse.getNoOfStudents() - (int) mCourse.getTodayNumStudent();
    }

    public void getCourseStudent() {
        for (int i = 0; i < memberList.size(); i++) {
            final int count = i;
            new DataRetrievalUser(getContext()).getStudentInfoToShowInCourseList(memberList.get(i), new DataRetrievalUser.OnGetStudentListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(Student s) {
                   checkPresentOrAbsent(s);
                    if(memberList.size() == count + 1) {
                        if (todayRollCallList == null) {
                            presentList.remove(PRESENT_HEADER);
                        }
                        presentList.addAll(absentList);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailed(FirebaseError firebaseError) {

                }
            });
        }
    }

    private void checkPresentOrAbsent(Student s) {
        if (todayRollCallList != null) {
            for (int i = 0; i < todayRollCallList.size(); i++) {
                if (s.getId().equalsIgnoreCase(todayRollCallList.get(i))) {
                    presentList.add(s);
                    return;
                }
            }
        }
        absentList.add(s);
        return;
    }
}
