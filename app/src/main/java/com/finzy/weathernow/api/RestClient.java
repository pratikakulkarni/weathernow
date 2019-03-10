package com.finzy.weathernow.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import android.util.Log;
import com.finzy.weathernow.BuildConfig;
import com.finzy.weathernow.R;
import com.finzy.weathernow.utils.ConnectivityInterceptor;
import com.finzy.weathernow.utils.CustomDialog;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT;

public class RestClient {

    public static String TAG = RestClient.class.getCanonicalName();
    private static APIServices REST_CLIENT;
    private Context mContext;

    private boolean isErrorToBeShown = true;

    private RestClient(Context context) {
        mContext = context;
        init();
    }

    private RestClient(Context context, boolean isErrorToBeShown) {
        mContext = context;
        this.isErrorToBeShown = isErrorToBeShown;
        init();
    }

    public static RestClient getInstance(Context context) {
        //if (sInstance == null)
        return new RestClient(context);
    }

    public static RestClient getInstance(Context context, boolean isErrorToBeShown) {
        //if (sInstance == null)
        return new RestClient(context, isErrorToBeShown);
    }

    private void init() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        Interceptor interceptCode = chain -> {
            Request request = chain.request();
            Response response = chain.proceed(request);
            MediaType contentType = response.body().contentType();

            String bodyString = response.body().string();

            if (mContext instanceof Activity) {
                try {
                    if (isErrorToBeShown) {
                        if (response.code() == 500) {
                            try {
                                final JSONObject jsonObject = new JSONObject(bodyString);
                                if (jsonObject.has("errorMsg")) {
                                    final String err = jsonObject.getString("errorMsg");
                                    ((Activity) mContext).runOnUiThread(() -> CustomDialog.commonDialog(((Activity) mContext), mContext.getResources().getString(R.string.message),
                                            err,
                                            mContext.getResources().getString(R.string.ok_button), null));
                                }
                            } catch (Exception e) {
                                ((Activity) mContext).runOnUiThread(() -> CustomDialog.commonDialog(((Activity) mContext), mContext.getResources().getString(R.string.message),
                                        mContext.getString(R.string.internal_server),
                                        mContext.getResources().getString(R.string.ok_button), null));
                            }
                        } else if (response.code() == 401) {
                            CustomDialog.commonDialog((Activity) mContext,
                                    mContext.getResources().getString(R.string.unauthenticated),
                                    mContext.getResources().getString(R.string.unauthenticated_error),
                                    mContext.getResources().getString(R.string.ok_button), null, null, null);
                        } else if (!(response.code() == 200)) {
                            final JSONObject jsonObject = new JSONObject(bodyString);
                            if (jsonObject.has("message")) {
                                final String err = jsonObject.getString("message");
                                ((Activity) mContext).runOnUiThread(() -> CustomDialog.commonDialog(((Activity) mContext), mContext.getResources().getString(R.string.message),
                                        err,
                                        mContext.getResources().getString(R.string.ok_button), null));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }

            ResponseBody body = ResponseBody.create(contentType, bodyString);
            return response.newBuilder().body(body).build();
        };

        Interceptor timeoutInterceptor = chain -> {
            try {
                Request request = chain.request();
                Response response = chain.proceed(request);
                MediaType contentType = response.body().contentType();
                String bodyString = response.body().string();
                ResponseBody body = ResponseBody.create(contentType, bodyString);
                return response.newBuilder().body(body).build();
            } catch (SocketTimeoutException exception) {
                exception.printStackTrace();
                Log.e(TAG, exception.getMessage());
                if (mContext instanceof Activity) {
                    if (isErrorToBeShown) {
                        ((Activity) mContext).runOnUiThread(() -> CustomDialog.commonDialog(((Activity) mContext), mContext.getResources().getString(R.string.message),
                                mContext.getResources().getString(R.string.connection_timeout_message),
                                mContext.getResources().getString(R.string.ok_button), v -> {
                                    Intent intent = new Intent(CONNECTION_TIMEOUT);
                                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                }));
                    }
                }
            }

            return chain.proceed(chain.request());
        };

        /**
         * LOGGING LEVEL FOR APIs
         */
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }


        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .hostnameVerifier((hostname, session) -> true)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(timeoutInterceptor)
                    .addInterceptor(interceptor)
                    .addInterceptor(interceptCode);

            if (mContext instanceof Activity) {
                builder.addInterceptor(new ConnectivityInterceptor((Activity) mContext));
            }

            OkHttpClient client = builder.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            REST_CLIENT = retrofit.create(APIServices.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static APIServices get() {
        return REST_CLIENT;
    }
}