package com.nick.miniweather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nick.miniweather.bean.TodayWeather;
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
import java.net.URL;

/**
 *
 */
public class MainActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "WeatherMainActivity";
    public static final int  UPDATE_TODAY_WEATHER = 1;
    private ImageView mUpdateBtn;
    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv,
            temperatureTv, climateTv, windTv, winddirTv, city_name_tv;
    private ImageView weatherImg, pmImg;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);

        initView();
    }

    /**
     * 初始化天气数据
     */
    void initView() {
        city_name_tv = (TextView) findViewById(R.id.title_city_name);//标题城市天气
        cityTv = (TextView) findViewById(R.id.city);//城市
        timeTv = (TextView) findViewById(R.id.time);//更新时间
        humidityTv = (TextView) findViewById(R.id.humidity);//湿度
        weekTv = (TextView) findViewById(R.id.week_today);//星期几
        pmDataTv = (TextView) findViewById(R.id.pm_data);//pm2.5数据
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);//pm2.5质量
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);//pm2.5图片
        temperatureTv = (TextView) findViewById(R.id.temperature);//温度
        climateTv = (TextView) findViewById(R.id.climate);//天气
        windTv = (TextView) findViewById(R.id.wind);//风力
        winddirTv = (TextView) findViewById(R.id.winddir);//风向
        weatherImg = (ImageView) findViewById(R.id.weather_img);//天气图片

        city_name_tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
        winddirTv.setText("N/A");
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
                TodayWeather todayWeather = null;
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
                    todayWeather = parseXML(reponseStr);
                    if (todayWeather != null) {
                        Log.d(TAG, todayWeather.toString());
                        Message msg = new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj = todayWeather;
                        mHandler.sendMessage(msg);
                    }
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

    /**
     * 解析XML
     *
     * @param xmldata
     */
    private TodayWeather parseXML(String xmldata) {


        TodayWeather todayWeather = null;
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

                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather == null)
                            break;

                        if (xmlPullParser.getName().equals("city")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setCity(xmlPullParser.getText());
                            Log.d(TAG, "city: " + xmlPullParser.getText());

                        } else if (xmlPullParser.getName().equals("updatetime")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setUpdatetime(xmlPullParser.getText());
                            Log.d(TAG, "updatetime: " + xmlPullParser.getText());

                        } else if (xmlPullParser.getName().equals("shidu")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setHumidity(xmlPullParser.getText());
                            Log.d(TAG, "shidu: " + xmlPullParser.getText());

                        } else if (xmlPullParser.getName().equals("wendu")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setTemperature(xmlPullParser.getText());
                            Log.d(TAG, "wendu: " + xmlPullParser.getText());

                        } else if (xmlPullParser.getName().equals("pm25")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setPm25(xmlPullParser.getText());
                            Log.d(TAG, "pm2.5: " + xmlPullParser.getText());

                        } else if (xmlPullParser.getName().equals("quality")) {
                            eventType = xmlPullParser.next();
                            todayWeather.setQuality(xmlPullParser.getText());
                            Log.d(TAG, "quality: " + xmlPullParser.getText());

                        } else if (xmlPullParser.getName().equals("fengxiang") && windDirCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setWinddir(xmlPullParser.getText());
                            Log.d(TAG, "fengxiang: " + xmlPullParser.getText());
                            windDirCount++;

                        } else if (xmlPullParser.getName().equals("fengli") && windPowerCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setWindpower(xmlPullParser.getText());
                            Log.d(TAG, "fengli: " + xmlPullParser.getText());
                            windPowerCount++;

                        } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setDate(xmlPullParser.getText());
                            Log.d(TAG, "date: " + xmlPullParser.getText());
                            dateCount++;

                        } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                            Log.d(TAG, "high: " + xmlPullParser.getText());
                            highCount++;

                        } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                            Log.d(TAG, "low: " + xmlPullParser.getText());
                            lowCount++;

                        } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                            eventType = xmlPullParser.next();
                            todayWeather.setType(xmlPullParser.getText());
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
        return todayWeather;
    }

    /**
     * @param todayWeather
     */
    private void updateTodayWeather(TodayWeather todayWeather) {
        city_name_tv.setText(todayWeather.getCity() + "天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        humidityTv.setText(todayWeather.getHumidity());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh() + "~" + todayWeather.getLow());
        humidityTv.setText("湿度" + todayWeather.getHumidity());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力:" + todayWeather.getWindpower());
        winddirTv.setText("风向:" + todayWeather.getWinddir());

        String weatherType = todayWeather.getType();
        updateTodayWeatherImage(weatherType);

        String pmData = todayWeather.getPm25();
        updatePmImage(Integer.parseInt(pmData));

        Toast.makeText(MainActivity.this, "更新成功！", Toast.LENGTH_LONG).show();

    }


    /**
     * update TodayWeather Image
     *
     * @param weatherType
     */
    private void updateTodayWeatherImage(String weatherType) {

        if (weatherType.contains("晴")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
        } else if (weatherType.contains("小雨")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        } else if (weatherType.contains("雷阵雨")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        } else if (weatherType.contains("多云")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        }  else if (weatherType.contains("阴")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
        } else if (weatherType.contains("中雨")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        } else if (weatherType.contains("大雨")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
        } else if (weatherType.contains("大暴雨")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        } else if (weatherType.contains("特大暴雨")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        } else if (weatherType.contains("雨夹雪")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        } else if (weatherType.contains("雷阵雨冰雹")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        } else if (weatherType.contains("小雪")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        } else if (weatherType.contains("中雪")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        } else if (weatherType.contains("阵雪")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        } else if (weatherType.contains("大雪")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
        } else if (weatherType.contains("暴雪")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        } else if (weatherType.contains("雾")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
        } else if (weatherType.contains("沙尘暴")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        }
    }

    /**
     * update pm2.5 Image
     *
     * @param pmData
     */
    private void updatePmImage(int pmData) {
        if (pmData >= 0 && pmData <= 50) {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
        } else if (pmData >= 51 && pmData <= 100) {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
        } else if (pmData >= 101 && pmData <= 150) {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
        } else if (pmData >= 151 && pmData <= 200) {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
        } else if (pmData >= 201 && pmData <= 300) {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
        } else if (pmData >= 300) {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
        }
    }
}