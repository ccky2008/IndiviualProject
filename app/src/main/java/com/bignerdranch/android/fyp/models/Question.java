package com.bignerdranch.android.fyp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 11/16/2016.
 */
public class Question implements Parcelable {
    private int mID;
    private String mTitle;
    private String mContent;
    private String mDate;
    private boolean mSolved;
    private String mSubject;
    private String mAuthor;
    private int mCommentCount;

    public Question() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        mDate = dateFormat.format(new Date());
        mSolved = false;
    }

    public Question(int mID, String mAuthor, String mTitle, String mContent, String mSubject, String mDate, int mCommentCount, String mSolved) {
        this.mID = mID;
        this.mAuthor = mAuthor;
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mSubject = mSubject;
        this.mDate = mDate;
        this.mCommentCount = mCommentCount;
        this.mSolved = Boolean.valueOf(mSolved);
    }

    private Question (Parcel in) {
        this.mID = in.readInt();
        this.mAuthor = in.readString();
        this.mTitle = in.readString();
        this.mContent = in.readString();
        this.mSubject = in.readString();
        this.mDate = in.readString();
        this.mCommentCount = in.readInt();
        this.mSolved = Boolean.valueOf(in.readString());
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mID);
        out.writeString(mAuthor);
        out.writeString(mTitle);
        out.writeString(mContent);
        out.writeString(mSubject);
        out.writeString(mDate);
        out.writeInt(mCommentCount);
        String mSolve = String.valueOf(mSolved);
        out.writeString(mSolve);
    }

    // Just cut and paste this for now
    public int describeContents() {
        return 0;
    }

    // Just cut and paste this for now
    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(int mCommentCount) {
        this.mCommentCount = mCommentCount;
    }
}
