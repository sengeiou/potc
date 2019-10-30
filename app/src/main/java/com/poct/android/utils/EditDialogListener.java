package com.poct.android.utils;

import android.content.DialogInterface;
import android.widget.EditText;

public class EditDialogListener implements DialogInterface.OnClickListener  {

    public EditText editText;

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
