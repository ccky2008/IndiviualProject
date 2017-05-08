package com.bignerdranch.android.fyp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bignerdranch.android.fyp.utils.QueryPreferences;
import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.fragments.CommentListFragment;
import com.bignerdranch.android.fyp.fragments.CommentNewFragment;
import com.bignerdranch.android.fyp.fragments.QuestionEditFragment;
import com.bignerdranch.android.fyp.fragments.QuestionViewPagerFragment;
import com.bignerdranch.android.fyp.models.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 12/19/2016.
 */
                                         //FragmentActivity
public class QuestionViewPagerActivity extends AppCompatActivity
          implements QuestionViewPagerFragment.OnDataQuestionPass{

    private static final String EXTRA_QUESTION_ID = "com.bignerdranch.android.fyp.question_id";
    private static final String EXTRA_QUESTION_LIST = "com.bignerdranch.android.fyp.question_list";
    private static final String ARG_QUESTION = "com.bignerdranch.android.fyp.question_object";

    private static final int DEFAULT_QUESTION_ID = 2;
    CommentListFragment mCommentListFragment;
    QuestionViewPagerFragment mQuestionViewPagerFragment;
    private Question mQuestion;
    //private List<Question> mQuestionListItem = new ArrayList<Question>();


    public static Intent newIntent(Context packageContext, ArrayList<Question> questions, int questionId) {
        Intent intent = new Intent(packageContext, QuestionViewPagerActivity.class);
        intent.putExtra(EXTRA_QUESTION_ID, questionId);
        intent.putParcelableArrayListExtra(EXTRA_QUESTION_LIST, questions);
        return intent;
    }

    @Override
    public void onDataQuestionPass(Question question) {
        mQuestion = question;
    }

    // What you gonna to do?
    // The questionList is passed to this activity. Than you need to pass it to the QuestionViewPagerFragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_activity_question_pager);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initFragment();
    }

    private void initFragment() {
        mQuestionViewPagerFragment = new QuestionViewPagerFragment();
        mCommentListFragment = new CommentListFragment();
        setContentToViewPagerFragment(mQuestionViewPagerFragment);
        setContentToCommentFragment(mCommentListFragment);
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.questionContent, mQuestionViewPagerFragment);
        transaction.add(R.id.commentContent, mCommentListFragment);
        transaction.commit();
    }

    private void setContentToViewPagerFragment(Fragment questionViewPagerFragment) {
        int questionId = getIntent().getIntExtra(EXTRA_QUESTION_ID,DEFAULT_QUESTION_ID);
        List<Question> mQuestionsList = getIntent().getParcelableArrayListExtra(EXTRA_QUESTION_LIST);
        Log.i("questionId", Integer.toString(questionId));
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_QUESTION_ID, questionId);
        bundle.putParcelableArrayList(EXTRA_QUESTION_LIST, (ArrayList)mQuestionsList);
        questionViewPagerFragment.setArguments(bundle);
    }

    private void setContentToCommentFragment(Fragment commentListFragment) {
        int questionId = getIntent().getIntExtra(EXTRA_QUESTION_ID,DEFAULT_QUESTION_ID);
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_QUESTION_ID, questionId);
        commentListFragment.setArguments(bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isAuthor()) {
            getMenuInflater().inflate(R.menu.forum_fragment_question_detail_author, menu);
        } else {
            getMenuInflater().inflate(R.menu.forum_fragment_question_detail_user, menu);
        }
        // Configure the search info and add any event listeners...
        return super.onCreateOptionsMenu(menu);
    }

    private boolean isAuthor() {
        String userName = QueryPreferences.getStoredEmail(getApplicationContext());
        if (userName.equals(mQuestion.getAuthor())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i("tEST", "cLICK");
                onBackPressed();
                return true;
            case R.id.action_edit:
                QuestionEditFragment questionEditFragment = new QuestionEditFragment();
                sendDataToCommentFragment(questionEditFragment);
                setNewFragment(questionEditFragment);
                return true;
            case R.id.action_reply:
                CommentNewFragment commentNewFragment = new CommentNewFragment();
                sendDataToCommentFragment(commentNewFragment);
                setNewFragment(commentNewFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendDataToCommentFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_QUESTION, mQuestion);
        fragment.setArguments(bundle);
    }

    private void setNewFragment(Fragment fragment) {
       // Bundle bundle = new Bundle();
        //bundle.putParcelable(ARG_QUESTION, mQuestion);
       // CommentListFragment commentListFragment = new CommentListFragment();
       // fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.wholeContent, fragment)
                .addToBackStack(null)
                .commit();
    }
}
