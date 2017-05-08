package com.bignerdranch.android.fyp.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.bignerdranch.android.fyp.utils.GenericDate;
import com.bignerdranch.android.fyp.R;

import com.bignerdranch.android.fyp.utils.SharedMethod;
import com.bignerdranch.android.fyp.fragments.AlertDialogFragment;
import com.bignerdranch.android.fyp.fragments.QuizQuestionFragment;
import com.bignerdranch.android.fyp.models.QuizAnswerSheet;
import com.bignerdranch.android.fyp.network.DataSaving;
import com.bignerdranch.android.fyp.utils.UserLab;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David on 4/19/2017.
 */

public class QuizQuestionActivity extends AppCompatActivity implements AlertDialogFragment.OnDataPassForLogout{

    private static final String DIALOG_SUBMIT = "submit";
    private static final String TAG = "QuizQuestion";
    private final String TIMER = "timer";
    private List<Integer> questionNum;
    private String endTime;
    private String quizId;
    @BindView(R.id.quiz_timer)
    public TextView timer;
    private String courseCode = null;
    private String timeLimit = null;
    private String answerSheetKey = null;
    private List<Integer> randomQuestionSequence;
    private DataSaving mDataSaving;
    private CountDownTimer countDownTimer;


    private void createCounter() {
        countDownTimer = new CountDownTimer(SharedMethod.compareTimeMilli(endTime), 1000) {

            public void onTick(long millis) {
                int seconds = (int) (millis / 1000) % 60;
                int minutes = (int) ((millis / (1000 * 60)) % 60);

                //long seconds = TimeUnit.MILLISECONDS.toSeconds(second);
                timer.setText(String.format("%02d:%02d", minutes, seconds));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                timer.setText("done!");
                FragmentManager manager = getSupportFragmentManager();
                AlertDialogFragment dialog = AlertDialogFragment.newInstance(TIMER, 12);
                dialog.show(manager, TIMER);
                updateAnswerSheet();
                finish();
            }
        }.start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_question_header);
        ButterKnife.bind(this);

        mDataSaving = new DataSaving();
        timeLimit = getIntent().getStringExtra("TIMER");
        courseCode = getIntent().getStringExtra("COURSE_CODE");
        questionNum = getIntent().getIntegerArrayListExtra("LIST");
        endTime = getIntent().getStringExtra("EndTime");
        quizId = getIntent().getStringExtra("QuizId");
        randomQuestionSequence = SharedMethod.randomList(questionNum);

        saveStudentAnswerSheet();
        initAnswerSheet();
        initHeader();
        initViewPager();
        createCounter();
    }


    private void initAnswerSheet() {
        if ((UserLab.get().getIsCorrectAnswer() == null) || (UserLab.get().getStudentAnswer() == null)) {
            LinkedHashMap<String, String> studentAnswer = new LinkedHashMap<>();
            LinkedHashMap<String, Boolean> isCorrectAnswer = new LinkedHashMap<>();
            for(int i = 0; i < randomQuestionSequence.size(); i++) {
                Log.i("Quiz", Integer.toString(randomQuestionSequence.get(i)));
                studentAnswer.put(Integer.toString(randomQuestionSequence.get(i)), "false");
                isCorrectAnswer.put(Integer.toString(randomQuestionSequence.get(i)), false);
            }
            UserLab.get().setStudentAnswer(studentAnswer);
            UserLab.get().setIsCorrectAnswer(isCorrectAnswer);
        }
    }

    private void initHeader() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.quiz_header);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Timer start
    }

    private void initViewPager() {
        FragmentPagerItems pages = new FragmentPagerItems(this);

        for (int i = 0 ; i < randomQuestionSequence.size() ; i++) {
            Bundle args = new Bundle();
            args.putString("COURSE_CODE", courseCode);
            args.putInt("LIST", questionNum.get(i));
            args.putInt("NUMBER", i+1);
            pages.add(FragmentPagerItem.of(getString(R.string.quiz_question), QuizQuestionFragment.class, args));
        }

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), pages);

        ViewPager viewPager = (ViewPager) findViewById(R.id.quiz_viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(questionNum.size() - 1);
        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               // Log.i(TAG, "On page scrolled");
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "On page selected" + Integer.toString(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "OnStop is being called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserLab.get().setIsCorrectAnswer(null);
        UserLab.get().setStudentAnswer(null);
        countDownTimer.cancel();
        Log.i(TAG, "On destro is called");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.quiz_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submit_quiz:
                FragmentManager manager = getSupportFragmentManager();
                AlertDialogFragment dialog = AlertDialogFragment.newInstance("Submission", 11) ;
                dialog.show(manager, DIALOG_SUBMIT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveStudentAnswerSheet() {
        QuizAnswerSheet answerSheet = new QuizAnswerSheet();
        answerSheet.setMarked(false);
        answerSheet.setId(quizId);
        answerSheet.setStudentId(UserLab.get().getUserId());
        answerSheet.setDate(GenericDate.getTodayForRollCall());
        answerSheetKey = mDataSaving.saveStudentAnswerSheet(answerSheet, courseCode, questionNum.size(), randomQuestionSequence);
    }

    @Override
    public void onDataPassForLogout(Boolean logout) {
        if (logout) {
            //QueryPreferences.setStoredHasLoggedIn(this, false);
            updateAnswerSheet();
            finish();
        }
    }

    private void updateAnswerSheet() {
        LinkedHashMap<String, String> mapStudentAnswer = UserLab.get().getStudentAnswer();
        LinkedHashMap<String, Boolean> mapCorrectAnswer = UserLab.get().getIsCorrectAnswer();
        List<String> studentAnswer = new ArrayList<>();
        List<Boolean> correctAnswer = new ArrayList<>();
        for(Map.Entry<String,String> entry : mapStudentAnswer.entrySet()) {
            studentAnswer.add(entry.getValue());
        }

        for(Map.Entry<String,Boolean> entry : mapCorrectAnswer.entrySet()) {
          correctAnswer.add(entry.getValue());
        }

        mDataSaving.updateStudentAnswerSheet(courseCode, answerSheetKey, studentAnswer);
        mDataSaving.updateCorrectAnswer(courseCode, answerSheetKey, correctAnswer);

    }

}
