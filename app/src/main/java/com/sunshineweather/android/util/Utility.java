package com.sunshineweather.android.util;


import android.text.TextUtils;

import com.sunshineweather.android.db.City;
import com.sunshineweather.android.db.Country;
import com.sunshineweather.android.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2019/03/27 0027.
 */

public class Utility {

    public static boolean handleProvinceResponse(String response){
        try {
            JSONArray allProvinces = new JSONArray(response);
            for (int i=0; i<allProvinces.length(); i++){
                JSONObject provinceObject = allProvinces.getJSONObject(i);
                Province province = new Province();
                province.setProvinceCode(provinceObject.getInt("id"));
                province.setProinceName(provinceObject.getString("name"));
                province.save();

            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean handleCitiesResponse(String response, int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i=0; i<allCities.length(); i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityCode(cityObject.getInt("id"));
                    city.setCityName(cityObject.getString("name"));
                    city.setProvinceId(provinceId);
                    city.save();

                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    public static boolean handleCountryResponse(String response, int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCountries = new JSONArray(response);
                for (int i=0; i<allCountries.length(); i++){
                    JSONObject countryObject = allCountries.getJSONObject(i);
                    Country country = new Country();
                    country.setCountryCode(countryObject.getInt("id"));
                    country.setCountryName(countryObject.getString("name"));
                    country.setWeatherId(countryObject.getString("weather_id"));
                    country.setCityId(cityId);
                    country.save();

                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
