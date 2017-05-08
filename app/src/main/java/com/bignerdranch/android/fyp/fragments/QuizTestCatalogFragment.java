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
import com.bignerdranch.android.fyp.adapters.QuizListAdapter;
import com.bignerdranch.android.fyp.models.Quiz;
import com.bignerdranch.android.fyp.network.DataRetrievalUser;
import com.bignerdranch.android.fyp.utils.UserLab;
import com.bignerdranch.android.fyp.utils.VerticalSpaceItemDecoration;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 4/19/2017.
 */

public class QuizTestCatalogFragment extends Fragment{

    private static final int VERTICAL_ITEM_SPACE = 24;
    private static final String TAG = "QUIZ_TEST";
    private static final String COURSE_CODE = "KEY";
    private List<Quiz> mQuizList;
    private RecyclerView mRecyclerView;
    private QuizListAdapter mAdapter;
    private String courseCode = null;
    private List<String> doneQuiz;
    private DataRetrievalUser mDataRetrievalUser;
    private boolean isDoneLoadingQuizInfo = false;
    private boolean isDoneLoadingQuizId = false;

    public static QuizTestCatalogFragment newInstance(String courseCode) {
        Bundle args = new Bundle();
        args.putString(COURSE_CODE, courseCode);
        QuizTestCatalogFragment fragment = new QuizTestCatalogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataRetrievalUser = new DataRetrievalUser(getContext());
        courseCode = getArguments().getString(COURSE_CODE).toLowerCase();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_view, container, false);
        mQuizList = new ArrayList<>();
        getDoneQuizId(courseCode, UserLab.get().getUserId());
        getQuiz(courseCode);
        mAdapter = new QuizListAdapter(getContext(), mQuizList);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    public void getQuiz(String courseCode) {
        mDataRetrievalUser.getQuizInfo(courseCode, new DataRetrievalUser.OnGetQuizListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List<Quiz> quizList) {
                mQuizList.addAll(quizList);
                isDoneLoadingQuizInfo = true;
                //isValidTestToDo();
                //mAdapter.notifyDataSetChanged();
                auxiliaryChecking();
            }

            @Override
            public void onFailed(FirebaseError firebaseError) {

            }
        });
    }

    public void getDoneQuizId(String courseCode, String userId) {
        mDataRetrievalUser.getDoneQuizId(courseCode, userId, new DataRetrievalUser.OnGetQuizIdListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List<String> doneQuizId) {
                doneQuiz = doneQuizId;
                isDoneLoadingQuizId = true;
                Log.i(TAG, Integer.toString(doneQuizId.size()));
                //isValidTestToDo();
                auxiliaryChecking();
            }

            @Override
            public void onFailed(FirebaseError firebaseError) {

            }
        });
    }

    private void isValidTestToDo(String quizId) {
                for (int i = 0; i < mQuizList.size(); i++) {
                    if (mQuizList.get(i).getId().contains(quizId)) {
                        mQuizList.remove(i);
                        return;
                    }
                }
    }


    private void auxiliaryChecking () {
        if (isDoneLoadingQuizInfo && isDoneLoadingQuizId) {
            for (int i = 0 ; i < doneQuiz.size(); i ++) {
                isValidTestToDo(doneQuiz.get(i));
            }
        }
        mAdapter.notifyDataSetChanged();
    }
    // 1,2,3,4,5
    // 1,2
}
