package com.bignerdranch.android.fyp.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.List;

/**
 * Created by David on 4/19/2017.
 */
public class SingleQuestion extends QuizQuestion {
    private String answer;

    private List<String> numOfAnswer;

    public SingleQuestion() {
        super();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getNumOfAnswer() {
        return numOfAnswer;
    }

    public void setNumOfAnswer(List<String> numOfAnswer) {
        this.numOfAnswer = numOfAnswer;
    }
}
