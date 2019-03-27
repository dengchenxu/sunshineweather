package com.sunshineweather.android.db;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Administrator on 2019/03/27 0027.
 */

public class Province extends LitePalSupport {

    private int id;
    private int provinceCode;
    private String proinceName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProinceName() {
        return proinceName;
    }

    public void setProinceName(String proinceName) {
        this.proinceName = proinceName;
    }
}
