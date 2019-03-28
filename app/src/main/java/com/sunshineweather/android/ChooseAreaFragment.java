package com.sunshineweather.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sunshineweather.android.db.City;
import com.sunshineweather.android.db.Country;
import com.sunshineweather.android.db.Province;
import com.sunshineweather.android.util.HttpUtil;
import com.sunshineweather.android.util.ProgressDialogUtil;
import com.sunshineweather.android.util.Utility;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2019/03/27 0027.
 */

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTRY = 2;
    //private ProgressBar progressBar;
    private TextView titleText;
    private Button backButton;
    private ListView showAreas;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<Country> countryList;
    private Province selectProvince;
    private City selectCity;
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        showAreas = view.findViewById(R.id.show_areas);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList);
        showAreas.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showAreas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (currentLevel == LEVEL_PROVINCE){
                    selectProvince = provinceList.get(position);
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    selectCity = cityList.get(position);
                    queryCountries();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLevel == LEVEL_COUNTRY){
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = LitePal.findAll(Province.class);
        if (provinceList.size() > 0){
            dataList.clear();
            for (Province province : provinceList){
                dataList.add(province.getProinceName());
            }
            adapter.notifyDataSetChanged();
            showAreas.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
            Log.d("ChooseArea", String.valueOf(currentLevel));
        }else {
            String url = "http://guolin.tech/api/china";
            queryFromServer(url, "province");
        }
    }

    private void queryCities(){
        titleText.setText(selectProvince.getProinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = LitePal.where("provinceid = ?", String.valueOf(selectProvince.getId())).find(City.class);
        if (cityList.size() > 0){
            dataList.clear();
            for (City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            showAreas.setSelection(0);
            currentLevel = LEVEL_CITY;
            Log.d("ChooseArea", String.valueOf(currentLevel));
        }else {
            String url = "http://guolin.tech/api/china/" + selectProvince.getProvinceCode();
            queryFromServer(url, "city");
        }
    }

    private void queryCountries(){
        titleText.setText(selectCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countryList = LitePal.where("cityid = ?", String.valueOf(selectCity.getId())).find(Country.class);
        if (countryList.size() > 0){
            dataList.clear();
            for (Country country : countryList){
                dataList.add(country.getCountryName());
            }
            adapter.notifyDataSetChanged();
            showAreas.setSelection(0);
            currentLevel = LEVEL_COUNTRY;
            Log.d("ChooseArea", String.valueOf(currentLevel));
        }else {
            String url = "http://guolin.tech/api/china/" + selectProvince.getProvinceCode() + "/" + selectCity.getCityCode();
            queryFromServer(url, "country");
        }
    }

    private void queryFromServer(String url, final String type){
        showProgressDialog();
        HttpUtil.sendRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDailog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if ("city".equals(type)){
                    result = Utility.handleCitiesResponse(responseText, selectProvince.getId());
                }else if ("country".equals(type)){
                    result = Utility.handleCountryResponse(responseText, selectCity.getId());
                }
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDailog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("country".equals(type)){
                                queryCountries();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog(){
        ProgressDialogUtil.showProgressDialog(getContext());
    }

    private void closeProgressDailog(){
        ProgressDialogUtil.dismiss();
    }
}
