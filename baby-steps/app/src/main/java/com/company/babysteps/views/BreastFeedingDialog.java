package com.company.babysteps.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.company.babysteps.R;
import com.company.babysteps.entities.Feeding;
import com.company.babysteps.entities.FeedingType;
import com.company.babysteps.services.OnDialogListener;
import com.company.babysteps.utils.StringFormatter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BreastFeedingDialog extends DialogFragment {

    @BindView(R.id.tv_start_date) TextView mTvStartDate;
    @BindView(R.id.tv_end_date) TextView mTvEndDate;
    @BindView(R.id.et_details) TextView mTVDetails;
    private static Context sContext;
    private static Feeding sFeeding;
    private static OnDialogListener sListener;

    private Date mStartDate;
    private Date mEndDate;
    private int[] mTempStartDate;
    private int[] mTempEndDate;

    public static BreastFeedingDialog newInstance(Context ctx, OnDialogListener listener, Feeding feeding) {
        sContext = ctx;
        sListener = listener;
        sFeeding = feeding;
        return new BreastFeedingDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(sContext).inflate(R.layout.breast_feeding_dialog, null);
        ButterKnife.bind(this, view);
        this.setElements();
        AlertDialog dialog = new AlertDialog.Builder(sContext)
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(R.string.btn_edit, null)
                .setNeutralButton(R.string.btn_cancel, getCancelListener())
                .setNegativeButton(getString(R.string.btn_delete), getDeleteListener()).create();

        dialog.setOnShowListener(getShowListener());
        return dialog;
    }

    private DialogInterface.OnShowListener getShowListener() {
        return new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mEndDate.after(mStartDate)) {
                            String details = mTVDetails.getText().toString().equals("") ? null : mTVDetails.getText().toString();
                            Feeding feeding = new Feeding(sFeeding.getId(),
                                    FeedingType.BREAST_FEEDING, details, mStartDate, mEndDate, 0, null);
                            sListener.onEditSelected(feeding);
                            dialog.dismiss();
                            return;
                        }
                        Toast.makeText(sContext, R.string.msg_end_time_before_start_time, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
    }

    private DialogInterface.OnClickListener getDeleteListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sListener.onDeleteSelected(sFeeding);
            }
        };
    }

    private DialogInterface.OnClickListener getCancelListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        };
    }

    private View.OnClickListener getDateTvClickListener(final boolean isStartDate) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(isStartDate);
            }
        };
    }

    private void showTimeDialog(boolean isStartDate) {
        final Calendar c = Calendar.getInstance();
        c.setTime(isStartDate ? sFeeding.getStartDateTime() : sFeeding.getEndDateTime());
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        TimePickerDialog timeDialog = new TimePickerDialog(sContext, getTimeSetListener(isStartDate), hour, minutes, false);
        timeDialog.show();
    }

    private TimePickerDialog.OnTimeSetListener getTimeSetListener(final boolean isStartDate) {
        return new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int h, int min) {
                try {
                    if(isStartDate) {
                        mStartDate = StringFormatter.parseDateTime(mTempStartDate[0], mTempStartDate[1], mTempStartDate[2], h, min);
                        mTvStartDate.setText(StringFormatter.formatDateTime(mStartDate));
                    } else {
                        mEndDate = StringFormatter.parseDateTime(mTempEndDate[0], mTempEndDate[1], mTempEndDate[2], h, min);
                        mTvEndDate.setText(StringFormatter.formatDateTime(mEndDate));
                    }
                } catch (ParseException e) {
                    Log.d("BreastFeedingDialog", e.getMessage());
                }
            }
        };
    }

    private void showDatePickerDialog(boolean isStartDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(isStartDate ? sFeeding.getStartDateTime() : sFeeding.getEndDateTime());
        DatePickerDialog datePD = new DatePickerDialog(sContext, getDateSetDialog(isStartDate),
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        datePD.show();
    }

    private DatePickerDialog.OnDateSetListener getDateSetDialog(final boolean isStartDate) {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(isStartDate) {
                    mTempStartDate = new int[]{year, month, dayOfMonth};
                } else {
                    mTempEndDate = new int[]{year, month, dayOfMonth};
                }
                showTimeDialog(isStartDate);
            }
        };
    }

    private void setElements() {
        if(sFeeding != null) {
            this.mTvStartDate.setText(StringFormatter.formatDateTime(sFeeding.getStartDateTime()));
            this.mTvEndDate.setText(StringFormatter.formatDateTime(sFeeding.getEndDateTime()));
            this.mTVDetails.setText(sFeeding.getDetails() == null ? "" : sFeeding.getDetails());
            this.mTvStartDate.setOnClickListener(getDateTvClickListener(true));
            this.mTvEndDate.setOnClickListener(getDateTvClickListener(false));
            this.mStartDate = sFeeding.getStartDateTime();
            this.mEndDate = sFeeding.getEndDateTime();
        }
    }

}