package com.bignerdranch.android.fyp.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by David on 3/11/2017.
 */

public class CommentNew {

    String author;
    String comment;
    String date;

    public CommentNew() {

    }

    public CommentNew(String author, String comment) {
        this.author = author;
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("author", getAuthor());
        result.put("comment", getComment());
        result.put("date", getDate());
        return result;
    }



}
