package com.sunshineweather.android.gson;

/**
 * Created by Administrator on 2019/03/28 0028.
 */

public class AQI {
    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
        public String qlty;
    }
}
