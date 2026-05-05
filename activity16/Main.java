package com.weather.app;

import java.util.List;
import java.util.Scanner;

import com.weather.app.model.Forecast;
import com.weather.app.model.WeatherResponse;
import com.weather.app.service.WeatherService;

public class Main {
    public static void main(String[] args) {

        // Create a scanner
        Scanner sc = new Scanner(System.in);
        try {
            // Prompt user to input Latitude and Longitude
            System.out.print("Enter Latitude (e.g., 14.59 for Manila): ");
            Double lat = sc.nextDouble();

            System.out.print("Enter Longitude (e.g., 120.98 for Manila): ");
            Double lon = sc.nextDouble();

            // Call the WeatherService to get the forecast object
            WeatherService service = new WeatherService();
            WeatherResponse response = service.getForecast(lat, lon);

            if (response == null) {
                System.out.println("Could not retrieve weather data");
                return;
            }

            // Check if forecasts exist
            List<Forecast> forecasts = response.getForecasts();
            if (forecasts == null || forecasts.isEmpty()) {
                System.out.println("No forecast data available.");
                return;
            }

            // Make sure to limit loop three times for safe looping
            int limit = Math.min(3, forecasts.size());

            for (int i = 0; i < limit; i++) {
                Forecast forecast = forecasts.get(i);

                // Check for incomplete nested data
                if (forecast == null || forecast.getWind() == null) {
                    System.out.println("Incomplete data for forecast at index " + i);
                    continue;
                }

                // Print the formatted forecast
                System.out.println(String.format("At hour %d: %d°C with %d speed winds from the %s.",
                        forecast.getTimepoint(),
                        forecast.getTemperature(),
                        forecast.getWind().getSpeed(),
                        forecast.getWind().getDirection()));
            }

        } catch (Exception e) {
            System.out.println("Invalid input. Please enter valid numbers.");
        } finally {
            sc.close();
        }
    }

}