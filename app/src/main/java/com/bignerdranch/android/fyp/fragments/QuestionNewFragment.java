package com.bignerdranch.android.fyp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import com.bignerdranch.android.fyp.utils.GenericDate;
import com.bignerdranch.android.fyp.models.Post;
import com.bignerdranch.android.fyp.network.BackgroundWorker;
import com.bignerdranch.android.fyp.network.DataSaving;
import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.utils.UserLab;

/**
 * Created by David on 11/16/2016.
 */
public class QuestionNewFragment extends Fragment
        implements OnItemSelectedListener, BackgroundWorker.AsyncResponse {

    private Post mPost;
    private EditText mTitleField;
    private EditText mContentField;
    private Button mSubmitButton;
    private Spinner mSubjectSpinner;

    public static QuestionNewFragment newInstance() {
        return new QuestionNewFragment();
    }

    @Override
    public void processFinish(String output){
        getFragmentManager().popBackStack(null, getFragmentManager().POP_BACK_STACK_INCLUSIVE);
        QuestionListFragment questionListFragment = new QuestionListFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fContent, questionListFragment)
                .commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        mPost = new Post();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forum_fragment_new_question, container, false);
        //Toolbar  toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        //AppCompatActivity activity = (AppCompatActivity) getActivity();
        //activity.setSupportActionBar(toolbar);
        //activity.getSupportActionBar().setHomeButtonEnabled(true);
        //activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTitleField = (EditText) v.findViewById(R.id.question_title);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPost.setTitle(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mContentField = (EditText)v.findViewById(R.id.question_content);
        mContentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPost.setContent(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSubjectSpinner = (Spinner) v.findViewById(R.id.question_subject);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount()));
                    //"Hint to be displayed"
                }
                return  v;
            }

            @Override
            public int getCount() {
                return super.getCount() -1; //  dont display last item. It is used as hint
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Chinese");
        adapter.add("English");
        adapter.add("Math ");
        adapter.add("Others");
        adapter.add("Subject"); // hint to display
        mSubjectSpinner.setAdapter(adapter);
        mSubjectSpinner.setSelection(adapter.getCount()); //set the hint the default selection so it appears on launch
        mSubjectSpinner.setOnItemSelectedListener(this);

        mSubmitButton = (Button) v.findViewById(R.id.question_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                backToQuestionListFragment();
                //insertToMYSQL();
            }
        });
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id) {

       String item = parent.getItemAtPosition(position).toString();
        int x = parent.getSelectedItemPosition(); // get the number
        //security check
        mPost.setSubject(item);

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    private void saveData() {
        mPost.setAuthor(UserLab.get().getUserId());
        mPost.setDate(GenericDate.getTodayDayandTime());
        DataSaving dataSaving = new DataSaving();
        dataSaving.saveDataToFirebase(mPost);
    }

    private void backToQuestionListFragment() {
        QuestionListFragment questionListFragment = QuestionListFragment.newInstance();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fContent, questionListFragment)
                .commit();
    }

}
