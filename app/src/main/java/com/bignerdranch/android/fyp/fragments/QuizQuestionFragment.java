package com.bignerdranch.android.fyp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.utils.SharedMethod;
import com.bignerdranch.android.fyp.models.MultipleQuestion;
import com.bignerdranch.android.fyp.models.OpenQuestion;
import com.bignerdranch.android.fyp.models.QuizQuestion;
import com.bignerdranch.android.fyp.models.SingleQuestion;
import com.bignerdranch.android.fyp.network.DataRetrievalUser;
import com.bignerdranch.android.fyp.utils.UserLab;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David on 4/19/2017.
 */

public class QuizQuestionFragment extends Fragment{

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private static final String GREY_BUTTON_TAG = "GREY";
    private static final String BLUE_BUTTON_TAG = "BLUE";
    private static final String TAG = "QuizPerQuestion";
    private QuizQuestion mQuizQuestion;
    private static final String SINGLE = "Single";
    private static final String MULTIPLE = "Multiple";
    private static final String OPEN = "Open";
    private LinearLayout mRoot;
    private OpenQuestion mOpenQuestion = null;
    private MultipleQuestion mMultipleQuestion = null;
    private SingleQuestion mSingleQuestion = null;
    private String courseCode = null;
    private List<Boolean> completedAnswerList = new ArrayList<>();
    private List<String> answerChoices = new ArrayList<>();
    private int questionNum;
    private boolean isFirstTime = true;
    private boolean isInvisible = false;

    private int number;
    //thsi is for multiple answer
    private int multipleQuestionNumberOfAnswer = 0;
    private List<Integer> answerListForMultiple;

    // This is for single answer
    private int singleAnswer;

