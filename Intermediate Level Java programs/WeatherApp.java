import java.net.URI;
import java.net.http.*;
import java.util.Scanner;
import java.util.regex.*;

public class WeatherApp {
    public static void main(String[] args) throws Exception {
        String apiKey = System.getenv("WEATHER_API_KEY");
        if (apiKey == null || apiKey.isBlank()){
            System.out.println("Set WEATHER_API_KEY environment variable first.");
            return;
        }
        Scanner sc = new Scanner(System.in);
        HttpClient client = HttpClient.newHttpClient();

        while (true){
            System.out.print("\n Enter city (or 'quit'): ");
            String city = sc.nextLine().trim();
            if (city.equalsIgnoreCase("quit")) break;
            if (city.isEmpty()) continue;

            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city.replace(" ", "%20") + "&appid=" + apiKey + "&units=metric";
            HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200){
                System.out.println("City not found or API error (" + resp.statusCode() + ")");
                continue;
            }

            String json = resp.body();
            String name = match(json, "\"name\":\"([^\"]+)\"");
            String country = match(json, "\"country\":\"([^\"]+)\"");
            String desc = match(json, "\"description\":\"([^\"]+)\"");
            String temp = match(json, "\"temp\":(-?[\\d.]+)");
            String feels = match(json, "\"feels_like\":(-?[\\d.]+)");
            String humidity = match(json, "\"humidity\":(\\d+)");

            System.out.printf("%n %s, %s%n", name, country);
            System.out.printf("     Weather: %s%n", desc);
            System.out.printf("     Temp: %s°C (feels like %s°C)%n", temp, feels);
            System.out.printf("     Humidity: %s%%%n", humidity);
        }
        sc.close();
    }

    static String match(String json, String pattern){
        Matcher m = Pattern.compile(pattern).matcher(json);
        return m.find() ? m.group(1) : "?";
    }
}