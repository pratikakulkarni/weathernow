package com.finzy.weathernow.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.finzy.weathernow.R;

public class NetworkUtil {

    private static Dialog dialog;

    public static void noConnectivityDialog(Activity context) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_common);

        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        TextView tvMessage = dialog.findViewById(R.id.tv_message);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        TextView tvConfirm = dialog.findViewById(R.id.tv_confirm);

        tvTitle.setText(context.getResources().getString(R.string.data_connectivity));
        tvMessage.setText(context.getResources().getString(R.string.data_connectivity_message));
        tvConfirm.setText(context.getResources().getString(R.string.ok_button));

        tvCancel.setVisibility(View.GONE);

        tvConfirm.setOnClickListener(v -> dialog.dismiss());

        if (!context.isFinishing())
            dialog.show();
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*public boolean isConnectedToInternet(Context context) {
        boolean isConnected = false;

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        isConnected = true;
                    }

        }
        return isConnected;
    }*/
}
