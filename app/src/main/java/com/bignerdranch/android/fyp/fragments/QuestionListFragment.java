package com.bignerdranch.android.fyp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.fyp.adapters.PostAdapter;
import com.bignerdranch.android.fyp.models.Post;
import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.network.DataRetrievalUser;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Created by David on 12/1/2016.
 */

public class QuestionListFragment extends Fragment {

    private static final String TAG = "QuestionListFragment";
    private String keyIndex = "";
    ProgressBar mProgressBar;
    RecyclerView mRecyclerView;
    List<Post> posts;
    PostAdapter adapter;
    Context context;

    public static QuestionListFragment newInstance() {
        return new QuestionListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        clearBackStack();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forum_fragment_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar) ;
        posts = new ArrayList<>();

        adapter = new PostAdapter(getContext(), posts);
        adapter.setLoadMoreListener(new PostAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                /*
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "inside load more");
                        loadMore(keyIndex);
                    }
                });*/
            }
        });

        adapter.setOnItemClickListener(new PostAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Fragment fragment = new CommentListFragment().newInstance(posts.get(position));
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fContent, fragment)
                        .addToBackStack(null)
                        .commit();
            }
            @Override
            public void onItemLongClick(int position, View v) {
               //doNothing
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
        load();
        return v;
    }

    // First time loading
    private void load() {
        new DataRetrievalUser(getContext()).getPostInfo(new DataRetrievalUser.OnGetPostListener() {
            @Override
            public void onStart() {
                //Log.i(TAG, "Loading data starts");
            }

            @Override
            public void onSuccess(List<Post> postList) {
                mProgressBar.setVisibility(View.GONE);
                posts.addAll(postList);
                Collections.reverse(posts);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.forum_fragment_question_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_post:
                QuestionNewFragment questionNewFragment = QuestionNewFragment.newInstance();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fContent, questionNewFragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            default:
                break;
        }
        return false;
    }

    private void clearBackStack() {
        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    /*  want to implement dynamic loading */
}
