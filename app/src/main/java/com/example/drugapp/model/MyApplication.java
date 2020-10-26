package com.example.drugapp.model;

import android.app.Application;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {
    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        Bmob.initialize(this, "43383671c9998c43620ecd69c3f0996f");
    }

    public static MyApplication getMyApplication() {
        return myApplication;
    }
}
