package com.dot3digital.ui.real.fragment;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.dot3digital.R;

/**
 * @description Base Fragment
 *              This fragment contains common function
 *
 * @author      Stelian
 */

public abstract class BaseFragment extends Fragment
{
    // Params
    public final static String PARAM_VIEW_KEY = "viewKey";
    public final static String PARAM_VIEW_CATE_KEY = "viewCateKey";

    // Variables
    View mRootView;

    /**
     * Fail to load dialog
     */
    protected void showLoadFailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.failed_loading_data);
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.dialog_ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Show Toast Message
     *
     * @param res_id
     */
    protected void showToast(int res_id) {
        Toast.makeText(getActivity(), res_id, Toast.LENGTH_LONG).show();
    }
}
