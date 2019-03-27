package com.sunshineweather.android.db;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Administrator on 2019/03/27 0027.
 */

public class Country extends LitePalSupport {

    private int id;
    private int countryCode;
    private String countryName;
    private String weatherId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
