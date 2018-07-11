package com.nick.miniweather.bean;

public class TodayWeather {
    private String city;
    private String updatetime;
    private String temperature;
    private String humidity;
    private String pm25;
    private String quality;
    private String winddir;
    private String windpower;
    private String data;
    private String high;
    private String low;
    private String type;

    public String getCity() {
        return city;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public String getTemperature() {
        return temperature;
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

    public String getData() {
        return data;
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

    public void setData(String data) {
        this.data = data;
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
                "city'" + city + "\'" +
                "updatetime'" + updatetime + "\'" +
                "temperature'" + temperature + "\'" +
                "humidity'" + humidity + "\'" +
                "pm25'" + pm25 + "\'" +
                "quality'" + quality + "\'" +
                "winddir'" + winddir + "\'" +
                "windpower'" + windpower + "\'" +
                "data'" + data + "\'" +
                "high'" + high + "\'" +
                "low'" + low + "\'" +
                "type'" + type + "\'" +
                '}';
    }
}