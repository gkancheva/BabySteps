package com.company.babysteps;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HostFragment extends Fragment {

    private static LoaderManager sLoaderManager;
    private static Context sContext;

    public static HostFragment newInstance(Context context, LoaderManager loaderManager) {
        sLoaderManager = loaderManager;
        sContext = context;
        return new HostFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_host, container, false);

        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment_list, BabyDevelopmentListFragment.newInstance(sContext, sLoaderManager))
                .commit();

        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment_details_view, new BabyDevelopmentDetailsFragment())
                .commit();

        return view;
    }
}