package com.sunshineweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2019/03/28 0028.
 */

public class Weather {
    public String status;
    public Basic basic;
    public Now now;
    public AQI aqi;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
