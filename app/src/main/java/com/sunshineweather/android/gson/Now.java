package com.sunshineweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2019/03/28 0028.
 */

public class Now {
    @SerializedName("cond_txt")
    public String coundition;
    @SerializedName("hum")
    public int humidity;
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("wind_dir")
    public String windDirection;
    @SerializedName("wind_sc")
    public String windScale;
    public String fl;

}
