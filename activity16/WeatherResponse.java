package com.weather.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    private String product;
    @SerializedName("dataseries")
    private List<Forecast> forecasts;

    public WeatherResponse() {}
    
    public String getProduct() {
        return product;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

}
