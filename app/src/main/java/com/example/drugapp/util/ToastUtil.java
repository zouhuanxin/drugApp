package com.example.drugapp.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void toast(final Activity activity,final String s){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,s,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
