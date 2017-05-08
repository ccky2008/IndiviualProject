package com.bignerdranch.android.fyp.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.fyp.utils.CustomViewPager;
import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.models.Question;

import java.util.List;

/**
 * Created by David on 12/25/2016.
 */

public class QuestionViewPagerFragment extends Fragment {

    private static final String EXTRA_QUESTION_ID = "com.bignerdranch.android.fyp.question_id";
    private static final String EXTRA_QUESTION_LIST = "com.bignerdranch.android.fyp.question_list";
    private CustomViewPager mViewPager;

    OnDataQuestionPass dataQuestionPasser;

    public interface OnDataQuestionPass {
        public void onDataQuestionPass(Question question);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if (context instanceof  Activity) {
            dataQuestionPasser = (OnDataQuestionPass) activity;
        }
    }

    public void passData(Question question) {
        dataQuestionPasser.onDataQuestionPass(question);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forum_fragment_view_pager, container, false);
        mViewPager = (CustomViewPager) view.findViewById(R.id.activity_question_pager_view_pager);
        mViewPager.setPagingEnabled(false);
        Bundle bundle = this.getArguments();
        final int questionId = bundle.getInt(EXTRA_QUESTION_ID);
        final List<Question> mQuestionsList = bundle.getParcelableArrayList(EXTRA_QUESTION_LIST);

        final FragmentManager fragmentManager = getFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                if (mQuestionsList == null || mQuestionsList.isEmpty()) {
                    Log.i("Test", "The list has a error");
                }
                return QuestionDetailFragment.newInstance(mQuestionsList.get(position));
            }

            @Override
            public int getCount() {
                return mQuestionsList.size();
            }
        });

        Integer qId = new Integer(questionId);
        for (int i = 0; i < mQuestionsList.size(); i++) {
            Integer listQuestionId = new Integer(mQuestionsList.get(i).getID());
            if (listQuestionId.equals(qId)) {
                Question question = mQuestionsList.get(i);
                Log.i("check valid question id", Integer.toString(question.getID()));
                mViewPager.setCurrentItem(i);
                passData(question);
            }
        }
        return view;
    }
}
