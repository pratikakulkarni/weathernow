package com.finzy.weathernow.api;

import com.finzy.weathernow.api.response.CityInfoRes;
import com.finzy.weathernow.api.response.ForecastRes;
import com.finzy.weathernow.api.response.WeatherRes;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIServices {


    @GET("weather")
    Call<WeatherRes> getCurrentWeather(@Query("appid") String appId,
                                       @Query(value = "units") String unit,
                                       @Query(value = "lat") double lat,
                                       @Query(value = "lon") double lon);

    @GET("forecast")
    Call<ForecastRes> getForecastedWeather(@Query("appid") String appId,
                                           @Query(value = "units") String unit,
                                           @Query(value = "lat") double lat,
                                           @Query(value = "lon") double lon);

    @GET("find")
    Call<CityInfoRes> findCities(@Query("appid") String appId,
                                 @Query(value = "q") String query,
                                 @Query(value = "type") String type,
                                 @Query(value = "sort") String sort,
                                 @Query(value = "units") String units);
}