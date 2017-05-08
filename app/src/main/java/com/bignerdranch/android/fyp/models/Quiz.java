package com.bignerdranch.android.fyp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by David on 4/19/2017.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Quiz {
    private String id;
    private String author;
    private int easy;
    private int medium;
    private int hard;
    private String subject;
    private int totalNum;
    private String date;
    @PropertyName("start_time")
    private String startTime;
    @PropertyName("time_limit")
    private String timeLimit;
    @PropertyName("random_answer_sequence")
    private boolean randomAnswerSequence;
    @PropertyName("random_question_sequence")
    private boolean randomQuestionSequence;

    @JsonIgnoreProperties
    private List<Integer> questionNum;
    @JsonIgnoreProperties
    private List<Question> quizQuestionList;

    public Quiz() {}


    public List<Integer> getQuestionNum() {
        return questionNum;
    }

    public List<Question> getQuizQuestionList() {
        return quizQuestionList;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getEasy() {
        return easy;
    }

    public void setEasy(int easy) {
        this.easy = easy;
    }

    public int getMedium() {
        return medium;
    }

    public void setMedium(int medium) {
        this.medium = medium;
    }

    public int getHard() {
        return hard;
    }

    public void setHard(int hard) {
        this.hard = hard;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public boolean isRandomAnswerSequence() {
        return randomAnswerSequence;
    }

    @PropertyName("random_answer_sequence")
    public void setRandomAnswerSequence(boolean randomAnswerSequence) {
        this.randomAnswerSequence = randomAnswerSequence;
    }

    public boolean isRandomQuestionSequence() {
        return randomQuestionSequence;
    }

    @PropertyName("random_question_sequence")
    public void setRandomQuestionSequence(boolean randomQuestionSequence) {
        this.randomQuestionSequence = randomQuestionSequence;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    @PropertyName("time_limit")
    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    @JsonIgnore
    public void setQuestionNum(List<Integer> questionNum) {
        this.questionNum = questionNum;
    }

    @JsonIgnore
    public void setQuizQuestionList(List<Question> quizQuestionList) {
        this.quizQuestionList = quizQuestionList;
    }
}
