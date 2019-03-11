package com.finzy.weathernow.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.finzy.weathernow.R;


public class CustomDialog {

    public interface TextEntered {
        void isTextEntered(String typedString);
    }

    /*public static void displayInfoDialog(final Activity activity, String title, String msg, String confirmButtonText) {
        CustomDialog.commonDialog(activity,
                title,
                msg,
                confirmButtonText,
                null, null, null);
    }*/

    public static void commonDialog(final Activity activity, String title, String msg, String confirmButtonText, View.OnClickListener onClickListener) {

        CustomDialog.commonDialog(activity,
                title,
                msg,
                confirmButtonText,
                onClickListener, null, null);
    }

    public static void commonDialog(final Activity activity, String title, String msg,
                                    String confirmButtonText, final View.OnClickListener confirmButtonClickListener,
                                    String cancelButtonText, final View.OnClickListener cancelButtonClickListener) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_common);

        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        TextView tvMessage = dialog.findViewById(R.id.tv_message);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        TextView tvConfirm = dialog.findViewById(R.id.tv_confirm);

        tvTitle.setText(title);
        tvMessage.setText(msg);

        if (cancelButtonText != null) {
            tvCancel.setVisibility(View.VISIBLE);
            tvCancel.setText(cancelButtonText);
            tvCancel.setOnClickListener(v -> {
                if (cancelButtonClickListener != null) {
                    cancelButtonClickListener.onClick(v);
                }
                if (!activity.isFinishing())
                    dialog.dismiss();

            });
        } else {
            tvCancel.setVisibility(View.GONE);
        }

        if (confirmButtonText != null) {
            tvConfirm.setVisibility(View.VISIBLE);
            tvConfirm.setText(confirmButtonText);
            tvConfirm.setOnClickListener(v -> {
                if (confirmButtonClickListener != null) {
                    confirmButtonClickListener.onClick(v);
                }
                if (!activity.isFinishing())
                    dialog.dismiss();

            });
        } else {
            tvConfirm.setVisibility(View.GONE);
        }

        if (!activity.isFinishing())
            dialog.show();
    }
}