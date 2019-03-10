package com.finzy.weathernow.activity;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.finzy.weathernow.R;
import com.finzy.weathernow.adapter.CitySearchAdapter;
import com.finzy.weathernow.api.response.CityInfoRes;
import com.finzy.weathernow.api.response.WeatherRes;
import com.finzy.weathernow.utils.LocationPreferences;
import com.finzy.weathernow.viewmodel.CitySearchViewModel;
import com.finzy.weathernow.viewmodel.factory.CitySearchFactory;
import com.finzy.weathernow.widget.RemoteFetchService;

import java.util.ArrayList;

import static android.content.Intent.*;
import static com.finzy.weathernow.widget.RemoteFetchService.BROADCAST_ACTION;

public class CitySearchActivity extends AppCompatActivity {

    @BindView(R.id.editText_seachCity)
    EditText editText_seachCity;

    @BindView(R.id.recyclerView_Cities)
    RecyclerView recyclerView_Cities;

    @BindView(R.id.imageView_background)
    ImageView imageView_background;

    private CitySearchAdapter citySearchAdapter;
    private CitySearchViewModel viewModel;

    private LiveData<CityInfoRes> weatherResLiveData;

    //widget
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_search);

        ButterKnife.bind(this);

        Glide.with(this)
                .load(R.drawable.city_background)
                .centerCrop()
//                .apply((RequestOptions.bitmapTransform(new BlurTransformation(25, 1))))
                .into(imageView_background);

        viewModel = ViewModelProviders
                .of(CitySearchActivity.this, new CitySearchFactory(getApplication(), CitySearchActivity.this/*, editable.toString()*/))
                .get(CitySearchViewModel.class);

        setAdapter();

        editText_seachCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                weatherResLiveData = viewModel.getCities(editable.toString());
                weatherResLiveData.observe(CitySearchActivity.this, cities -> {
                    if (cities != null && weatherResLiveData != null && weatherResLiveData.getValue() != null) {
                        citySearchAdapter.setWeatherResList(weatherResLiveData.getValue().getList());
                        citySearchAdapter.notifyDataSetChanged();
                    } else {
                        citySearchAdapter.setWeatherResList(new ArrayList<>());
                        citySearchAdapter.notifyDataSetChanged();
                    }
                });
            }
        });


        setResult(RESULT_CANCELED);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        /*if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }*/
    }

    private void setAdapter() {
        ArrayList<WeatherRes> weatherRes = null;
        if (weatherResLiveData != null && weatherResLiveData.getValue() != null) {
            weatherRes = (ArrayList<WeatherRes>) weatherResLiveData.getValue().getList();
        }
        if (weatherRes == null) {
            weatherRes = new ArrayList<>();
        }

        citySearchAdapter = new CitySearchAdapter(weatherRes, this);

        recyclerView_Cities.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_Cities.setLayoutManager(layoutManager);
        recyclerView_Cities.setAdapter(citySearchAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.city_search_skip, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_skip:
                Intent intent = new Intent(CitySearchActivity.this, MainActivity.class);
                intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setPreferredLocation(WeatherRes weatherRes) {
        double lat = weatherRes.getCoord().getLat();
        double lon = weatherRes.getCoord().getLon();

        final Context context = CitySearchActivity.this;

        LocationPreferences.saveLocationPref(context, lat, lon);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

        // Build the intent to call the service
        Intent intent = new Intent(context.getApplicationContext(),
                RemoteFetchService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        intent.putExtra(RemoteFetchService.LOCATION_CHANGED, true);

        // Update the widgets via the service
        context.startService(intent);
    }

    public CitySearchActivity() {
        super();
    }

    public void notifyWidget() {
        /*Intent intent = new Intent(this, WeatherAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), WeatherAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);*/
        if (getIntent().getExtras() != null) {
            /*AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(CitySearchActivity.this);
            WeatherAppWidget.updateAppWidget(CitySearchActivity.this, appWidgetManager, mAppWidgetId);*/
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        } else {
            Intent mainAct = new Intent(CitySearchActivity.this, MainActivity.class);
            mainAct.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
            mainAct.putExtra(RemoteFetchService.LOCATION_CHANGED, true);
            startActivity(mainAct);
            finish();
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            notifyWidget();
        }
    };

    public void onResume() {
        super.onResume();
        if (broadcastReceiver != null)
            registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }
}