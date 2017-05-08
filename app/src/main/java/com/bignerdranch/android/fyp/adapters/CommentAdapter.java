package com.bignerdranch.android.fyp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.models.Comment;
import com.bignerdranch.android.fyp.models.CommentCollection;
import com.bignerdranch.android.fyp.models.CommentNew;
import com.bignerdranch.android.fyp.models.Post;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by David on 3/11/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static Context context;
    List<CommentNew> commentList;
    private static final String TAG = "CommentAdapter";
    /*
    * isLoading - to set the remote loading and complete status to fix back to back load more call
    * isMoreDataAvailable - to set whether more data from server available or not.
    * It will prevent useless load more request even after all the server data loaded
    * */

    public CommentAdapter(Context context, List<CommentNew> commentList) {
        this.context = context;
        this.commentList = commentList;
        // Why the comment list is 0??????????????????
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new CommentAdapter.CommentHolder(inflater.inflate(R.layout.forum_list_item_comment, parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CommentAdapter.CommentHolder)holder).bindData(commentList.get(position), position);
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    /* VIEW HOLDERS */

    static class CommentHolder extends RecyclerView.ViewHolder{
        //Real UI
        //private RelativeLayout mQuestionBox;
        private TextView mCommentNumber;
        private TextView mAuthor;
        private TextView mDate;
        private TextView mContent;

        public CommentHolder(View itemView) {
            super(itemView);
            mCommentNumber = (TextView) itemView.findViewById(R.id.comment_Num);
            mAuthor = (TextView) itemView.findViewById(R.id.comment_user);
            mDate = (TextView) itemView.findViewById(R.id.comment_date);
            mContent = (TextView) itemView.findViewById(R.id.comment);
        }

        public void bindData(CommentNew comment, int position){

            mCommentNumber.setText(Integer.toString(position + 1));
            mAuthor.setText(comment.getAuthor());
            mContent.setText(comment.getComment());
            mDate.setText(comment.getDate());
        }
    }

    /*
    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }*/

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged(){
        notifyDataSetChanged();
    }

}
