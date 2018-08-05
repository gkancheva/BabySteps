package com.company.babysteps.services;

import android.os.AsyncTask;

import com.company.babysteps.R;
import com.company.babysteps.entities.Baby;
import com.company.babysteps.entities.Week;
import com.company.babysteps.repositories.PostRepo;
import com.company.babysteps.repositories.PostRepoImpl;
import com.company.babysteps.repositories.SettingsRepo;
import com.company.babysteps.repositories.SettingsRepoImpl;
import com.company.babysteps.utils.DateUtil;
import com.company.babysteps.utils.HTMLParserUtils;
import com.company.babysteps.utils.NetworkUtils;
import com.company.babysteps.utils.NotificationUtils;
import com.company.babysteps.utils.UrlConst;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.Locale;

public class PeriodicJobService extends JobService {

    private SettingsRepo mSettingsRepo;
    private static AsyncTask<String, String, Week> mGetPostsInBackground;

    @Override
    public boolean onStartJob(final JobParameters job) {
        this.mSettingsRepo = new SettingsRepoImpl(this);
        Baby baby = this.mSettingsRepo.getBabyInfo();
        int[] age = DateUtil.getPeriodToToday(baby.getDateOfBirth());
        final boolean weekOrMonthAnniversary = DateUtil.weekOrMonthAnniversary(baby.getDateOfBirth());

        if(weekOrMonthAnniversary) {
            String title = this.getTitle(age);
            String body = this.getString(R.string.msg_notification_body);
            NotificationUtils.notifyWeekOrMonthAnniversary(this, title, body);
            mGetPostsInBackground = new AsyncTask<String, String, Week>() {

                private boolean rescheduleJob = false;

                @Override
                protected Week doInBackground(String... params) {
                    if(NetworkUtils.hasNetworkConnectivity(PeriodicJobService.this)) {
                        String path = params[0];
                        return HTMLParserUtils.getWeekInfo(path);
                    }
                    rescheduleJob = true;
                    return null;
                }

                @Override
                protected void onPostExecute(Week week) {
                    if(week != null) {
                        PostRepo postRepo = new PostRepoImpl(PeriodicJobService.this, null, null);
                        postRepo.saveWeekLocally(week);
                    }
                    jobFinished(job, rescheduleJob);
                }
            };
            UrlConst urlConst = UrlConst.getInstance();
            String path = urlConst.getUrl(age[0], age[1]);
            this.mGetPostsInBackground.execute(path);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(this.mGetPostsInBackground != null) {
            this.mGetPostsInBackground.cancel(true);
        }
        return true;
    }

    private String getTitle(int[] age) {
        String monthExpr = age[0] == 0 ? "" : " " + age[0] + getString(R.string.month_expression);
        String weekExpr = age[1] == 0 ? "" : " " + age[1] + getString(R.string.week_expression);
        return String.format(Locale.getDefault(), getString(R.string.format_notification_title), monthExpr, weekExpr);
    }
}