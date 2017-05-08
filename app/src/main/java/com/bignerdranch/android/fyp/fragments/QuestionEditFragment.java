package com.bignerdranch.android.fyp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.bignerdranch.android.fyp.network.BackgroundWorker;
import com.bignerdranch.android.fyp.activities.MainPageActivity;
import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.models.Question;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 12/22/2016.
 */

public class QuestionEditFragment extends Fragment
            implements BackgroundWorker.AsyncResponse, AdapterView.OnItemSelectedListener {

    private static final String ARG_FRAGMENT = "fragment";
    private static final String ARG_QUESTION = "com.bignerdranch.android.fyp.question_object";

    private Question mQuestion;
    private EditText mQuestionTitle;
    private EditText mQuestionContent;
    private Spinner mQuestionSubject;
    private Button mSubmitButton;
    String oldTitle ;
    String newTitle;
    String oldContent;
    String newContent;
    @Override
    public void processFinish(String result){
        if (isInsertSuccess(result)) {
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Intent intent = new Intent(getActivity(), MainPageActivity.class);
            startActivity(intent);
        } else {
            getActivity().onBackPressed();
        }
    }

    private boolean isInsertSuccess(String output) {
        if (output.equalsIgnoreCase("true")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        mQuestion = bundle.getParcelable(ARG_QUESTION);
        Log.i("Update", "The update id is " + Integer.toString(mQuestion.getID()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forum_fragment_new_question, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mQuestionTitle = (EditText) view.findViewById(R.id.question_title);
        mQuestionTitle.setText(mQuestion.getTitle());

        oldTitle = mQuestion.getTitle();
        newTitle = setQuestionTitle();

        mQuestionContent = (EditText) view.findViewById(R.id.question_content);
        mQuestionContent.setText(mQuestion.getContent());

        oldContent = mQuestion.getContent();
        newContent = setQuestionContent();
        mQuestionSubject = (Spinner) view.findViewById(R.id.question_subject);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.subject, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mQuestionSubject.setAdapter(adapter);
        setDefaultOptionSubject();

        mSubmitButton = (Button) view.findViewById(R.id.question_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(isEdited(oldTitle, newTitle, oldContent, newContent))) {
                    updateDate();
                    updateToMySQL();
                    //int numberOfFragment = getFragmentManager().getBackStackEntryCount();
                }else{
                    getActivity().onBackPressed();
                    Log.i("Test", "Not  Chnage");
                }
            }
        });
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    private void setDefaultOptionSubject() {
        String subject = mQuestion.getSubject();
        switch (subject) {
            case "Chinese":
                mQuestionSubject.setSelection(0);
                break;
            case "English":
                mQuestionSubject.setSelection(1);
                break;
            case "Math":
                mQuestionSubject.setSelection(2);
                break;
            case "Others":
                mQuestionSubject.setSelection(3);
                break;
            default:
                mQuestionSubject.setSelection(3);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id) {
        String item = parent.getItemAtPosition(position).toString(); // get the subject Name
        int x = parent.getSelectedItemPosition(); // get the number
        mQuestion.setSubject(item);

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    private boolean isEdited(String oldTitle, String newTitle, String oldContent, String newContent) {
        if (oldTitle.equals(newTitle) && oldContent.equals(newContent)) {

            //Log.i("Test", oldTitle + "       " + newTitle + "          " +oldContent +"        "+ newContent);
            return false;
        }  else {
            return true;
        }
    }


    private String setQuestionTitle() {

        mQuestionTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               mQuestion.setTitle(mQuestionTitle.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return mQuestion.getTitle();
    }


    private String setQuestionContent() {
        mQuestionContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mQuestion.setContent(mQuestionContent.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return mQuestion.getContent();
    }

    private void updateDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String newDate = dateFormat.format(new Date());
        mQuestion.setDate(newDate);
    }

    private void updateToMySQL() {
        String type = "updateQuestion";
        String id = Integer.toString(mQuestion.getID());
        String title = mQuestion.getTitle();
        String content = mQuestion.getContent();
        String subject = mQuestion.getSubject();
        String date = mQuestion.getDate();
        String[] paras = {type, id, title, content, subject, date};
        BackgroundWorker backgroundworker = new BackgroundWorker(this);
        backgroundworker.execute(paras);
    }

}
