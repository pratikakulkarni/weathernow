package com.finzy.weathernow.viewmodel.factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import com.finzy.weathernow.viewmodel.MainActivityViewModel;

public class MainActivityFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private Context context;


    public MainActivityFactory(Application application, Context context) {
        this.mApplication = application;
        this.context = context;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MainActivityViewModel(mApplication, context);
    }
}