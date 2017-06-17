package com.tcg.healthpointtracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by JoseR on 6/11/2017.
 */
public class TCGHelperMethods {

    public static void showSimpleAlertMessage(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getText(R.string.option_ok), (arg0, arg1) -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    public static int clamp(int n, int min, int max) {
        int r = n;
        if(n < min) {
            r = min;
        }
        if(n > max) {
            r = max;
        }
        return r;
    }

}
