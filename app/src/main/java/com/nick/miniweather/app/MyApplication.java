package com.nick.miniweather.app;

import android.app.Application;
import android.util.Log;

import com.nick.miniweather.db.CityDB;

public class MyApplication extends Application{
    private static final String TAG = "Weather";
    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyApplication ->Oncreate");
        myApplication = this;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    private CityDB openCityDB() {

    }
}