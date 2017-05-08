package com.bignerdranch.android.fyp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.models.Student;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David on 4/9/2017.
 */

public class CourseStudentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_TEXT = 0;
    public final int TYPE_STUDENT = 1;
    public Context mContext;
    private List<Student> mStudentList;

    public CourseStudentAdapter(Context context, List<Student> studentList) {
        mContext = context;
        mStudentList = studentList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == TYPE_STUDENT) {
            return new CourseStudentHolder(inflater.inflate(R.layout.rollcall_list_course_student, parent, false));
        } else {
            return new TextRowHolder(inflater.inflate(R.layout.rollcall_list_item_text_row, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Student s = mStudentList.get(position);
        if (getItemViewType(position) == TYPE_STUDENT) {
            ((CourseStudentHolder)holder).bindData(s);
        } else {
            ((TextRowHolder)holder).bindData(s);
        }
    }

    @Override
    public int getItemCount() {
        return mStudentList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mStudentList.get(position).getId().equalsIgnoreCase("Text")) {
            return TYPE_TEXT;
        } else {
            return TYPE_STUDENT;
        }
    }

    public static class CourseStudentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.student_icon)
        public ImageView studentIcon;
        @BindView(R.id.student_id)
        public TextView studentId;
        @BindView(R.id.student_name)
        TextView studentName;
        Context mContext;
        public CourseStudentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        public void bindData(Student s) {
            Picasso.with(mContext).
                    load(s.getIconLink())
                    .placeholder(R.drawable.ic_author_name)
                    .error(R.drawable.ic_author_name)
                    .into(studentIcon);
            studentId.setText(s.getId());
            studentName.setText(s.getName());
        }
    }

    public static class TextRowHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.present_text)
        TextView textForDivider;

        public TextRowHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(Student s) {
            textForDivider.setText(s.getName());
        }
    }
}
