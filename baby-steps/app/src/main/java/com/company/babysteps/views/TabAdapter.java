package com.company.babysteps.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;

import com.company.babysteps.BabyDevelopmentListFragment;
import com.company.babysteps.FeedingFragment;
import com.company.babysteps.GrowthFragment;
import com.company.babysteps.R;
import com.company.babysteps.HostFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private static final int TAB_COUNT = 3;
    private Context mContext;
    private LoaderManager mLoaderManager;

    public TabAdapter(FragmentManager fm, Context ctx, LoaderManager loaderManager) {
        super(fm);
        this.mContext = ctx;
        this.mLoaderManager = loaderManager;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if(this.mContext.getResources().getBoolean(R.bool.isTablet)) {
                    return HostFragment.newInstance(this.mContext, this.mLoaderManager);
                }
                return BabyDevelopmentListFragment.newInstance(this.mContext, this.mLoaderManager);
            case 1:
                return FeedingFragment.newInstance(mContext, mLoaderManager);
            case 2:
                return GrowthFragment.newInstance(mContext, mLoaderManager);
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String[] titles = mContext.getResources().getStringArray(R.array.fragments_titles);
        return titles[position];
    }
}