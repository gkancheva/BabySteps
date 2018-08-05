package com.company.babysteps;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.company.babysteps.entities.Baby;
import com.company.babysteps.entities.Growth;
import com.company.babysteps.repositories.GrowthRepo;
import com.company.babysteps.repositories.GrowthRepoImpl;
import com.company.babysteps.repositories.GrowthRepoListener;
import com.company.babysteps.services.OnDialogListener;
import com.company.babysteps.entities.SelectableEntity;
import com.company.babysteps.views.GrowthDialog;
import com.company.babysteps.views.GrowthRVAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GrowthFragment extends Fragment implements
        GrowthRVAdapter.GrowthClickListener,
        GrowthRepoListener,
        View.OnClickListener {

    @BindView(R.id.label_date) TextView mLabelDate;
    @BindView(R.id.label_age) TextView mLabelAge;
    @BindView(R.id.label_weight) TextView mLabelWeight;
    @BindView(R.id.label_height) TextView mLabelHeight;
    @BindView(R.id.label_head) TextView mLabelHead;
    @BindView(R.id.rv_growth) RecyclerView mRvGrowth;
    @BindView(R.id.fab) FloatingActionButton mFabAdd;

    private GrowthRVAdapter mAdapter;
    private GrowthRepo mGrowthRepo;
    private static Context sContext;
    private static LoaderManager sLoaderManager;

    public static GrowthFragment newInstance(Context ctx, LoaderManager loaderManager) {
        sContext = ctx;
        sLoaderManager = loaderManager;
        return new GrowthFragment();
    }

    public GrowthFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_growth, container, false);
        ButterKnife.bind(this, v);
        this.setLayoutElements();
        this.mGrowthRepo = new GrowthRepoImpl(sContext, sLoaderManager, this);
        this.mGrowthRepo.getGrowthList();
        return v;
    }

    @Override
    public void onGrowthSelected(Growth growth) {
        DialogFragment dialog = GrowthDialog.newInstance(sContext, getOnDialogListener(), growth);
        dialog.show(getChildFragmentManager(), "growth_dialog");
    }

    @Override
    public void onGetGrowthsSuccess(List<Growth> growthList) {
        this.updateViews(growthList.size() > 0);
        Baby baby = ((MainActivity)getActivity()).sBaby;
        if(baby != null) {
            mAdapter.updateGrowthList(growthList, baby.getDateOfBirth());
        }
    }

    @Override
    public void onSingleCompleted() {
        this.mGrowthRepo.getGrowthList();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fab) {
            this.saveGrowth();
        }
    }

    private void updateViews(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        this.mLabelDate.setVisibility(visibility);
        this.mLabelAge.setVisibility(visibility);
        this.mLabelWeight.setVisibility(visibility);
        this.mLabelHeight.setVisibility(visibility);
        this.mLabelHead.setVisibility(visibility);
    }

    private void setLayoutElements() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        this.mRvGrowth.setLayoutManager(layoutManager);
        this.mRvGrowth.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(sContext, DividerItemDecoration.VERTICAL);

        this.mRvGrowth.addItemDecoration(itemDecoration);
        Baby baby = ((MainActivity)getActivity()).sBaby;
        this.mAdapter = new GrowthRVAdapter(this, sContext, baby == null ? null : baby.getDateOfBirth());
        this.mRvGrowth.setAdapter(this.mAdapter);
        this.mFabAdd.setOnClickListener(this);
    }

    private void saveGrowth() {
        Baby baby = ((MainActivity)getActivity()).sBaby;
        if(baby == null) {
            Toast.makeText(sContext, R.string.msg_provide_baby_info, Toast.LENGTH_SHORT).show();
            return;
        }
        DialogFragment dialog = GrowthDialog.newInstance(sContext, getOnDialogListener(), null);
        dialog.show(getChildFragmentManager(), "growth_dialog");

    }

    private OnDialogListener getOnDialogListener() {
        return new OnDialogListener() {
            @Override
            public void onSaveSelected(SelectableEntity growth) {
                mGrowthRepo.saveGrowth((Growth)growth);
            }

            @Override
            public void onEditSelected(SelectableEntity growth) {
                mGrowthRepo.updateGrowth((Growth)growth);
            }

            @Override
            public void onDeleteSelected(SelectableEntity growth) {
                mGrowthRepo.deleteGrowth((Growth)growth);
            }
        };
    }

}