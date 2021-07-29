package com.dot3digital.ui.real.control;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.dot3digital.R;

/**
 * @description     Define Global Dialogs
 *
 * @author          Stelian
 */
public class dot3Dialog {
    public enum DIALOG_REASON {
        NOT_CONNECTED_NETWORK,
        NOT_ENABLED_BLUETOOTH
    };

    /**
     * Show Warning Dialog
     *
     * @param context
     * @param reason
     */
    public static void showWarningDialog(Context context, DIALOG_REASON reason) {
        int msgId;

        switch (reason) {
            case NOT_CONNECTED_NETWORK: msgId = R.string.warning_dialog_network_connectivity; break;
            case NOT_ENABLED_BLUETOOTH: msgId = R.string.warning_dialog_bluetooth_message; break;
            default: return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.app_name);
        builder.setMessage(msgId);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //finish(); //TODO? finish current activity
                System.exit(0);
            }
        });
        builder.show();
    }
}
