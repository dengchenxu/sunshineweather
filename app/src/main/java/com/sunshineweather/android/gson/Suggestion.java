package com.sunshineweather.android.gson;

/**
 * Created by Administrator on 2019/03/28 0028.
 */

public class Suggestion {
    public LifeStyle comf;
    public LifeStyle sport;
    public LifeStyle cw;

    public class LifeStyle{
        public String type;
        public String brf;
        public String txt;
    }
}
