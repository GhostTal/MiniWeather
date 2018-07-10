package com.nick.miniweather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nick.miniweather.util.NetUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public class MainActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "WeatherMainActivity";
    private ImageView mUpdateBtn;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);
    }


    /**
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.title_update_btn) {
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code", "101010100");//北京
            Log.d(TAG, cityCode);

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE) {
                Log.d(TAG, "网络ok");
                queryWeatherCode(cityCode);
                Toast.makeText(MainActivity.this, "网络OK", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "网络异常");
                Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * @param cityCode
     */
    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d(TAG, address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    //打开和URL之间的链接
                    connection = (HttpURLConnection) url.openConnection();
                    //设置通用的请求
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    //该URL对应的资源的输入流
                    InputStream in = connection.getInputStream();
                    //定义BufferedReader输入流来获取URL数据
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder reponse = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        reponse.append(str);
                        Log.d(TAG, str);
                    }
                    String reponseStr = reponse.toString();
                    Log.d(TAG, reponseStr);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}