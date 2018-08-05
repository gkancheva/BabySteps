package com.company.babysteps;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.company.babysteps.entities.Baby;
import com.company.babysteps.entities.Feeding;
import com.company.babysteps.entities.FeedingType;
import com.company.babysteps.repositories.FeedingRepo;
import com.company.babysteps.repositories.FeedingRepoImpl;
import com.company.babysteps.repositories.FeedingRepoListener;
import com.company.babysteps.services.OnDialogListener;
import com.company.babysteps.entities.SelectableEntity;
import com.company.babysteps.utils.NotificationUtils;
import com.company.babysteps.utils.StringFormatter;
import com.company.babysteps.views.BottleFeedingDialog;
import com.company.babysteps.views.BreastFeedingDialog;
import com.company.babysteps.views.FeedingRVAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedingFragment extends Fragment implements
        View.OnClickListener,
        FeedingRepoListener,
        FeedingRVAdapter.FeedingClickListener{

    @BindView(R.id.btn_breast_feeding) Button mBtnBreastFeeding;
    @BindView(R.id.btn_bottle_feeding) Button mBtnBottleFeeding;
    @BindView(R.id.tv_latest_activity) TextView mTvLatestActivity;
    @BindView(R.id.rv_feeding_activities) RecyclerView mRvFeedings;
    @BindView(R.id.timer) Chronometer mChronometer;
    @BindView(R.id.tv_timer_on) TextView mTimerOnInfo;
    @BindView(R.id.btn_timer_stop) Button mBtnStopTimer;

    private FeedingRepo mFeedingRepo;
    private List<Feeding> mFeedings;
    private static Context sContext;
    private static LoaderManager sLoaderManager;
    private boolean mFeedingInProgress;
    private long mStartMilliseconds = System.currentTimeMillis();

    private FeedingRVAdapter mFeedingAdapter;

    private FirebaseAnalytics mFirebaseAnalytics;

    public static FeedingFragment newInstance(Context ctx, LoaderManager loaderManager) {
        sContext = ctx;
        sLoaderManager = loaderManager;
        return new FeedingFragment();
    }

    public FeedingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_feeding, container, false);
        ButterKnife.bind(this, v);
        this.mFeedingRepo = new FeedingRepoImpl(sContext, this, sLoaderManager);
        this.setLayoutElements();
        this.mFeedingRepo.getFeedings();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(sContext);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(sContext);
        this.mStartMilliseconds = sharedPref.getLong(getString(R.string.pref_timer_start_time), 0);
        this.mFeedingInProgress = sharedPref.getBoolean(getString(R.string.pref_feeding_in_progress), false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isAdded()) {
            updateLatestActivityTextView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(sContext);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(getString(R.string.pref_timer_start_time), this.mStartMilliseconds);
        editor.putBoolean(getString(R.string.pref_feeding_in_progress), this.mFeedingInProgress);
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        Baby baby = ((MainActivity)getActivity()).sBaby;
        switch (v.getId()) {
            case R.id.btn_breast_feeding:
                if(baby == null) {
                    Toast.makeText(sContext, sContext.getString(R.string.msg_provide_baby_info), Toast.LENGTH_SHORT).show();
                    return;
                }
                this.mStartMilliseconds = System.currentTimeMillis();
                this.mFeedingInProgress = true;
                this.startChronometer(SystemClock.elapsedRealtime());
                NotificationUtils.notifyFeedingActivityInProgress(sContext);
                break;
            case R.id.btn_bottle_feeding:
                if(baby == null) {
                    Toast.makeText(sContext, sContext.getString(R.string.msg_provide_baby_info), Toast.LENGTH_SHORT).show();
                    return;
                }
                this.showAddingBottleFeeding();
                break;
            case R.id.btn_timer_stop:
                this.logFirebaseAnalyticsEvent(sContext.getString(R.string.breast_feeding));
                stopChronometer();
                NotificationUtils.cancelFeedingNotification(sContext);
                break;
            default:
                break;
        }
    }

    private void logFirebaseAnalyticsEvent(String feedingType) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, feedingType);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    @Override
    public void onGetFeedingsSuccess(List<Feeding> feedings) {
        this.mFeedings = feedings;
        updateLatestActivityTextView();
        this.mFeedingAdapter.updateFeedingsList(this.mFeedings);
    }

    private void updateLatestActivityTextView() {
        if(this.mFeedings != null && this.mFeedings.size() > 0) {
            String formatTxtLatestActivity = getString(R.string.format_str_latest_activity);
            String date = StringFormatter.getFormattedElapsedTime(this.mFeedings.get(0).getStartDateTime(), sContext);
            this.mTvLatestActivity.setText(String.format(formatTxtLatestActivity, date));
        } else {
            this.mTvLatestActivity.setText(R.string.latest_activity_no_info);
        }
    }

    @Override
    public void onSingleCompleted() {
        this.mFeedingRepo.getFeedings();
    }

    @Override
    public void onFeedingSelected(Feeding feeding) {
        DialogFragment dialog;
        if(feeding.getType() == FeedingType.BREAST_FEEDING) {
            dialog = BreastFeedingDialog.newInstance(sContext, getDialogListener(), feeding);
        } else {
            dialog = BottleFeedingDialog.newInstance(sContext, getDialogListener(), feeding);
        }
        dialog.show(getChildFragmentManager(), "feeding_dialog");
    }

    private OnDialogListener getDialogListener() {
        return new OnDialogListener() {
            @Override
            public void onSaveSelected(SelectableEntity feeding) {
                Baby baby = ((MainActivity)getActivity()).sBaby;
                if(baby == null) {
                    Toast.makeText(sContext, sContext.getString(R.string.msg_provide_baby_info), Toast.LENGTH_SHORT).show();
                    return;
                }
                mFeedingRepo.saveFeeding((Feeding)feeding);
                logFirebaseAnalyticsEvent(sContext.getString(R.string.bottle_feeding));
            }

            @Override
            public void onEditSelected(SelectableEntity feeding) {
                mFeedingRepo.updateFeeding((Feeding)feeding);
            }

            @Override
            public void onDeleteSelected(SelectableEntity feeding) {
                mFeedingRepo.deleteFeeding((Feeding)feeding);
            }
        };
    }

    private void startChronometer(long base) {
        this.updateViewsVisibility();
        this.mTimerOnInfo.setText(sContext.getString(R.string.breast_feeding));
        this.mChronometer.setBase(base);
        this.mChronometer.start();
    }

    private void stopChronometer() {
        long time = SystemClock.elapsedRealtime() - this.mChronometer.getBase();
        Date startDateTime = new Date(this.mStartMilliseconds);
        Date endDateTime = new Date(this.mStartMilliseconds + time);
        Feeding feeding = new Feeding(0, FeedingType.BREAST_FEEDING, null,
                startDateTime, endDateTime, 0, null);
        this.mChronometer.stop();
        this.mFeedingInProgress = false;
        this.mStartMilliseconds = -1;
        this.updateViewsVisibility();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(sContext);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(getString(R.string.start_time_key), this.mStartMilliseconds);
        editor.putBoolean(sContext.getString(R.string.feeding_in_progress_key), this.mFeedingInProgress);
        editor.apply();
        this.mFeedingRepo.saveFeeding(feeding);
        this.mFeedingRepo.getFeedings();
    }

    private void showAddingBottleFeeding() {
        DialogFragment dialog = BottleFeedingDialog.newInstance(sContext, getDialogListener(), null);
        dialog.show(getChildFragmentManager(), "bottle_feeding_dialog");
    }

    private void updateViewsVisibility() {
        if(this.mFeedingInProgress) {
            this.mBtnBreastFeeding.setVisibility(View.GONE);
            this.mBtnBottleFeeding.setVisibility(View.GONE);
            this.mTvLatestActivity.setVisibility(View.GONE);
            this.mChronometer.setVisibility(View.VISIBLE);
            this.mTimerOnInfo.setVisibility(View.VISIBLE);
            this.mBtnStopTimer.setVisibility(View.VISIBLE);
        } else {
            this.mBtnBreastFeeding.setVisibility(View.VISIBLE);
            this.mBtnBottleFeeding.setVisibility(View.VISIBLE);
            this.mTvLatestActivity.setVisibility(View.VISIBLE);
            this.mChronometer.setVisibility(View.GONE);
            this.mTimerOnInfo.setVisibility(View.GONE);
            this.mBtnStopTimer.setVisibility(View.GONE);
        }
    }

    private void setLayoutElements() {
        this.mRvFeedings.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mRvFeedings.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(sContext, DividerItemDecoration.VERTICAL);
        this.mRvFeedings.addItemDecoration(itemDecoration);
        this.mFeedingAdapter = new FeedingRVAdapter(this, sContext);
        this.mRvFeedings.setAdapter(this.mFeedingAdapter);
        this.mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                mChronometer = chronometer;
            }
        });
        this.mBtnBreastFeeding.setOnClickListener(this);
        this.mBtnBottleFeeding.setOnClickListener(this);
        this.mBtnStopTimer.setOnClickListener(this);
        if(this.mFeedingInProgress) {
            long elapsedTime = System.currentTimeMillis() - this.mStartMilliseconds;
            this.startChronometer(SystemClock.elapsedRealtime() - elapsedTime);
        }
    }
}