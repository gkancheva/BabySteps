package com.company.babysteps.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.company.babysteps.R;
import com.company.babysteps.entities.Feeding;
import com.company.babysteps.entities.FeedingType;
import com.company.babysteps.entities.MilkType;
import com.company.babysteps.services.OnDialogListener;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottleFeedingDialog extends DialogFragment implements
        AdapterView.OnItemSelectedListener {

    @BindView(R.id.spinner_milk_type) Spinner mMilkTypeSpinner;
    @BindView(R.id.et_quantity) EditText mTvQuantity;
    @BindView(R.id.et_details) EditText mTvDetails;
    private static Context sContext;
    private String mMilkType;

    private static OnDialogListener sListener;
    private static Feeding sFeeding;

    public static BottleFeedingDialog newInstance(Context ctx, OnDialogListener listener, Feeding feeding) {
        sContext = ctx;
        sListener = listener;
        sFeeding = feeding;
        return new BottleFeedingDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(sContext).inflate(R.layout.bottle_feeding_dialog, null);
        ButterKnife.bind(this, view);
        this.setElements();
        AlertDialog dialog = getAlertDialog(view);
        dialog.setOnShowListener(getShowListener());
        return dialog;
    }

    private AlertDialog getAlertDialog(View view) {
        if(sFeeding == null) {
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
                sListener.onDeleteSelected(sFeeding);
            }
        };
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.mMilkType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private DialogInterface.OnShowListener getShowListener() {
        return new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mTvQuantity.getText().toString().equals("")) {
                            int quantity = Integer.parseInt(mTvQuantity.getText().toString());
                            String details = mTvDetails.getText().toString().equals("") ? null : mTvDetails.getText().toString();
                            Date date = sFeeding == null ? new Date() : sFeeding.getStartDateTime();
                            Feeding feeding = new Feeding(0,
                                    FeedingType.BOTTLE_FEEDING,
                                    details, date, null, quantity,
                                    MilkType.getMilkType(mMilkType, sContext));
                            if(sFeeding == null) {
                                sListener.onSaveSelected(feeding);
                            } else {
                                feeding.setId(sFeeding.getId());
                                sListener.onEditSelected(feeding);
                            }
                            dialog.dismiss();
                            return;
                        }
                        Toast.makeText(sContext, R.string.msg_provide_quantity, Toast.LENGTH_SHORT).show();
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

    private void setElements() {
        this.mMilkTypeSpinner.setOnItemSelectedListener(this);
        final List<String> milkTypes = MilkType.getValues(sContext);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(sContext, android.R.layout.simple_list_item_1, milkTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mMilkTypeSpinner.setAdapter(adapter);
        if(sFeeding != null) {
            this.mTvDetails.setText(sFeeding.getDetails());
            this.mTvQuantity.setText(String.valueOf(sFeeding.getQuantity()));
            int spinnerPos = adapter.getPosition(sFeeding.getMilkType().getName(sContext));
            mMilkTypeSpinner.setSelection(spinnerPos);
        }
    }
}