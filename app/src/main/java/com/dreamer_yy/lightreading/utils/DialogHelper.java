package com.dreamer_yy.lightreading.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.dreamer_yy.lightreading.R;

/**
 * Created by Dreamer__YY on 2018/5/8.
 */

public class DialogHelper {
    public static Dialog getLoadingDialog(Context context){
        Dialog dialog = new Dialog(context, R.style.dialog);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_dialog, null);
        dialog.setContentView(dialogView);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setDimAmount(0.f);
        return dialog;
    }
}
