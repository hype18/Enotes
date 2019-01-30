package com.example.hype.enotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by hype on 29.01.19.
 * класс по созданию диалога в фрагменте
 */

public class DialogInfo extends DialogFragment implements DialogInterface.OnClickListener {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("Инфо").setPositiveButton("Ок", this)
                .setMessage(R.string.info_mess);
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                break;
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
