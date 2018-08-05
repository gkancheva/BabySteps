package com.company.babysteps.services;

import com.company.babysteps.entities.SelectableEntity;

public interface OnDialogListener {
    void onSaveSelected(SelectableEntity entity);
    void onEditSelected(SelectableEntity entity);
    void onDeleteSelected(SelectableEntity entity);
}