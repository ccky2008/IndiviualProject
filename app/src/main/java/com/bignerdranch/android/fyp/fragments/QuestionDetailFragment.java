package com.bignerdranch.android.fyp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.models.Question;

/**
 * Created by David on 12/19/2016.
 */

public class QuestionDetailFragment extends Fragment {

    private static final String ARG_QUESTION = "question";
    private Question mQuestion;
    TextView mQuestionTitle;
    TextView mQuestionContent;
    TextView mQuestionAuthor;
    TextView mQuestionCommentCount;
    TextView mQuestionSubject;

    public static QuestionDetailFragment newInstance(Question question) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_QUESTION, question);
        QuestionDetailFragment questionDetailFragment = new QuestionDetailFragment();
        questionDetailFragment.setArguments(args);
        return questionDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuestion = getArguments().getParcelable(ARG_QUESTION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forum_fragment_question_detail, container, false);
        mQuestionTitle = (TextView) v.findViewById(R.id.question_topic);
        mQuestionTitle.setText(mQuestion.getTitle());

        mQuestionContent = (TextView) v.findViewById(R.id.question_content);
        mQuestionContent.setText(mQuestion.getContent());

        mQuestionAuthor = (TextView) v.findViewById(R.id.question_author);
        mQuestionAuthor.setText(mQuestion.getAuthor());

        mQuestionSubject = (TextView) v.findViewById(R.id.question_subject);
        mQuestionSubject.setText(mQuestion.getSubject());

        mQuestionCommentCount = (TextView) v.findViewById(R.id.question_comment_count);
        //Log.i("QuestionDetail", Integer.toString(mQuestion.getCommentCount()));
        mQuestionCommentCount.setText(Integer.toString(mQuestion.getCommentCount()));

        return v;
    }
}
