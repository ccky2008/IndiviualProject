package com.bignerdranch.android.fyp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 3/5/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post implements Parcelable {
    private String id;
    private String title;
    private String author;
    private String content;
    private String subject;
    private boolean isSolve;
    private String date;
    private long comment;


    public  Post() {
        this.isSolve = false;
    }

    public Post(String title, String author, String content, String subject, String date) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.subject = subject;
        this.isSolve = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSolve() {
        return isSolve;
    }

    public void setSolve(boolean solve) {
        isSolve = solve;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getComment() {
        return comment;
    }

    public void setComment(long comment) {
        this.comment = comment;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(author);
        out.writeString(content);
        out.writeString(subject);
        out.writeString(date);
        out.writeValue(isSolve);
    }

    private Post (Parcel in) {
        this.title = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.subject = in.readString();
        this.date = in.readString();
        this.isSolve = (Boolean)in.readValue(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };


}

