package com.finzy.weathernow;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;
import com.finzy.weathernow.api.response.ForecastRes;
import com.finzy.weathernow.models.PrefLocation;
import com.finzy.weathernow.repo.ForecastRepo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WeatherForecastUnitTest {

    @Mock
    private Context contextMock;

    @Mock
    LifecycleOwner lifecycleOwner;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        lifecycleOwner = Mockito.mock(LifecycleOwner.class);
    }

    @Test
    public void isAPI_Correct() {

        LiveData<ForecastRes> livaData = ForecastRepo.getInstance(contextMock.getApplicationContext())
                .getForecastedWeather(new PrefLocation(12.9716, 77.5946));


        livaData.observe(lifecycleOwner, forecastRes -> {
            assertNotNull(forecastRes);
            assertNotNull(forecastRes.getCity());
            assertEquals(forecastRes.getCity(), "Bangalore");

        });
    }
}
