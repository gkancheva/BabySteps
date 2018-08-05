package com.company.babysteps;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.company.babysteps.entities.Post;

public class DetailsActivity extends AppCompatActivity {

    private static final String POST_KEY = "POST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_details);

        if(getIntent().getExtras().getParcelable(POST_KEY) != null) {
            Post post = getIntent().getExtras().getParcelable(POST_KEY);
            Bundle args = new Bundle();
            args.putParcelable(POST_KEY, post);
            BabyDevelopmentDetailsFragment fragment = new BabyDevelopmentDetailsFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_details_portrait, fragment)
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
