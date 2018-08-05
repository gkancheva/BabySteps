package com.company.babysteps;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.babysteps.entities.Post;
import com.company.babysteps.entities.Week;
import com.company.babysteps.repositories.PostRepo;
import com.company.babysteps.repositories.PostRepoImpl;
import com.company.babysteps.services.PostClickListener;
import com.company.babysteps.services.PostRepoListener;
import com.company.babysteps.views.PostsRVAdapter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BabyDevelopmentListFragment extends Fragment implements PostRepoListener, PostClickListener {

    @BindView(R.id.iv_week_image) ImageView mIvWeekImage;
    @BindView(R.id.tv_week_main_title) TextView mTvWeekTitle;
    @BindView(R.id.rv_posts) RecyclerView mRvPosts;

    private static Context sContext;
    private PostRepo mPostRepo;
    private PostsRVAdapter mRVAdapter;
    private static LoaderManager mLoaderManager;
    private int[] mBabyAge;

    public static BabyDevelopmentListFragment newInstance(Context ctx, LoaderManager loaderManager) {
        sContext = ctx;
        mLoaderManager = loaderManager;
        return new BabyDevelopmentListFragment();
    }

    public BabyDevelopmentListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_baby_dev, container, false);
        ButterKnife.bind(this, view);
        this.setLayoutElements();
        if(sContext == null) {
            sContext = getParentFragment().getActivity();
        }
        if(mLoaderManager == null) {
            mLoaderManager = getParentFragment().getActivity().getSupportLoaderManager();
        }
        this.mPostRepo = new PostRepoImpl(sContext, mLoaderManager, this);
        this.mBabyAge = MainActivity.getBabysAge();
        if(this.mBabyAge == null) {
            this.mBabyAge = new int[]{0, 0};
            Toast.makeText(sContext, R.string.msg_add_baby, Toast.LENGTH_SHORT).show();
        }

        this.mPostRepo.getWeekWithPostsFromDB();
        return view;
    }

    @Override
    public void onPostSuccess(Week week) {
        if(isAdded()) {
            if (week == null) {
                this.mPostRepo.getPostsForWeekFromTheWeb(this.mBabyAge);
                return;
            }
            this.updateUi(week);
            if (getResources().getBoolean(R.bool.isTablet)) {
                this.onPostSelected(week.getPosts().get(0));
            }
        }
    }

    @Override
    public void onPostFailure(String message) {
        Toast.makeText(sContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostSelected(Post post) {
        ((MainActivity)getActivity()).openPostDetails(post);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).mProgressBar.setVisibility(View.GONE);
    }

    private void updateUi(Week week) {
        this.mTvWeekTitle.setText(week.getTitle());
        Picasso.with(sContext)
                .load(week.getImageUrl())
                .placeholder(R.mipmap.ic_baby_feet)
                .error(R.mipmap.ic_baby_feet)
                .into(this.mIvWeekImage);
        this.mRVAdapter.updatePostList(week.getPosts());
    }

    private void setLayoutElements() {
        this.mRvPosts.setLayoutManager(new LinearLayoutManager(sContext));
        this.mRvPosts.setItemAnimator(new DefaultItemAnimator());
        this.mRVAdapter = new PostsRVAdapter(sContext, this);
        this.mRvPosts.setAdapter(this.mRVAdapter);
    }
}