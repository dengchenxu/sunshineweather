package com.sunshineweather.android.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sunshineweather.android.R;


/**
 * Created by Administrator on 2019/03/28 0028.
 */

public class ProgressDialogUtil {
    private static AlertDialog mAlertDialog;

    public static void showProgressDialog(Context context){
        if (mAlertDialog == null){
            mAlertDialog = new AlertDialog.Builder(context, R.style.CustomProgressDialog).create();
        }

        View loadView = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog, null);
        mAlertDialog.setView(loadView, 0, 0, 0, 0);
        mAlertDialog.setCanceledOnTouchOutside(false);

        TextView tvTip = loadView.findViewById(R.id.tv_tip);
        tvTip.setText("加载中...");
        mAlertDialog.show();
    }

    public static void showProgressDialog(Context context, String tip){
        if (TextUtils.isEmpty(tip)){
            tip = "加载中...";
        }

        if (mAlertDialog == null){
            mAlertDialog = new AlertDialog.Builder(context, R.style.CustomProgressDialog).create();
        }

        View loadView = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog, null);
        mAlertDialog.setView(loadView);
        mAlertDialog.setCanceledOnTouchOutside(false);
        TextView tvTip = loadView.findViewById(R.id.tv_tip);
        tvTip.setText(tip);
        mAlertDialog.show();
    }

    public static void dismiss(){
        if (mAlertDialog != null && mAlertDialog.isShowing()){
            mAlertDialog.dismiss();
        }
    }
}
