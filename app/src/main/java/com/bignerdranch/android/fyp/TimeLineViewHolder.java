package com.bignerdranch.android.fyp;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.fyp.models.Course;
import com.bignerdranch.android.fyp.network.DataSaving;
import com.bignerdranch.android.fyp.utils.UserLab;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David on 4/3/2017.
 */

public class TimeLineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "ViewHolder";
    @BindView(R.id.text_timeline_id)
    public TextView mId;
    @BindView(R.id.text_timeline_title)
    public TextView mMessage;
    @BindView(R.id.text_timeline_time)
    public TextView mTime;
    @BindView(R.id.time_marker)
    public TimelineView mTimelineView;
    @BindView(R.id.roll_call_image_button)
    public ImageButton mImageButton;

    private DataSaving mDataSaving;
    private Context mContext;

    public TimeLineViewHolder(View itemView, int viewType, Context context) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        mTimelineView.initLine(viewType);
        mImageButton.setOnClickListener(this);
        mContext = context;
        mDataSaving = new DataSaving();
    }

    @Override
    public void onClick(View v) {
        Course course = UserLab.get().getCourses().get(getAdapterPosition()) ;
        Log.i(TAG, "Onclick Image button " + course.getId());
        mDataSaving.saveRollCallToStudent(course.getId());
        mDataSaving.saveRollCallToCourse(course.getKey());
        mDataSaving.decreaseCourseRollCallAbsent(course.getKey(), course.getNoOfStudents());
        mDataSaving.increaseCourseRollCallCount(course.getKey());
        mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorPrimary));
        mImageButton.setVisibility(View.GONE);
        mTime.setText(R.string.rollCallCompleted);
        Toast.makeText(mContext, R.string.rollCallCompleted, Toast.LENGTH_SHORT).show();

        if (UserLab.get().getRollCall() == null) {
            List<String> courseCode = new ArrayList<>();
            courseCode.add(course.getId());
            UserLab.get().setRollCall(courseCode);
        } else {
            UserLab.get().getRollCall().add(course.getId());
        }
    }

}
