package com.finzy.weathernow.utils;

import android.app.Activity;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class ConnectivityInterceptor implements Interceptor {

    private Activity mContext;

    public ConnectivityInterceptor(Activity context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!new NetworkUtil().isConnectedToInternet(mContext)) {
            try {
                mContext.runOnUiThread(() -> {
                    NetworkUtil.noConnectivityDialog(mContext);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new NoConnectivityException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }
}