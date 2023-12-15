package com.mycompany.organaiser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;

public class DialogConfirmationImpl implements DialogConfirmationer {

    Context context;

    String nameTitle;
    String nameMessage;
    String namePositive;
    String nameNegative;

    InnerDialogRunner runner;

    public DialogConfirmationImpl(Context context){
        this.context = context;
    }

    @Override
    public void showDialogConfirmation(
            String nameTitle,
            String nameMessage,
            String namePositive,
            String nameNegative,
            InnerDialogRunner runner
    ) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(nameTitle);
        dialogBuilder.setMessage(nameMessage);
        dialogBuilder.setPositiveButton(namePositive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                runner.start();

            }
        });
        dialogBuilder.setNegativeButton(nameNegative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}
