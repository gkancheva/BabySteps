package com.company.babysteps.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.babysteps.R;
import com.company.babysteps.entities.Post;
import com.company.babysteps.services.PostClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostsRVAdapter extends RecyclerView.Adapter<PostsRVAdapter.PostViewHolder>{

    private Context mContext;
    private List<Post> mPosts;
    private PostClickListener mClickListener;

    public PostsRVAdapter(Context ctx, PostClickListener listener) {
        this.mContext = ctx;
        this.mPosts = new ArrayList<>();
        this.mClickListener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_post, parent, false);
        view.setFocusable(true);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.mPosts.size();
    }

    public void updatePostList(List<Post> posts) {
        this.mPosts = posts;
        this.notifyDataSetChanged();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_post_title) TextView mTvPostTitle;
        @BindView(R.id.tv_post_body) TextView mTvPostBody;

        PostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            Post post = mPosts.get(position);
            this.mTvPostTitle.setText(post.getTitle());
            SpannableString str = new SpannableString(post.getReducedBody() + " ...read more");
            str.setSpan(new StyleSpan(Typeface.ITALIC), str.length() - 9, str.length(), 0);
            str.setSpan(new ForegroundColorSpan(Color.BLUE), str.length() - 9, str.length(), 0);
            this.mTvPostBody.setText(str);
        }

        @Override
        public void onClick(View v) {
            Post post = mPosts.get(getAdapterPosition());
            mClickListener.onPostSelected(post);
        }
    }
}