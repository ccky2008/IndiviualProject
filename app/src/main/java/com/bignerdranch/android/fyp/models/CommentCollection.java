package com.bignerdranch.android.fyp.models;

import java.util.List;

/**
 * Created by David on 3/11/2017.
 */

public class CommentCollection {

    private String postid;
    private List<CommentNew> comment;

    public CommentCollection() {

    }

    public CommentCollection(String id, List<CommentNew> comment) {
        this.postid = id;
        this.comment = comment;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public List<CommentNew> getComment() {
        return comment;
    }

    public void setComments(List<CommentNew> comment) {
        this.comment = comment;
    }
}
