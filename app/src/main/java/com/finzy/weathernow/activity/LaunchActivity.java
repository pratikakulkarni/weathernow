package com.finzy.weathernow.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.finzy.weathernow.R;
import com.finzy.weathernow.models.PrefLocation;
import com.finzy.weathernow.utils.LocationPreferences;

import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.finzy.weathernow.utils.GPSUtil.LOCATION;
import static com.finzy.weathernow.utils.GPSUtil.askForGPS;

public class LaunchActivity extends AppCompatActivity {

    @BindView(R.id.fullscreen_content_controls)
    LinearLayout fullscreenContentControls;

    @BindView(R.id.button_next)
    Button butttonNext;

    private PrefLocation prefLocation;
    protected LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        registerReceiver(gpsReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (LocationPreferences.loadLocationPref(LaunchActivity.this) == null) {
                    if (ContextCompat.checkSelfPermission(LaunchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
                    } else {
                        getCurrentLocation();
                        enableNextActivity();
                    }
                } else {
                    enableNextActivity();
                    butttonNext.performClick();
                }

            }
        }, 2000);
    }

    public void enableNextActivity() {
        /*if (ContextCompat.checkSelfPermission(LaunchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            askForGPS(LaunchActivity.this);
        }*/
        fullscreenContentControls.setVisibility(View.VISIBLE);
        butttonNext.setOnClickListener(v -> {
            Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(LaunchActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LaunchActivity.this, permission)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(LaunchActivity.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(LaunchActivity.this, new String[]{permission}, requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        enableNextActivity();

        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //PrefLocation
                case 1:
                    askForGPS(LaunchActivity.this);
                    break;
            }
        }
    }

    public void getCurrentLocation() {
        butttonNext.setText("Getting Location... (Skip)");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) Objects.requireNonNull(this).getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    prefLocation = new PrefLocation();
                    prefLocation.setLatitide(location.getLatitude());
                    prefLocation.setLongitude(location.getLongitude());
                    LocationPreferences.saveLocationPref(LaunchActivity.this, prefLocation.getLatitide(),
                            prefLocation.getLongitude());

                    butttonNext.setText("Next");
                    butttonNext.performClick();
                    locationManager.removeUpdates(this);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gpsReceiver != null) {
            unregisterReceiver(gpsReceiver);
        }
    }

    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null &&
                    intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                getCurrentLocation();
            }
        }
    };
}