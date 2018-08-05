package com.company.babysteps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.company.babysteps.entities.Baby;
import com.company.babysteps.entities.Post;
import com.company.babysteps.repositories.SettingsRepo;
import com.company.babysteps.repositories.SettingsRepoImpl;
import com.company.babysteps.utils.DateUtil;
import com.company.babysteps.views.TabAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends AppCompatActivity {

    public ProgressBar mProgressBar;

    private static final String POST_KEY = "POST";

    public static Baby sBaby;
    private TabAdapter mTabAdapter;
    private ViewPager mViewPager;
    private SettingsRepo mSettingsRepo;
    private boolean mFeedingInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setOrientation();
        setContentView(R.layout.activity_main);

        this.mSettingsRepo = new SettingsRepoImpl(this);
        sBaby = this.mSettingsRepo.getBabyInfo();
        if(getIntent().getExtras() != null) {
            this.mFeedingInProgress = getIntent()
                    .getExtras().getBoolean(getString(R.string.feeding_in_progress_key));
        }
        this.setContentElements();

        if(null == sBaby) {
            if(getIntent().getExtras() == null) {
                startSettingsActivity();
                this.finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_show_settings) {
            this.startSettingsActivity();
        }
        return true;
    }

    public static int[] getBabysAge() {
        if(sBaby != null) {
            return DateUtil.getPeriodToToday(sBaby.getDateOfBirth());
        } else {
            return null;
        }
    }

    public void openPostDetails(Post post) {
        if(!getResources().getBoolean(R.bool.isTablet)) {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(POST_KEY, post);
            startActivity(intent);
        } else {
            if(getSupportFragmentManager().getFragments().size() > 0) {
                BabyDevelopmentDetailsFragment fr = new BabyDevelopmentDetailsFragment();
                Bundle args = new Bundle();
                args.putParcelable(POST_KEY, post);
                fr.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction().replace(R.id.fragment_details_view, fr)
                        .commit();
            }
        }
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void setContentElements() {
        this.mTabAdapter = new TabAdapter(getSupportFragmentManager(), this, getSupportLoaderManager());
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(this.mTabAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        if(getResources().getBoolean(R.bool.isTablet)) {
            mViewPager.setOffscreenPageLimit(4);
        }
        tabLayout.setupWithViewPager(mViewPager);
        if(this.mFeedingInProgress) {
            this.mViewPager.setCurrentItem(1);
        }
        getSupportActionBar().setElevation(0);
        this.mProgressBar = findViewById(R.id.progress_bar);
        this.mProgressBar.setVisibility(View.VISIBLE);

        AdView mAdView = findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }

    private void setOrientation() {
        if(getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else  {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}