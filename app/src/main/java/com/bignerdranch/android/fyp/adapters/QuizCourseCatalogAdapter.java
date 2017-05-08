package com.bignerdranch.android.fyp.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.fragments.QuizTestCatalogFragment;
import com.bignerdranch.android.fyp.models.Course;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David on 4/21/2017.
 */

public class QuizCourseCatalogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context mContext;
    private List<Course> mCourses;
    private static final String COURSE_CODE = "KEY";

    public QuizCourseCatalogAdapter(Context context, List<Course> courses) {
        mContext = context;
        mCourses = courses;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new CourseHolder(inflater.inflate(R.layout.quiz_course_catalog_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CourseHolder)holder).bindData(mCourses.get(position));
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    static class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.course_id)
        TextView courseId;
        @BindView(R.id.course_name)
        TextView courseName;
        @BindView(R.id.course_day_of_week)
        TextView courseDayOfWeek;
        @BindView(R.id.course_day_of_time)
        TextView courseDayOfTime;

        private String courseCode = null;
        CourseHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindData(Course c) {
            courseCode = c.getId();
            courseId.setText(c.getId());
            courseName.setText(c.getName());
            courseDayOfWeek.setText(c.getDay());
            String time = c.getStartTime() + "  :  " + c.getEndTime();
            courseDayOfTime.setText(time);
        }

        @Override
        public void onClick(View v) {
            QuizTestCatalogFragment fragment = QuizTestCatalogFragment.newInstance(courseCode);
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fContent, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
