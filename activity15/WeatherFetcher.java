import java.net.http.*;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

public class WeatherFetcher {
    public static void main(String[] args) {
        // Create a scanner
        Scanner sc = new Scanner(System.in);

        // Prompt user to input Latitude and Longitude
        System.out.print("Enter Latitude (e.g., 14.59 for Manila): ");
        String latitude = sc.nextLine();

        System.out.print("Enter Longitude (e.g., 120.98 for Manila): ");
        String longitude = sc.nextLine();

        // Create an HttpClient
        HttpClient client = HttpClient.newHttpClient();

        String url = String.format(
                "https://www.7timer.info/bin/astro.php?lon=%s&lat=%s&ac=0&unit=metric&output=json", longitude,
                latitude);

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
                System.out.println("SUCCESS!");
                System.out.println(response.body());
            } else {
                System.out.println("ERROR: " + status);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());

        } finally {
            sc.close(); // Close scanner
        }

    }
}