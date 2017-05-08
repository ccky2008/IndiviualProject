package com.bignerdranch.android.fyp.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.fyp.utils.GenericDate;
import com.bignerdranch.android.fyp.R;
import com.bignerdranch.android.fyp.models.Post;

import java.util.List;

/**
 * Created by David on 3/11/2017.
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;

    private static ClickListener clickListener;
    static Context context;
    List<Post> posts;
    public OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    private String load = "load";

    /*
    * isLoading - to set the remote loading and complete status to fix back to back load more call
    * isMoreDataAvailable - to set whether more data from server available or not.
    * It will prevent useless load more request even after all the server data loaded
    * */

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
           return new PostHolder(inflater.inflate(R.layout.forum_list_item_question,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load_indicator,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if(getItemViewType(position)==TYPE_MOVIE){
            ((PostHolder)holder).bindData(posts.get(position));
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {
        if(posts.get(position).getAuthor() == load){
            return TYPE_LOAD;
        }else{
            return TYPE_MOVIE;
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    /* VIEW HOLDERS */

    public static class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Real UI
        //private RelativeLayout mQuestionBox;
        private TextView mTitle;
        private TextView mAuthor;
        private TextView mSubject;
        private TextView mDate;
        private TextView mComment_count;


        public PostHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mDate = (TextView) itemView.findViewById(R.id.question_date);
            mTitle = (TextView) itemView.findViewById(R.id.question_title);
            mAuthor = (TextView) itemView.findViewById(R.id.question_author);
            mSubject = (TextView) itemView.findViewById(R.id.question_subject);
            mComment_count = (TextView) itemView.findViewById(R.id.question_comment_count);
        }

        void bindData(Post post){
            mDate.setText(GenericDate.findTwoDaysDistance(post.getDate()));
            mTitle.setText(post.getTitle());
            mAuthor.setText(post.getAuthor());
            mSubject.setText(post.getSubject());
            mComment_count.setText(String.format("%d", post.getComment()));
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        PostAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

}
