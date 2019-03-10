package com.finzy.weathernow.viewholder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.finzy.weathernow.R;
import com.finzy.weathernow.api.response.WInfo;
import com.finzy.weathernow.utils.TimeUtil;

public class ForecastViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textView_maxTemp)
    public TextView textView_maxTemp;
    @BindView(R.id.textView_Time)
    public TextView textView_Time;
    @BindView(R.id.textView_minTemp)
    public TextView textView_minTemp;
    @BindView(R.id.textView_Temp)
    public TextView textView_Temp;
    @BindView(R.id.textView_WindSpeed)
    public TextView textView_WindSpeed;
    @BindView(R.id.textView_Humidity)
    public TextView textView_Humidity;
    @BindView(R.id.textView_Weather)
    public TextView textView_Weather;


    private Activity activity;

    public ForecastViewHolder(View view, Activity activity) {
        super(view);
        this.activity = activity;
        ButterKnife.bind(this, view);
    }

    public void bindData(WInfo wInfo) {
        textView_maxTemp.setText(Double.toString(wInfo.getMain().getTempMax()) + (char) 0x00B0 + "C");
        textView_minTemp.setText(Double.toString(wInfo.getMain().getTempMin()) + (char) 0x00B0 + "C");
        textView_Temp.setText(Double.toString(wInfo.getMain().getTemp()) + (char) 0x00B0 + "C");
        textView_Time.setText(TimeUtil.getFormattedDate(wInfo.getDt()));
        textView_WindSpeed.setText(Double.toString(wInfo.getWind().getSpeed()) + " km/h");
        textView_Humidity.setText(Integer.toString(wInfo.getMain().getHumidity()) + "%");

        if (wInfo.getWeather().size() > 0) {
            textView_Weather.setVisibility(View.VISIBLE);
            textView_Weather.setText(wInfo.getWeather().get(0).getDescription());
        } else {
            textView_Weather.setVisibility(View.GONE);
        }
    }
}