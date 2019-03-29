package com.sunshineweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2019/03/28 0028.
 */

public class Forecast {
    public String date;
    public Cond cond;
    @SerializedName("tmp")
    public Temperature temperature;

    public class Cond{
        @SerializedName("txt_d")
        public String condition;
    }

    public class Temperature{
        public String max;
        public String min;
    }
}
