package com.finzy.weathernow.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.finzy.weathernow.R;
import com.finzy.weathernow.fragment.ForecastFragment;
import com.finzy.weathernow.fragment.WeatherFragment;

import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity implements WeatherFragment.OnFragmentInteractionListener,
        ForecastFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.fragment_navigation_host).navigateUp();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
