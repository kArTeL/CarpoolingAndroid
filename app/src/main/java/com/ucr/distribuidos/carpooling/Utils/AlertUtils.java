package com.ucr.distribuidos.carpooling.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.ucr.distribuidos.carpooling.R;

/**
 * Created by Neil on 11/23/16.
 */

public class AlertUtils {
    /**Error dialog with R.String */
    public static void showErrorDialog(final int title,final int messageRes,final Activity activity) {


        showErrorDialog(activity.getString(title),activity.getString(messageRes), activity);
    }

    /**Error dialog with String */
    public static void showErrorDialog(final String title,final String message,final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(activity.getString(R.string.accept_text), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}
