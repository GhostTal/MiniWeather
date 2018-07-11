package com.nick.miniweather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nick.miniweather.util.NetUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
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
                    parseXML(reponseStr);
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

    private void parseXML(String xmldata) {

        int windDirCount = 0;
        int windPowerCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d(TAG, "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    //判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("city")) {
                            eventType = xmlPullParser.next();
                            Log.d(TAG, "city: " + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("updatetime")) {
                            eventType = xmlPullParser.next();
                            Log.d(TAG, "updatetime: " + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("shidu")) {
                            eventType = xmlPullParser.next();
                            Log.d(TAG, "shidu: " + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("wendu")) {
                            eventType = xmlPullParser.next();
                            Log.d(TAG, "wendu: " + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("pm25")) {
                            eventType = xmlPullParser.next();
                            Log.d(TAG, "pm2.5: " + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("quality")) {
                            eventType = xmlPullParser.next();
                            Log.d(TAG, "quality: " + xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("fengxiang") && windDirCount == 0) {
                            eventType = xmlPullParser.next();
                            Log.d(TAG, "fengxiang: " + xmlPullParser.getText());
                            windDirCount++;
                        } else if (xmlPullParser.getName().equals("fengli") && windPowerCount == 0) {
                            eventType = xmlPullParser.next();
                            Log.d(TAG, "fengli: " + xmlPullParser.getText());
                            windPowerCount++;
                        } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                            eventType = xmlPullParser.next();
                            Log.d(TAG, "date: " + xmlPullParser.getText());
                            dateCount++;
                        } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                            eventType = xmlPullParser.next();
                            Log.d(TAG, "high: " + xmlPullParser.getText());
                            highCount++;
                        } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                            eventType = xmlPullParser.next();
                            Log.d(TAG, "low: " + xmlPullParser.getText());
                            lowCount++;
                        } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                            eventType = xmlPullParser.next();
                            Log.d(TAG, "type: " + xmlPullParser.getText());
                            typeCount++;
                        }
                        break;
                    //判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                //进入下一个元素并处罚相应事件
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}