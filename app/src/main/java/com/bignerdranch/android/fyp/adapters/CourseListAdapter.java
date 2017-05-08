package com.bignerdranch.android.fyp.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.fragments.RollCallCourseListFragment;
import com.bignerdranch.android.fyp.fragments.RollCallCourseStudentFragment;
import com.bignerdranch.android.fyp.fragments.RollCallDayFragment;
import com.bignerdranch.android.fyp.models.Course;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David on 4/9/2017.
 */

public class CourseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "CourseListAdapter";
    public Context mContext;
    private List<Course> mCourses;

    public CourseListAdapter(Context context, List<Course> courses) {
        mContext = context;
        mCourses = courses;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new CourseHolder(inflater.inflate(R.layout.rollcall_list_item_course, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CourseHolder) holder).bindData(mCourses.get(position));
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    static class CourseHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.course_id)
        TextView courseId;
        @BindView(R.id.course_name)
        TextView courseName;
        @BindView(R.id.course_day_of_week)
        TextView courseDayOfWeek;
        @BindView(R.id.course_day_of_time)
        TextView courseDayOfTime;
        @BindView(R.id.course_student)
        Button courseStudentButton;
        @BindView(R.id.attendance_list)
        Button courseAttentanceButton;

        public CourseHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(Course course) {
            final Course courseToDisplay = course;
            courseId.setText(course.getId());
            courseName.setText(course.getName());
            courseDayOfWeek.setText(course.getDay());
            String time = course.getStartTime() + "  :  " + course.getEndTime();
            courseDayOfTime.setText(time);

            courseStudentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RollCallCourseStudentFragment fragment = RollCallCourseStudentFragment.newInstance(courseToDisplay);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fContent, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            courseAttentanceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RollCallDayFragment fragment = RollCallDayFragment.newInstance(courseToDisplay);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fContent, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });


        }

    }
}
