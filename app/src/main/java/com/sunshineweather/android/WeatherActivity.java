package com.sunshineweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sunshineweather.android.gson.Forecast;
import com.sunshineweather.android.gson.Weather;
import com.sunshineweather.android.util.HttpUtil;
import com.sunshineweather.android.util.Utility;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private TextView titleCityText;
    private TextView titleUpdateTimeText;
    private TextView nowTemperatureText;
    private TextView nowConditionText;
    private TextView nowMaxMinTemperatureText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView somatosensoryTemperatureText;
    private TextView humidityText;
    private TextView windDirectionText;
    private TextView windGradeText;
    private TextView comfortText;
    private TextView sportText;
    private TextView carWashText;
    private ScrollView weatherLayout;
    private ImageView bingPicImg;
    public SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private String mUpdateTime;
    public DrawerLayout drawerLayout;
    private Button navButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        titleCityText = (TextView) findViewById(R.id.title_city_text);
        titleUpdateTimeText = (TextView) findViewById(R.id.title_update_time_text);
        nowTemperatureText = (TextView) findViewById(R.id.now_temperature_text);
        nowConditionText = (TextView) findViewById(R.id.now_condition_text);
        nowMaxMinTemperatureText = (TextView) findViewById(R.id.now_max_min_temperature_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_item);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm2_5_text);
        somatosensoryTemperatureText = (TextView) findViewById(R.id.somatosensory_temperature_text);
        humidityText = (TextView) findViewById(R.id.humidity_text);
        windDirectionText = (TextView) findViewById(R.id.wind_direction_text);
        windGradeText = (TextView) findViewById(R.id.wind_grade_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = sharedPreferences.getString("weather", null);
        String bingPic = sharedPreferences.getString("bing_pic", null);

        if (bingPic != null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }

        if (weatherString != null){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            mUpdateTime = sharedPreferences.getString("updatetime", null);
            showWeatherInfo(weather);
        }else {
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void showWeatherInfo(Weather weather){
        String cityName = weather.basic.cityName;

        String nowTemperature = weather.now.temperature + "℃";
        String nowCondition = weather.now.coundition;
        String nowMaxMinTemperature = weather.forecastList.get(0).temperature.max + "℃ / " + weather.forecastList.get(0).temperature.min + "℃";
        titleCityText.setText(cityName);
        titleUpdateTimeText.setText(mUpdateTime);
        nowTemperatureText.setText(nowTemperature);
        nowConditionText.setText(nowCondition);
        nowMaxMinTemperatureText.setText(nowMaxMinTemperature);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView forecastItemDateText = view.findViewById(R.id.forecast_item_date_text);
            TextView forecastItemConditionText = view.findViewById(R.id.forecast_item_condition_text);
            TextView forecastItemMaxMinTemperature = view.findViewById(R.id.forecast_item_max_min_temperature_text);
            forecastItemDateText.setText(forecast.date);
            forecastItemConditionText.setText(forecast.cond.condition);
            forecastItemMaxMinTemperature.setText(forecast.temperature.max + "℃ / " + forecast.temperature.min + "℃");
            forecastLayout.addView(view);
        }
        aqiText.setText(weather.aqi.city.aqi);
        pm25Text.setText(weather.aqi.city.pm25);
        humidityText.setText(String.valueOf(weather.now.humidity));
        somatosensoryTemperatureText.setText(weather.now.fl);
        windDirectionText.setText(weather.now.windDirection);
        windGradeText.setText(weather.now.windScale);
        comfortText.setText("舒适度：" + weather.suggestion.comf.txt);
        sportText.setText("运动建议：" + weather.suggestion.sport.txt);
        carWashText.setText("洗车建议：" + weather.suggestion.cw.txt);
        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this,AutoUpdateService.class);
        startService(intent);
    }

    public void requestWeather(String weatherId){
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=f0edd530f63d4cb1b83b5eab626d536d";
        HttpUtil.sendRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.putString("updatetime", dateFormat.format(System.currentTimeMillis()));
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            mUpdateTime = dateFormat.format(System.currentTimeMillis());
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    private void loadBingPic(){
        String bingPicUrl = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendRequest(bingPicUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(WeatherActivity.this, "图片获取失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }
}
