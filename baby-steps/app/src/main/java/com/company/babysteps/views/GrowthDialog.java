package com.company.babysteps.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.Toast;

import com.company.babysteps.MainActivity;
import com.company.babysteps.R;
import com.company.babysteps.entities.Baby;
import com.company.babysteps.entities.Growth;
import com.company.babysteps.services.OnDialogListener;
import com.company.babysteps.utils.StringFormatter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GrowthDialog extends DialogFragment {

    @BindView(R.id.et_date) EditText mEtDate;
    @BindView(R.id.et_weight) EditText mEtWeight;
    @BindView(R.id.et_height) EditText mEtHeight;
    @BindView(R.id.et_head) EditText mEtHead;
    private static Context sContext;
    private static OnDialogListener sListener;
    private static Growth sGrowth;
    private Date mDate;

    public static GrowthDialog newInstance(Context ctx, OnDialogListener listener, Growth growth) {
        sContext = ctx;
        sListener = listener;
        sGrowth = growth;
        return new GrowthDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(sContext).inflate(R.layout.growth_dialog, null);
        ButterKnife.bind(this, view);
        this.setElements();
        AlertDialog dialog = getAlertDialog(view);
        dialog.setOnShowListener(getShowListener());
        return dialog;
    }

    private AlertDialog getAlertDialog(View view) {
        if(sGrowth == null) {
            return new AlertDialog.Builder(sContext)
                    .setView(view)
                    .setCancelable(true)
                    .setPositiveButton(R.string.btn_save, null)
                    .setNegativeButton(R.string.btn_cancel, getCancelListener())
                    .create();
        }
        return new AlertDialog.Builder(sContext)
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(R.string.btn_edit, null)
                .setNegativeButton(R.string.btn_delete, getDeleteListener())
                .setNeutralButton(R.string.btn_cancel, getCancelListener())
                .create();
    }

    private DialogInterface.OnClickListener getDeleteListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sListener.onDeleteSelected(sGrowth);
            }
        };
    }

    private DialogInterface.OnShowListener getShowListener() {
        return new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strWeight = mEtWeight.getText().toString();
                        String strHeight = mEtHeight.getText().toString();
                        String strHead = mEtHead.getText().toString();
                        Growth growth = null;
                        Baby baby = ((MainActivity)getActivity()).sBaby;
                        if(baby.getDateOfBirth().after(mDate)) {
                            Toast.makeText(sContext, R.string.msg_invalid_date, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            double weight = !strWeight.equals("") ? Double.parseDouble(strWeight) : 0;
                            double height = !strHeight.equals("") ? Double.parseDouble(strHeight) : 0;
                            double head = !strHead.equals("") ? Double.parseDouble(strHead) : 0;
                            growth = new Growth(mDate, weight, height, head);
                        } catch (NumberFormatException e) {
                            Toast.makeText(sContext, R.string.msg_provide_valid_numbers, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(sGrowth == null) {
                            sListener.onSaveSelected(growth);
                        } else {
                            growth.setId(sGrowth.getId());
                            sListener.onEditSelected(growth);
                        }
                        dialog.dismiss();
                    }
                });
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

    private View.OnClickListener getDateTvClickListener() {
        final Baby baby = ((MainActivity)getActivity()).sBaby;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePD = new DatePickerDialog(sContext, getDateSetDialog(),
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                DatePicker dp = datePD.getDatePicker();
                dp.setMaxDate(new Date().getTime());
                dp.setMinDate(baby.getDateOfBirth().getTime());
                datePD.show();
            }
        };
    }

    private DatePickerDialog.OnDateSetListener getDateSetDialog() {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                try {
                    mDate = StringFormatter.parseDate(year, month, dayOfMonth);
                } catch (ParseException e) {
                    Log.e("GrowthDialog", e.getMessage());
                }
                mEtDate.setText(StringFormatter.formatDateShort(mDate));
            }
        };
    }

    private void setElements() {
        this.mEtDate.setText(StringFormatter.formatDateShort(new Date()));
        mEtDate.setOnClickListener(getDateTvClickListener());
        mDate = new Date();
        if(sGrowth != null) {
            this.mEtDate.setText(StringFormatter.formatDateShort(sGrowth.getDate()));
            this.mEtWeight.setText(sGrowth.getWeight() + "");
            this.mEtHeight.setText(sGrowth.getHeight() + "");
            this.mEtHead.setText(sGrowth.getHead() + "");
        }

    }
}