package com.bignerdranch.android.fyp.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.List;

/**
 * Created by David on 4/19/2017.
 */

@IgnoreExtraProperties
public class MultipleQuestion extends QuizQuestion {

    private List<String> answer;

    @PropertyName("numOfAnswer")
    private List<String> numOfAnswer;

    public MultipleQuestion() {
        super();
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public List<String> getNumOfAnswer() {
        return numOfAnswer;
    }

    public void setNumOfAnswer(List<String> numOfAnswer) {
        this.numOfAnswer = numOfAnswer;
    }
}
