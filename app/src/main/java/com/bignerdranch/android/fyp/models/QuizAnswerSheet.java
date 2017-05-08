package com.bignerdranch.android.fyp.models;

/**
 * Created by David on 4/22/2017.
 */

public class QuizAnswerSheet {
    String studentId;
    String date;
    boolean isMarked;
    String id;
    long score;

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public QuizAnswerSheet() {

    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Two List for right or wrong and student Answer

}
