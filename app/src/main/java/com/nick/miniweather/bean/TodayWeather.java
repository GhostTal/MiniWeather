package com.nick.miniweather.bean;

import android.widget.ImageView;

public class TodayWeather {
    private String city;
    private String updatetime;
    private String temperature;
    private String humidity;
    private String pm25;
    private String quality;
    private String winddir;
    private String windpower;
    private String date;
    private String high;
    private String low;
    private String type;
    private ImageView weatherImg;
    private ImageView pmImg;

    public String getCity() {
        return city;
    }

    public String getUpdatetime() {
        return updatetime;
    }


    public String getHumidity() {
        return humidity;
    }

    public String getPm25() {
        return pm25;
    }

    public String getQuality() {
        return quality;
    }

    public String getWinddir() {
        return winddir;
    }

    public String getWindpower() {
        return windpower;
    }

    public String getDate() {
        return date;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getType() {
        return type;
    }


    public ImageView getWeatherImg() {
        return weatherImg;
    }

    public void setWeatherImg(ImageView weatherImg) {
        this.weatherImg = weatherImg;
    }

    public ImageView getPmImg() {
        return pmImg;
    }

    public void setPmImg(ImageView pmImg) {
        this.pmImg = pmImg;
    }


    public void setCity(String city) {
        this.city = city;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setWinddir(String winddir) {
        this.winddir = winddir;
    }

    public void setWindpower(String windpower) {
        this.windpower = windpower;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TodayWeather{" +
                "city='" + city + "\'" +
                ",updatetime='" + updatetime + "\'" +
                ",temperature='" + temperature + "\'" +
                ",humidity='" + humidity + "\'" +
                ",pm25='" + pm25 + "\'" +
                ",quality'=" + quality + "\'" +
                ",winddir='" + winddir + "\'" +
                ",windpower='" + windpower + "\'" +
                ",data='" + date + "\'" +
                ",high='" + high + "\'" +
                ",low='" + low + "\'" +
                ",type='" + type + "\'" +
                '}';
    }
}