package com.finzy.weathernow;

import android.app.Application;

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();

        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(e);
            }
        });
    }

    public void handleUncaughtException(Throwable e) {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically

        /*Intent intent = new Intent();
        intent.setAction("com.finzy.SEND_LOG");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity(intent);

        System.exit(1); // kill off the crashed app*/
    }
}