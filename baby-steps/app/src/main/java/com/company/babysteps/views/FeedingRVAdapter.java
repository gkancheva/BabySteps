package com.company.babysteps.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.babysteps.R;
import com.company.babysteps.entities.Feeding;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedingRVAdapter extends RecyclerView.Adapter<FeedingRVAdapter.FeedingViewHolder>{

    private List<Feeding> mFeedings;
    private FeedingClickListener mListener;
    private Context mContext;

    public interface FeedingClickListener {
         void onFeedingSelected(Feeding feeding);
    }

    public FeedingRVAdapter(FeedingClickListener mListener, Context ctx) {
        this.mFeedings = new ArrayList<>();
        this.mListener = mListener;
        this.mContext = ctx;
    }

    @NonNull
    @Override
    public FeedingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_feeding, parent, false);
        return new FeedingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedingViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.mFeedings.size();
    }

    public void updateFeedingsList(List<Feeding> feedings) {
        int itemCount = this.getItemCount();
        this.mFeedings.clear();
        this.notifyItemRangeRemoved(0, itemCount);
        this.mFeedings.addAll(feedings);
        this.notifyItemRangeInserted(0, feedings.size());
    }

    public class FeedingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_feeding_info) TextView mTvFeedingInfo;

        FeedingViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        void bind(int position) {
            Feeding feeding = mFeedings.get(position);
            this.mTvFeedingInfo.setText(feeding.feedingToString(mContext));
        }

        @Override
        public void onClick(View v) {
            Feeding feeding = mFeedings.get(getAdapterPosition());
            mListener.onFeedingSelected(feeding);
        }
    }
}