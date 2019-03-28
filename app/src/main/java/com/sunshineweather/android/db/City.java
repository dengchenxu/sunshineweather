package com.sunshineweather.android.db;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Administrator on 2019/03/27 0027.
 */

public class City extends LitePalSupport {

    private int id;
    private int cityCode;
    private String cityName;
    private int provinceId;

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
