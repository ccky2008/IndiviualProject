package com.bignerdranch.android.fyp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.fyp.adapters.CommentAdapter;
import com.bignerdranch.android.fyp.models.CommentCollection;
import com.bignerdranch.android.fyp.models.CommentNew;
import com.bignerdranch.android.fyp.models.Post;
import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.network.DataRetrievalUser;
import com.bignerdranch.android.fyp.utils.CustomProgressDialog;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 12/25/2016.
 */

public class CommentListFragment extends Fragment {

    private static final String TAG = "CommentListFragment";
    private static final String ARG_POST = "com.bignerdranch.android.fyp.fragments.CommentListFragment.post";
    private RecyclerView mRecyclerView;
    private Post mPost;
    private List<CommentNew> mCommentList;
    private CommentCollection mCommentCollection;
    private CommentAdapter mCommentAdapter;
    private final String ADMIN_COMMENT = "admin";
    CustomProgressDialog mCustomProgressDialog;

    private View v;

    public static CommentListFragment newInstance(Post post) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_POST, post);
        CommentListFragment commentListFragment = new CommentListFragment();
        commentListFragment.setArguments(args);
        return commentListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPost = getArguments().getParcelable(ARG_POST);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.forum_fragment_list_comment, container, false);
        setPost();
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mCommentList = new ArrayList<>();
        mCommentAdapter = new CommentAdapter(getContext(), mCommentList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mCommentAdapter);

        load(mPost.getId());

        return v;
    }

    // Because a layout has a post so it will set the post layout out first
    public void setPost() {
        TextView topic = (TextView) v.findViewById(R.id.question_topic);
        TextView content = (TextView) v.findViewById(R.id.question_content);
        TextView author = (TextView) v.findViewById(R.id.question_author);
        TextView subject = (TextView) v.findViewById(R.id.question_subject);
        TextView comment_count = (TextView) v.findViewById(R.id.question_comment_count);

        topic.setText(mPost.getTitle());
        content.setText(mPost.getContent());
        author.setText(mPost.getAuthor());
        subject.setText(mPost.getSubject());
        comment_count.setText(Long.toString(mPost.getComment()));
    }

    public void load(String postId) {
        new DataRetrievalUser(getContext()).getPostComment(postId, new DataRetrievalUser.OnGetCommentListener() {
            @Override
            public void onStart() {
                mCustomProgressDialog = new CustomProgressDialog(getActivity());
                mCustomProgressDialog.show();
            }

            @Override
            public void onSuccess(List<CommentNew> commentList) {
                if(!commentList.isEmpty()) {
                    mCommentList.addAll(commentList);
                    mCommentAdapter.notifyDataSetChanged();
                }
                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailed(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forum_fragment_question_detail_user, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reply:
                CommentNewFragment commentNewFragment = CommentNewFragment.newInstance(mPost);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fContent, commentNewFragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            default:
                break;
        }
        return false;
    }

}
