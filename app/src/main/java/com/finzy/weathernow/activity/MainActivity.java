package com.finzy.weathernow.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import androidx.navigation.NavController;
import com.finzy.weathernow.R;
import com.finzy.weathernow.fragment.ForecastFragment;
import com.finzy.weathernow.fragment.WeatherFragment;

import com.finzy.weathernow.viewmodel.MainActivityViewModel;
import com.finzy.weathernow.viewmodel.factory.MainActivityFactory;
import com.finzy.weathernow.widget.RemoteFetchService;

public class MainActivity extends AppCompatActivity implements WeatherFragment.OnFragmentInteractionListener,
        ForecastFragment.OnFragmentInteractionListener {

    private boolean isLocationChanged = false;

    private LiveData<NavController> navControllerLiveData;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isLocationChanged = getIntent().getBooleanExtra(RemoteFetchService.LOCATION_CHANGED, false);

        Bundle bundle = new Bundle();
        bundle.putBoolean(RemoteFetchService.LOCATION_CHANGED, isLocationChanged);

        viewModel = ViewModelProviders
                .of(MainActivity.this, new MainActivityFactory(getApplication(), MainActivity.this))
                .get(MainActivityViewModel.class);
        navControllerLiveData = viewModel.getNavControllerLiveData(this, bundle);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}