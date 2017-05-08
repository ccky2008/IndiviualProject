package com.bignerdranch.android.fyp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bignerdranch.android.fyp.models.CommentNew;
import com.bignerdranch.android.fyp.models.Post;
import com.bignerdranch.android.fyp.network.DataSaving;
import com.bignerdranch.android.fyp.utils.GenericDate;
import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.utils.UserLab;

/**
 * Created by David on 12/23/2016.
 */

public class CommentNewFragment extends Fragment{

    private static final String TAG  = "CommentNewFragment";
    private static final String ARG_POST = "com.bignerdranch.android.fyp.question_object";
    private static final String ARG_FRAGMENT = "fragment";
    private boolean validInputComment = false;
    private EditText mCommentText;
    private Button mSubmitButton;
    private Post mPost;
    private CommentNew mComment;
    private DataSaving mDataSaving;

    public static CommentNewFragment newInstance(Post post) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_POST, post);
        CommentNewFragment commentNewFragment = new CommentNewFragment();
        commentNewFragment.setArguments(args);
        return commentNewFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPost = getArguments().getParcelable(ARG_POST);
        mComment = new CommentNew();
        mDataSaving = new DataSaving();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forum_fragment_new_comment, container, false);

        mCommentText = (EditText) v.findViewById(R.id.comment);
        mCommentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputText = mCommentText.getText().toString();
                if (inputText.length() >= 5) {
                  validInputComment = true;
                    mComment.setComment(mCommentText.getText().toString());}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSubmitButton = (Button) v.findViewById(R.id.comment_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validInputComment) {
                    mComment.setAuthor(UserLab.get().getUserId());
                    mComment.setDate(GenericDate.getToday());
                    mDataSaving.saveComment(mPost.getId(), mComment);
                    mDataSaving.updateCommentCount(mPost);
                    directToCommentList();
                } else {
                    //Log.i("Test", "InvalidComment");
                }
            }
        });
        return v;
    }

    private void directToCommentList() {
        CommentListFragment fragment = CommentListFragment.newInstance(mPost);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fContent, fragment)
                .commit();
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }
}
