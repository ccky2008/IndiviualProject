package com.bignerdranch.android.fyp.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.TimeLineViewHolder;
import com.bignerdranch.android.fyp.models.Course;
import com.bignerdranch.android.fyp.utils.SharedMethod;
import com.bignerdranch.android.fyp.utils.GenericDate;
import com.bignerdranch.android.fyp.utils.UserLab;
import com.bignerdranch.android.fyp.utils.VectorDrawableUtils;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

/**
 * Created by David on 4/3/2017.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder>{

    private List<Course> mFeedList;
    private List<String> mCompletedRollCallList;
    private Context mContext;
    private boolean isSignUnNow;

    public TimeLineAdapter(List<Course> feedList, Context context) {
        mFeedList = feedList;
        mContext = context;
        mCompletedRollCallList = UserLab.get().getRollCall();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.timetable_fragment, null);
        return new TimeLineViewHolder(view, viewType, mContext);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {

        Course course = mFeedList.get(position);
        Log.i("Adapter", "Position of ReyclerView" + Integer.toString(position));
        holder.mId.setText(course.getId());
        holder.mMessage.setText(course.getName());


        if (mCompletedRollCallList!= null) {
            Log.i("Adapter", "course completed changed");
            Log.i("Adapter", Integer.toString(mCompletedRollCallList.size()));
            for (int i = 0; i < mCompletedRollCallList.size(); i++) {
                if (course.getId().equalsIgnoreCase(mCompletedRollCallList.get(i))) {
                    holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorPrimary));
                    holder.mTime.setText(R.string.rollCallCompleted);
                    return;
                }
            }
        }

        Log.i("Adpater", "After for loop");
        holder.mTime.setText(getTime(course));

        if (isSignUnNow) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorPrimary));
            holder.mImageButton.setVisibility(View.VISIBLE);
            isSignUnNow = false;
        } else {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        }
    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }

    private String getTime(Course course) {
        String duringRollCall = " left to take roll call";
        String beforeCourse = " before class start";
        long dif = SharedMethod.compareTimeMilli(course.getStartTime());
        boolean isBeforeClass = SharedMethod.isBeforeCourseStartTime(dif);
        boolean isAfterClass;
        String timeString = "Roll call time is over";
        if (isBeforeClass) {
            return timeString = SharedMethod.timeDistance(dif) + beforeCourse;
        } else {
            isAfterClass = SharedMethod.isAfterCourseTime(GenericDate.getMilliSec(GenericDate.getTime()), GenericDate.getMilliSec(course.getEndTime()));
            if (isAfterClass) {
                return timeString;
            } else {
                isSignUnNow = true;
                return timeString = SharedMethod.timeDistance(Math.abs(SharedMethod.compareTimeMilli(course.getEndTime()))) + duringRollCall;
            }
        }
    }
}
