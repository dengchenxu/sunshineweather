package com.sunshineweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sunshineweather.android.listener.OnSetAutoUpdateTimeListener;
import com.sunshineweather.android.util.ActionSheetDialogUtil;

public class SettingActivity extends AppCompatActivity {

    private Switch autoUpdateSwitch;
    private String switchStatus;
    private LinearLayout setAutoUpdateTime;
    private TextView autoUpdateTimeText;
    private TextView autoUpdateTimeText1;
    private int mUpdateTime = 6;

    private OnSetAutoUpdateTimeListener listener = new OnSetAutoUpdateTimeListener() {
        @Override
        public void setAutoUpdateTime(String autoUpdateTime, int i) {
            autoUpdateTimeText.setText(autoUpdateTime);
            mUpdateTime = i;
            startService(mUpdateTime);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        autoUpdateSwitch = (Switch) findViewById(R.id.auto_update_switch);
        setAutoUpdateTime = (LinearLayout) findViewById(R.id.set_auto_update_time);
        autoUpdateTimeText = (TextView) findViewById(R.id.auto_update_time);
        autoUpdateTimeText1 = (TextView) findViewById(R.id.auto_update_time_text1);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        switchStatus = sharedPreferences.getString("switch", null);
        String radioType = sharedPreferences.getString("radio_button", null);

        if (radioType != null && radioType.equals("1")){
            autoUpdateTimeText.setText("1 小时");
            mUpdateTime = 1;
        }else if (radioType != null && radioType.equals("3")){
            autoUpdateTimeText.setText("3 小时");
            mUpdateTime = 3;
        }else if (radioType != null && radioType.equals("6")){
            autoUpdateTimeText.setText("6 小时");
            mUpdateTime = 6;
        }else if (radioType != null && radioType.equals("12")){
            autoUpdateTimeText.setText("12 小时");
            mUpdateTime = 12;
        }else if (radioType != null && radioType.equals("24")){
            autoUpdateTimeText.setText("24 小时");
            mUpdateTime = 24;
        }else if (radioType == null){
            autoUpdateTimeText.setText("6 小时");
            mUpdateTime = 6;
        }

        if (switchStatus != null && "true".equals(switchStatus)){
            autoUpdateSwitch.setChecked(true);
            setAutoUpdateTime.setEnabled(true);
            autoUpdateTimeText1.setTextColor(Color.parseColor("#000000"));
            autoUpdateTimeText.setTextColor(Color.parseColor("#5b5b5b"));
        }else{
            autoUpdateSwitch.setChecked(false);
            setAutoUpdateTime.setEnabled(false);
            autoUpdateTimeText1.setTextColor(Color.parseColor("#adadad"));
            autoUpdateTimeText.setTextColor(Color.parseColor("#adadad"));
        }

        autoUpdateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isSwitchOn) {
                if (isSwitchOn){
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this).edit();
                    editor.putString("switch", String.valueOf(autoUpdateSwitch.isChecked()));
                    editor.apply();
                    startService(mUpdateTime);
                    setAutoUpdateTime.setEnabled(true);
                    autoUpdateTimeText1.setTextColor(Color.parseColor("#000000"));
                    autoUpdateTimeText.setTextColor(Color.parseColor("#5b5b5b"));
                    Toast.makeText(SettingActivity.this, "开启自动更新", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this).edit();
                    editor.putString("switch", String.valueOf(autoUpdateSwitch.isChecked()));
                    editor.apply();
                    Intent intent = new Intent(SettingActivity.this, AutoUpdateService.class);
                    stopService(intent);
                    setAutoUpdateTime.setEnabled(false);
                    autoUpdateTimeText1.setTextColor(Color.parseColor("#adadad"));
                    autoUpdateTimeText.setTextColor(Color.parseColor("#adadad"));
                    Toast.makeText(SettingActivity.this, "关闭自动更新", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setAutoUpdateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSheetDialogUtil actionSheetDialogUtil = new ActionSheetDialogUtil(SettingActivity.this, listener);
                actionSheetDialogUtil.buildActionSheetDialog();
                actionSheetDialogUtil.show();
            }
        });
    }

    private void startService(int updateTime){
        Intent intent = new Intent(SettingActivity.this, AutoUpdateService.class);
        intent.putExtra("time", updateTime);
        startService(intent);
    }
}
