package com.bignerdranch.android.fyp.models;

/**
 * Created by David on 12/23/2016.
 */

public class Comment {
    private int id;
    private String comment;
    private int commentNum;
    private String date;
    private String userId;
    private int postId;

    public Comment() {

    }
    public Comment(int id, String comment, int commentNum, String date, String userId, int postId) {
        this.id = id;
        this.comment = comment;
        this.commentNum = commentNum;
        this.date = date;
        this.userId = userId;
        this.postId = postId;
    }
    public int getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
