package com.nick.miniweather;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.nick.miniweather.util.NetUtil;

public class MainActivity  extends Activity{

    public static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE) {
            Log.d(TAG, "网络ok");
            Toast.makeText(MainActivity.this, "网络OK", Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "网络异常");
            Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_LONG).show();
        }
    }
}