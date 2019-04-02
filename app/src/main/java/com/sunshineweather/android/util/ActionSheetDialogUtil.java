package com.sunshineweather.android.util;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sunshineweather.android.R;
import com.sunshineweather.android.listener.OnSetAutoUpdateTimeListener;

/**
 * Created by Administrator on 2019/04/02 0002.
 */

public class ActionSheetDialogUtil {
    public static final String TYPE_ONE_HOUR_RADIO_BUTTON = "1";
    public static final String TYPE_THREE_HOURS_RADIO_BUTTON = "3";
    public static final String TYPE_SIX_HOURS_RADIO_BUTTON = "6";
    public static final String TYPE_TWELVE_HOURS_RADIO_BUTTON = "12";
    public static final String TYPE_ONE_DAY_RADIO_BUTTON = "24";

    private Context context;
    private OnSetAutoUpdateTimeListener listener;
    private DisplayMetrics displayMetrics;
    private Dialog dialog;
    private RadioGroup autoUpdateTimeRadioGroup;
    private RadioButton oneHourRadioButton;
    private RadioButton threeHoursRadioButton;
    private RadioButton sixHoursRadioButton;
    private RadioButton twelveHoursRadioButton;
    private RadioButton oneDayRadioButton;
    private Button cancleActionButton;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ActionSheetDialogUtil(Context context, OnSetAutoUpdateTimeListener listener){
        this.context = context;
        this.listener = listener;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    }

    public void buildActionSheetDialog(){
        View view = LayoutInflater.from(context).inflate(R.layout.action_sheet_dialog, null);
        view.setMinimumWidth(displayMetrics.widthPixels);
        autoUpdateTimeRadioGroup = view.findViewById(R.id.auto_update_time_radio_group);
        oneHourRadioButton = view.findViewById(R.id.one_hour_radio_button);
        threeHoursRadioButton = view.findViewById(R.id.three_hours_radio_button);
        sixHoursRadioButton = view.findViewById(R.id.six_hours_radio_button);
        twelveHoursRadioButton = view.findViewById(R.id.twelve_hours_radio_button);
        oneDayRadioButton = view.findViewById(R.id.one_day_radio_button);
        cancleActionButton = view.findViewById(R.id.cancel_action_button);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String radioType = sharedPreferences.getString("radio_button", null);

        if (radioType != null && radioType.equals("1")){
            oneHourRadioButton.setChecked(true);
        }else if (radioType != null && radioType.equals("3")){
            threeHoursRadioButton.setChecked(true);
        }else if (radioType != null && radioType.equals("6")){
            sixHoursRadioButton.setChecked(true);
        }else if (radioType != null && radioType.equals("12")){
            twelveHoursRadioButton.setChecked(true);
        }else if (radioType != null && radioType.equals("24")){
            oneDayRadioButton.setChecked(true);
        }

        dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.x = 0;
        layoutParams.y = 0;
        dialogWindow.setAttributes(layoutParams);

        cancleActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        autoUpdateTimeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.one_hour_radio_button:
                        listener.setAutoUpdateTime("1 小时", 1);
                        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                        editor.putString("radio_button", TYPE_ONE_HOUR_RADIO_BUTTON);
                        editor.apply();
                        dialog.dismiss();
                        break;
                    case R.id.three_hours_radio_button:
                        listener.setAutoUpdateTime("3 小时", 3);
                        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                        editor.putString("radio_button", TYPE_THREE_HOURS_RADIO_BUTTON);
                        editor.apply();
                        dialog.dismiss();
                        break;
                    case R.id.six_hours_radio_button:
                        listener.setAutoUpdateTime("6 小时", 6);
                        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                        editor.putString("radio_button", TYPE_SIX_HOURS_RADIO_BUTTON);
                        editor.apply();
                        dialog.dismiss();
                        break;
                    case R.id.twelve_hours_radio_button:
                        listener.setAutoUpdateTime("12 小时", 12);
                        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                        editor.putString("radio_button", TYPE_TWELVE_HOURS_RADIO_BUTTON);
                        editor.apply();
                        dialog.dismiss();
                        break;
                    case R.id.one_day_radio_button:
                        listener.setAutoUpdateTime("24 小时", 24);
                        editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                        editor.putString("radio_button", TYPE_ONE_DAY_RADIO_BUTTON);
                        editor.apply();
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void show(){
        if (dialog != null){
            dialog.show();
        }
    }
}
