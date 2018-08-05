package com.company.babysteps.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.babysteps.R;
import com.company.babysteps.entities.Growth;
import com.company.babysteps.utils.DateUtil;
import com.company.babysteps.utils.StringFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GrowthRVAdapter extends RecyclerView.Adapter<GrowthRVAdapter.GrowthViewHolder>{

    private List<Growth> mGrowthList;
    private GrowthClickListener mListener;
    private Context mContext;
    private Date mBabyDateOfBirth;

    public interface GrowthClickListener {
         void onGrowthSelected(Growth growth);
    }

    public GrowthRVAdapter(GrowthClickListener mListener, Context ctx, Date babyDateOfBirth) {
        this.mGrowthList = new ArrayList<>();
        this.mListener = mListener;
        this.mContext = ctx;
        this.mBabyDateOfBirth = babyDateOfBirth;
    }

    @NonNull
    @Override
    public GrowthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_growth, parent, false);
        return new GrowthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrowthViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.mGrowthList.size();
    }

    public void updateGrowthList(List<Growth> growthList, Date dateOfBirth) {
        mBabyDateOfBirth = dateOfBirth;
        int itemCount = this.getItemCount();
        this.mGrowthList.clear();
        this.notifyItemRangeRemoved(0, itemCount);
        this.mGrowthList.addAll(growthList);
        this.notifyItemRangeInserted(0, growthList.size());
    }

    public class GrowthViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_date) TextView mTvDate;
        @BindView(R.id.tv_age) TextView mTvAge;
        @BindView(R.id.tv_weight) TextView mTvWeight;
        @BindView(R.id.tv_height) TextView mTvHeight;
        @BindView(R.id.tv_head) TextView mTvHead;

        GrowthViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        void bind(int position) {
            Growth growth = mGrowthList.get(position);
            this.mTvDate.setText(StringFormatter.formatDateShort(growth.getDate()));
            String age = DateUtil.getMonths(mBabyDateOfBirth, growth.getDate())
                    + mContext.getString(R.string.month_abbrev_growth_age);
            this.mTvAge.setText(age);
            this.mTvWeight.setText(StringFormatter.formatWeight(growth.getWeight(), mContext));
            this.mTvHeight.setText(StringFormatter.formatLength(growth.getHeight(), mContext));
            this.mTvHead.setText(StringFormatter.formatLength(growth.getHead(), mContext));
        }

        @Override
        public void onClick(View v) {
            Growth growth = mGrowthList.get(getAdapterPosition());
            mListener.onGrowthSelected(growth);
        }
    }
}