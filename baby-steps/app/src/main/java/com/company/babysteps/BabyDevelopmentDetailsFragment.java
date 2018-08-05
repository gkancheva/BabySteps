package com.company.babysteps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.babysteps.entities.Post;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BabyDevelopmentDetailsFragment extends Fragment {

    private static final String POST_KEY = "POST";

    @BindView(R.id.tv_post_title_in_details)
    TextView mTvPostTitle;
    @BindView(R.id.tv_post_body_in_details)
    TextView mTvPostBody;
    private Post mPost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            this.mPost = savedInstanceState.getParcelable(POST_KEY);
        }
        if(getArguments() != null && getArguments().getParcelable(POST_KEY) != null) {
            this.mPost = getArguments().getParcelable(POST_KEY);
        }

        View view = inflater.inflate(R.layout.fr_baby_dev_details, container, false);
        ButterKnife.bind(this, view);
        if(this.mPost != null) {
            this.mTvPostTitle.setText(this.mPost.getTitle());
            this.mTvPostBody.setText(this.mPost.getBody());
        }
        return view;
    }

}