package com.company.babysteps.services;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class SingleJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        DispatcherUtils.schedulePeriodicJob(this);
        jobFinished(job, false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}