    //This is for open answer;
    private String openAnswer = "null";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.quiz_question_per_page, container, false);
        ButterKnife.bind(this, v);
        mRoot = (LinearLayout) v.findViewById(R.id.quiz_question_container);
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFirstTime) {
            questionNum = getArguments().getInt("LIST");
            getQuestionInfo(getArguments().getInt("LIST"), getArguments().getString("COURSE_CODE").toLowerCase());
            number = getArguments().getInt("NUMBER");
            isFirstTime = false;
        } else {
            if (isInvisible) {
                Log.i(TAG, "The page is leaving");
                if (mSingleQuestion!= null) {
                    saveSingleQuestion(singleAnswer);
                } else if (mMultipleQuestion != null){
                    saveMultipleAnswerToLab();
                } else {
                    saveOpenAnswer();
                }
            } else {
                isInvisible = true;
            }

        }
    }

    private void getQuestionInfo(int index, String subject) {
        new DataRetrievalUser(getContext()).getQuizQuestion(subject, index, new DataRetrievalUser.OnGetQuizQuestionListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(QuizQuestion question) {
                mQuizQuestion = question;
                progressBar.setVisibility(View.GONE);
                defineObjectType(question);
                findView(number);
            }

            @Override
            public void onFailed(FirebaseError firebaseError) {

            }
        });
    }

    private void findView(int number) {
        if (mQuizQuestion.getType().equalsIgnoreCase(OPEN)) {
            View child = getActivity().getLayoutInflater().inflate(R.layout.quiz_open_question, null);
            mRoot.addView(child);
            initOpenQuestionUI(child);
        } else {
            View child = getActivity().getLayoutInflater().inflate(R.layout.quiz_multiple_choice_question, null);
            mRoot.addView(child);
            initMultipleChoiceQuestionUI(child);
        }
    }

    private void defineObjectType(QuizQuestion question) {
        if(question.getType().equalsIgnoreCase(OPEN)) {
            mOpenQuestion = (OpenQuestion) question;
        } else if (question.getType().equalsIgnoreCase(SINGLE)) {
            mSingleQuestion = (SingleQuestion) question;
        } else {
            mMultipleQuestion = (MultipleQuestion) question;
        }
    }

    private void initOpenQuestionUI(View child) {
        TextView question = (TextView) child.findViewById(R.id.quiz_question);
        EditText answerInput = (EditText) child.findViewById(R.id.open_question_answer);
        question.setText(getActivity().getString(R.string.quiz_question_text, number, mOpenQuestion.getQuestion()));
        answerInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    openAnswer = v.getText().toString().trim();
                    Toast.makeText(getContext(), "Your answer is saved", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }

    private void initMultipleChoiceQuestionUI(View child) {
        LinearLayout layout = (LinearLayout) child.findViewById(R.id.multiple_choice_question_container);
        TextView question = (TextView) child.findViewById(R.id.quiz_question);

        if (mSingleQuestion != null) {
            //Log.i(TAG,"Single Quesiton");
            answerChoices = mSingleQuestion.getNumOfAnswer();
            initCompletedAnswerList();
            if (completedAnswerList.size() == answerChoices.size()) {
                fillInCompletedList(mSingleQuestion.getAnswer());
                randomTwoList();
            }

            question.setText(getActivity().getString(R.string.quiz_question_text, number, mSingleQuestion.getQuestion()));
            createButton(layout, true);
        } else {
            //Log.i(TAG,"Multiple Quesiton");
            answerChoices = mMultipleQuestion.getNumOfAnswer();
            multipleQuestionNumberOfAnswer = mMultipleQuestion.getAnswer().size();
            answerListForMultiple = new ArrayList<>();
           // Log.i(TAG, "Multiple Answer is " + Integer.toString(countMultipleAnswer));
            initCompletedAnswerList();
            if (completedAnswerList.size() == answerChoices.size()) {
                fillInCompletedList(mMultipleQuestion.getAnswer());
                randomTwoList();
            }

            question.setText(getActivity().getString(R.string.quiz_question_text, number, mMultipleQuestion.getQuestion()));
            createButton(layout, false);
        }
    }

    private void createButton(final LinearLayout child, final boolean isSingleAnswer) {
        for(int i = 0; i < answerChoices.size(); i++) {
            final Button btn = new Button(getContext());
            btn.setId(i);
            btn.setText(answerChoices.get(i));
            btn.setBackgroundResource(R.drawable.buttonshape);
            btn.setTag(GREY_BUTTON_TAG);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 10, 0, 10);
            child.addView(btn, lp);
            final int id_ = btn.getId();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSingleAnswer) {
                        for (int i = 0; i < answerChoices.size(); i++) {
                            Button btnById = (Button) child.findViewById(i);
                            btnById.setBackgroundResource(R.drawable.buttonshape);
                            btnById.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
                        }
                        v.setBackgroundResource(R.drawable.buttonshapeonclick);
                        TextView text = (TextView) v;
                        text.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                        singleAnswer = id_;
                        Log.i(TAG, "The user single answer : " + Integer.toString(singleAnswer));
                    } else {
                        TextView text = (TextView) v;
                        if (v.getTag() == GREY_BUTTON_TAG) {
                            v.setTag(BLUE_BUTTON_TAG);
                            v.setBackgroundResource(R.drawable.buttonshapeonclick);
                            text.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                            addAnswerToList(id_);
                        } else {
                            v.setTag(GREY_BUTTON_TAG);
                            v.setBackgroundResource(R.drawable.buttonshape);
                            text.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
                            removeAnswerFromList(id_);
                        }
                    }
                }
            });

        }
    }

    private void initCompletedAnswerList() {
        for (int i = 0;i < answerChoices.size() ; i++) {
            completedAnswerList.add(false);
        }
    }

    private void fillInCompletedList(String x) {
       int rightLocation = SharedMethod.findCorrectAnswer(x);
        completedAnswerList.set(rightLocation, true);
    }

    private void fillInCompletedList(List<String> x) {
        for(int i = 0; i < x.size(); i++) {
            int rightLocation = SharedMethod.findCorrectAnswer(x.get(i));
            completedAnswerList.set(rightLocation, true);
        }
    }

    private void randomTwoList() {
        long seed = System.nanoTime();
        Collections.shuffle(answerChoices, new Random(seed));
        Collections.shuffle(completedAnswerList, new Random(seed));
    }

    private void saveSingleQuestion(int index) {
        String oneAnswer = answerChoices.get(index);
        boolean isCorrect = completedAnswerList.get(index);
        Log.i(TAG, "Student answer : " + oneAnswer + "IsRight :" + Boolean.valueOf(isCorrect));
        LinkedHashMap<String, String> studentAnswer = UserLab.get().getStudentAnswer();
        LinkedHashMap<String, Boolean> isCorrectAnswer = UserLab.get().getIsCorrectAnswer();

        String num = Integer.toString(questionNum);
        Log.i(TAG, "The question number is :" + num );

        for (Map.Entry<String,String> entry : studentAnswer.entrySet()) {
            Log.i(TAG, "The key is :" + entry.getKey());
            if (entry.getKey().equalsIgnoreCase(num)) {
                entry.setValue(oneAnswer);
            }
        }
        if (isCorrect) {
            for(Map.Entry<String, Boolean> entry: isCorrectAnswer.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(num)) {
                    entry.setValue(true);
                }
            }
        }
        saveAnswer(studentAnswer, isCorrectAnswer);
    }

    private void saveOpenAnswer() {
        HashMap<String, String> studentAnswer = UserLab.get().getStudentAnswer();
        String num = Integer.toString(questionNum);
        for (Map.Entry<String,String> entry : studentAnswer.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(num)) {
                entry.setValue(openAnswer);
            }
        }
    }

    private void saveAnswer(LinkedHashMap<String, String> student, LinkedHashMap<String, Boolean> isCorrect) {
        UserLab.get().setStudentAnswer(student);
        UserLab.get().setIsCorrectAnswer(isCorrect);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    // This is for Multiple answer question

    private void saveMultipleAnswerToLab() {
        if (answerListForMultiple.size() == mMultipleQuestion.getAnswer().size()) {
            int count = 0;
            for(int i = 0; i < answerListForMultiple.size(); i++) {
                boolean isRightAnswer = completedAnswerList.get(answerListForMultiple.get(i));
                Log.i(TAG, "Multiple Question boolean value is " + Boolean.valueOf(isRightAnswer));
                if (isRightAnswer) count++;
            }
            Log.i(TAG, "The count is  :" + Integer.toString(mMultipleQuestion.getAnswer().size()));
            Log.i(TAG, "The count is  :" + Integer.toString(count));
            if (count == mMultipleQuestion.getAnswer().size()) {
                Log.i(TAG, "Multiple is executed");
                saveCorrectAnswer(questionNum);
                saveStudentAnswer(questionNum);
            } else {
                saveStudentAnswer(questionNum);
            }

        } else {
            saveStudentAnswer(questionNum);
        }
    }

    private void saveStudentAnswer(int index) {
        LinkedHashMap<String, String> studentAnswer = UserLab.get().getStudentAnswer();
        String num = Integer.toString(index);
        String answerTotal = "";
        for(int i = 0 ;i < answerListForMultiple.size() ; i++) {
            String x = answerChoices.get(answerListForMultiple.get(i));
            if ( i == answerListForMultiple.size() - 1) {
                answerTotal += x;
            } else {
                answerTotal += x + ",";
            }
        }

        for (Map.Entry<String,String> entry : studentAnswer.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(num)) {
                entry.setValue(answerTotal);
                UserLab.get().setStudentAnswer(studentAnswer);
                Log.i(TAG, "fINISH execute");
            }
        }
    }

    private void saveCorrectAnswer(int index) {
        String num = Integer.toString(index);
        LinkedHashMap<String, Boolean> isCorrectAnswer = UserLab.get().getIsCorrectAnswer();
        for(Map.Entry<String, Boolean> entry: isCorrectAnswer.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(num)) {
                entry.setValue(true);
                UserLab.get().setIsCorrectAnswer(isCorrectAnswer);
            }
        }
    }

    private void addAnswerToList(int x) {
        if (answerListForMultiple.contains(x)) {
            return;
        } else {
            answerListForMultiple.add(x);
            Log.i(TAG, "after adding element " + Integer.toString(answerListForMultiple.size()));
            return;
        }
    }

    private void removeAnswerFromList(int x) {
        if (answerListForMultiple.contains(x)) {
            answerListForMultiple.remove(Integer.valueOf(x));
            Log.i(TAG, "after removing element" + Integer.toString(answerListForMultiple.size()));
            return;
        } else {
            return;
        }
    }

    //-----------------------------
}
