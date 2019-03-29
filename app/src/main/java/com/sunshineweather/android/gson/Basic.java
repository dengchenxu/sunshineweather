package com.sunshineweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2019/03/28 0028.
 */

public class Basic {
    @SerializedName("id")
    public String weatherId;
    @SerializedName("city")
    public String cityName;
    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
