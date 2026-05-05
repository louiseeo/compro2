package com.weather.app.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.weather.app.model.WeatherResponse;

public class WeatherService {
    private Gson gson = new Gson();
    private HttpClient client = HttpClient.newHttpClient();

    public WeatherResponse getForecast(double lat, double lon) {
        String url = String.format(
                "https://www.7timer.info/bin/astro.php?lon=%s&lat=%s&ac=0&unit=metric&output=json", lon,
                lat);

        // Build HttpRequest using GET method
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Send request synchronously
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            // Check status code
            if (status == 200) {
                return gson.fromJson(response.body(), WeatherResponse.class);
            } else {
                System.out.println("ERROR: " + status);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());

        }
        return null;

    }
}
