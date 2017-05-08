package com.bignerdranch.android.fyp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.fyp.utils.GenericDate;
import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.activities.QuizQuestionActivity;
import com.bignerdranch.android.fyp.fragments.AlertDialogFragment;
import com.bignerdranch.android.fyp.models.Quiz;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David on 4/19/2017.
 */

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizHolder>{

    public Context mContext;
    public List<Quiz> mQuizList;

    public QuizListAdapter(Context context, List<Quiz> quizList) {
        mContext = context;
        mQuizList = quizList;
    }

    @Override
    public QuizHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new QuizHolder(inflater.inflate(R.layout.quiz_catalog_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return mQuizList.size();
    }

    @Override
    public void onBindViewHolder(QuizHolder holder, int position) {
        Quiz quiz = mQuizList.get(position);
        holder.quizQuestionNum.setText(mContext.getString(R.string.quiz_question_num, quiz.getTotalNum()));
        holder.quizTimeLimit.setText(mContext.getString(R.string.quiz_time_limit, quiz.getTimeLimit()));
        holder.quizDate.setText(mContext.getString(R.string.quiz_start_at, quiz.getDate()));
        holder.quizTime.setText(quiz.getStartTime());
        //((QuizListAdapter.QuizHolder) holder).bindData(mQuizList.get(position));
    }

    public class QuizHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.quiz_question_num)
        public TextView quizQuestionNum;
        @BindView(R.id.quiz_time_limit)
        public TextView quizTimeLimit;
        @BindView(R.id.quiz_date)
        public TextView quizDate;
        @BindView(R.id.quiz_time)
        public TextView quizTime;
        private final Context context;

        public QuizHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            context = itemView.getContext();
        }


        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            String quizDate = mQuizList.get(clickPosition).getDate();
            String quizTime = mQuizList.get(clickPosition).getStartTime();
            String quizTimeLimit = mQuizList.get(clickPosition).getTimeLimit();
            String endTime = GenericDate.addMinutesToTime(quizTime, Integer.parseInt(quizTimeLimit));
            String endDateTime = quizDate + " " + endTime;
            String startDateTime = quizDate + " " + quizTime;

            if (GenericDate.comparedDay(startDateTime, endDateTime)) {
                Intent intent = new Intent(context, QuizQuestionActivity.class);
                ArrayList<Integer> passData = (ArrayList<Integer>) mQuizList.get(clickPosition).getQuestionNum();
                intent.putIntegerArrayListExtra("LIST", passData);
                intent.putExtra("TIMER", quizTimeLimit);
                intent.putExtra("COURSE_CODE", mQuizList.get(clickPosition).getSubject());
                intent.putExtra("QuizId", mQuizList.get(clickPosition).getId());
                intent.putExtra("EndTime", endTime);
                context.startActivity(intent);
                mQuizList.remove(clickPosition);
                notifyDataSetChanged();
            } else {
               AppCompatActivity activity = (AppCompatActivity) v.getContext();
                FragmentManager manager = activity.getSupportFragmentManager();
                AlertDialogFragment dialog = AlertDialogFragment.newInstance("Error", 13) ;
                dialog.show(manager, "Error");
            }
        }
    }
}
