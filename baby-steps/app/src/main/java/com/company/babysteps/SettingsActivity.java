package com.company.babysteps;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.company.babysteps.entities.Baby;
import com.company.babysteps.entities.Week;
import com.company.babysteps.repositories.PostRepo;
import com.company.babysteps.repositories.PostRepoImpl;
import com.company.babysteps.repositories.SettingsRepo;
import com.company.babysteps.repositories.SettingsRepoImpl;
import com.company.babysteps.services.DispatcherUtils;
import com.company.babysteps.services.PostRepoListener;
import com.company.babysteps.utils.DateUtil;
import com.company.babysteps.utils.NetworkUtils;
import com.company.babysteps.utils.StringFormatter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity  implements
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener, CompoundButton.OnCheckedChangeListener, TimePickerDialog.OnTimeSetListener, PostRepoListener {

    private static final String SETTINGS_KEY = "SETTINGS";

    @BindView(R.id.progress_bar1) ProgressBar mProgressBar;
    @BindView(R.id.tv_baby_name) EditText mEtBabyName;
    @BindView(R.id.tv_date_of_birth) TextView mTvDateOfBirth;
    @BindView(R.id.btn_metric_units) RadioButton mBtnMetricUnits;
    @BindView(R.id.btn_imperial_units) RadioButton mBtnImperialUnits;
    @BindView(R.id.switch_notification_run) Switch mSwitchNotification;
    @BindView(R.id.label_time_of_notification) TextView mLabelTimeOfNotification;
    @BindView(R.id.tv_time_of_notification) TextView mTvTimeOfNotification;
    @BindView(R.id.btn_save) Button mBtnSave;
    private SettingsRepo mSettingsRepo;
    private PostRepo mPostRepo;
    private Date mDateOfBirth;
    private boolean isMetricUnitsPref;
    private String mPreferredTime;
    private boolean mReceiveNotifications;
    private Baby mBaby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_settings);

        if(getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else  {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        ButterKnife.bind(this);

        this.mSettingsRepo = new SettingsRepoImpl(this);
        this.mPostRepo = new PostRepoImpl(this, getSupportLoaderManager(), this);
        this.mBaby = this.mSettingsRepo.getBabyInfo();
        this.isMetricUnitsPref = this.mSettingsRepo.isMetricUnitsPreference();
        this.mReceiveNotifications = this.mSettingsRepo.getNotificationPref();
        this.mPreferredTime = this.mSettingsRepo.getNotificationPrefTime();

        this.setElements();
    }

    @Override
    public void onBackPressed() {
        //When back pressed is clicked without saving information
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(SETTINGS_KEY, false);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_date_of_birth) {
            this.showDatePickerDialog();
        } else if(v.getId() == R.id.tv_time_of_notification) {
            this.showTimePickerDialog();
        } else if(v.getId() == R.id.btn_save) {
            this.save();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        try {
            this.mDateOfBirth = StringFormatter.parseDate(year, month, dayOfMonth);
            this.mTvDateOfBirth.setText(StringFormatter.formatDate(this.mDateOfBirth));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.mPreferredTime = String.format(Locale.getDefault(), getString(R.string.time_format), hourOfDay, minute);
        this.mTvTimeOfNotification.setText(this.mPreferredTime);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.mSwitchNotification.setChecked(isChecked);
        this.mReceiveNotifications = isChecked;
        if(this.mReceiveNotifications) {
            this.mLabelTimeOfNotification.setVisibility(View.VISIBLE);
            this.mTvTimeOfNotification.setVisibility(View.VISIBLE);
        } else {
            this.mLabelTimeOfNotification.setVisibility(View.GONE);
            this.mTvTimeOfNotification.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPostSuccess(Week week) {
        this.mProgressBar.setVisibility(View.GONE);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onPostFailure(String message) {
        this.mProgressBar.setVisibility(View.GONE);
        Toast.makeText(this, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
    }

    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        if(this.mBaby != null) {
            c.setTime(this.mBaby.getDateOfBirth());
        }
        DatePickerDialog datePD = new DatePickerDialog(this, this,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        DatePicker dp = datePD.getDatePicker();
        dp.setMaxDate(new Date().getTime());
        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.MONTH, -11);
        minDate.add(Calendar.DAY_OF_MONTH, -25);
        dp.setMinDate(minDate.getTimeInMillis());
        datePD.show();
    }

    private void showTimePickerDialog() {
        Calendar c = Calendar.getInstance();
        TimePickerDialog timePD = new TimePickerDialog(this, this,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                false);
        timePD.setTitle(getString(R.string.time_picker_title));
        timePD.show();
    }

    private void save() {
        String name = this.mEtBabyName.getText().toString();
        String timeOfNotification = this.mTvTimeOfNotification.getText().toString();
        if(name.equals("")) {
            Toast.makeText(this, R.string.msg_provide_name, Toast.LENGTH_SHORT).show();
            return;
        }
        if(this.mTvDateOfBirth.getText().toString().equals("")) {
            Toast.makeText(this, R.string.msg_provide_date_of_birth, Toast.LENGTH_SHORT).show();
            return;
        }
        if(this.mReceiveNotifications && timeOfNotification.equals("")) {
            Toast.makeText(this, R.string.msg_provide_notification_time, Toast.LENGTH_SHORT).show();
            return;
        }
        this.mBaby = new Baby(name, this.mDateOfBirth);
        this.mSettingsRepo.saveBabyInfoLocally(this.mBaby);
        this.mSettingsRepo.saveMetricUnitsPreference(this.isMetricUnitsPref);
        this.mPreferredTime = timeOfNotification;
        this.mSettingsRepo.saveNotificationPref(this.mPreferredTime, this.mReceiveNotifications);
        int[] age = DateUtil.getPeriodToToday(mBaby.getDateOfBirth());
        this.mPostRepo.getPostsForWeekFromTheWeb(age);
        this.mProgressBar.setVisibility(View.VISIBLE);
        if(this.mReceiveNotifications) {
            if(this.mBaby != null  && !timeOfNotification.equals("")){
                DispatcherUtils.scheduleSingleJob(this, this.mPreferredTime);
            }
        } else {
            DispatcherUtils.cancelNotifications(this);
        }
        if(!NetworkUtils.hasNetworkConnectivity(this)) {
            this.mProgressBar.setVisibility(View.GONE);
        }
    }

    private void setElements() {
        this.mBtnSave.setOnClickListener(this);
        this.mTvDateOfBirth.setOnClickListener(this);
        this.mSwitchNotification.setOnCheckedChangeListener(this);
        this.mTvTimeOfNotification.setOnClickListener(this);
        if(this.isMetricUnitsPref) {
            this.mBtnMetricUnits.setChecked(true);
            this.mBtnImperialUnits.setChecked(false);
        } else {
            this.mBtnMetricUnits.setChecked(false);
            this.mBtnImperialUnits.setChecked(true);
        }
        this.mBtnMetricUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMetricUnitsPref = true;
                mBtnMetricUnits.setChecked(true);
                mBtnImperialUnits.setChecked(false);
            }
        });

        this.mBtnImperialUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMetricUnitsPref = false;
                mBtnMetricUnits.setChecked(false);
                mBtnImperialUnits.setChecked(true);
            }
        });

        if(this.mBaby != null) {
            this.mEtBabyName.setText(this.mBaby.getName());
            this.mTvDateOfBirth.setText(StringFormatter.formatDate(this.mBaby.getDateOfBirth()));
            this.mDateOfBirth = this.mBaby.getDateOfBirth();
        }

        this.mSwitchNotification.setChecked(this.mReceiveNotifications);
        if(!this.mReceiveNotifications) {
            this.mLabelTimeOfNotification.setVisibility(View.GONE);
            this.mTvTimeOfNotification.setVisibility(View.GONE);
        } else {
            this.mLabelTimeOfNotification.setVisibility(View.VISIBLE);
            this.mTvTimeOfNotification.setVisibility(View.VISIBLE);
            this.mTvTimeOfNotification.setText(this.mPreferredTime == null ? "" : this.mPreferredTime);
        }

    }
}
