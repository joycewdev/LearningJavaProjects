/* Back end functions that retrieves weather data from external API and returns it for the GUI to display
 * Credits: written with help of https://www.youtube.com/watch?v=8ZcEYv2ezWc and uses https://open-meteo.com/ API
 * @Author: Joyce W
 * @Date: May 3, 2024
 * @Version 1.0
 */

// Imports necessary items
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WangJWeatherAppAPICode {
	// This method will retrieve weather data for the location specified by user
	// @Param: location
	// @Returns: weatherData or null
	public static JSONObject getWeatherData(String location) {
		// Attempts to call API for a response
		try {
			// Calls getLocationData method to get latitude & longitude info for location
			JSONArray locationData = getLocationData(location);
			JSONObject locationStats = (JSONObject) locationData.get(0);
			double latitude = (double) locationStats.get("latitude");
			double longitude = (double) locationStats.get("longitude");
			// Uses those coordinates to create the API request url
			String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude
					+ "&hourly=temperature_2m,relativehumidity_2m,weathercode,windspeed_10m&timezone=America%2FNew_York";
			
			HttpURLConnection connection = fetchAPIResponse(url);
			// Checks the status of response - 200 is successful
			if (connection.getResponseCode() != 200) {
				System.out.println("Error: could not connect to API");
				return null;
			}

			// Stores API results by reading JSON data into String Builder
			StringBuilder resultJSON = new StringBuilder();
			Scanner scan = new Scanner(connection.getInputStream());
			while (scan.hasNext()) {
				resultJSON.append(scan.nextLine());
			}
			scan.close();
			connection.disconnect();

			// Parses JSON string results into a JSON object
			JSONParser parser = new JSONParser();
			JSONObject results = (JSONObject) parser.parse(String.valueOf(resultJSON));

			// Gets the temperature, weather condition, humidity, and windspeed according to
			// hourly time read from the results parsed above
			JSONObject hourlyData = (JSONObject) results.get("hourly");
			JSONArray time = (JSONArray) hourlyData.get("time");
			int index = findIndexCurrentTime(time);

			JSONArray temperatureData = (JSONArray) hourlyData.get("temperature_2m");
			double temperature = (double) temperatureData.get(index);

			JSONArray weathercode = (JSONArray) hourlyData.get("weathercode");
			String weatherCondition = convertWeatherCode((long) weathercode.get(index));

			JSONArray humidityData = (JSONArray) hourlyData.get("relativehumidity_2m");
			long humidity = (long) humidityData.get(index);

			JSONArray windspeedData = (JSONArray) hourlyData.get("windspeed_10m");
			double windspeed = (double) windspeedData.get(index);

			// Build JSON object with the weather data that can be accessed by GUI
			JSONObject weatherData = new JSONObject();
			weatherData.put("temperature", temperature);
			weatherData.put("weather_condition", weatherCondition);
			weatherData.put("humidity", humidity);
			weatherData.put("windspeed", windspeed);

			return weatherData;
		} catch (Exception error) {
			System.out.println("Error. Could not retrieve weather data.");
		}
		return null;
	}

	// This method will retrieve location coordinates using geolocation API
	// @Param: location
	// @Returns: locationData or null
	public static JSONArray getLocationData(String location) {
		// Replaces white spaces in location name to match API's request format
		location = location.replaceAll(" ", "+");

		// Creates API URL using the location name
		String url = "https://geocoding-api.open-meteo.com/v1/search?name=" + location
				+ "&count=10&language=en&format=json";

		// Attempts to call API for a response
		try {
			HttpURLConnection connection = fetchAPIResponse(url);
			// Checks the status of response - 200 is successful
			if (connection.getResponseCode() != 200) {
				System.out.println("Error: could not connect to API");
				return null;
			} else {
				// Stores API results by reading JSON data into String Builder
				StringBuilder resultJSON = new StringBuilder();
				Scanner scan = new Scanner(connection.getInputStream());
				while (scan.hasNext()) {
					resultJSON.append(scan.nextLine());
				}
				scan.close();
				connection.disconnect();

				// Parses JSON string into a JSON object
				JSONParser parser = new JSONParser();
				JSONObject results = (JSONObject) parser.parse(String.valueOf(resultJSON));

				// Gets location data from results read above
				JSONArray locationData = (JSONArray) results.get("results");
				return locationData;
			}
		} catch (Exception error) {
			System.out.println("Error. Could not retrieve location data.");
		}
		return null;
	}

	// This method will attempt to establish a connection with API
	// @Param: url
	// @Returns: connection or null
	private static HttpURLConnection fetchAPIResponse(String url) {
		// Attempts to connect to API with "GET" request
		try {
			URL urlLink = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urlLink.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			return connection;
		} catch (IOException error) {
			System.out.println("Error. Could not connect to API.");
		}
		return null;
	}

	// This method will loop through the time list and find whichever matches our
	// current time
	// @Param: url
	// @Returns: connection or null
	private static int findIndexCurrentTime(JSONArray time) {
		// Calls getCurrentTime method to get the current system time
		String currentTime = getCurrentTime();
		// Matches current time to correct time in array retrieved from 7 day forecast
		for (int i = 0; i < time.size(); i++) {
			String timeVal = (String) time.get(i);
			if (timeVal.equalsIgnoreCase(currentTime)) {
				return i;
			}
		}
		return 0;
	}

	// This method will get current time of system and convert to match API's format
	// @Param: none
	// @Returns: formattedDateTime
	public static String getCurrentTime() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
		String formattedDateTime = currentDateTime.format(formatter);
		return formattedDateTime;
	}

	// This method will convert weather code values to corresponding weather
	// condition text
	// @Param: weathercode
	// @Returns: weatherCondition
	private static String convertWeatherCode(long weathercode) {
		String weatherCondition = "";
		if (weathercode == 0L)
			weatherCondition = "Sunny";
		else if (weathercode >= 1L && weathercode <= 2L)
			weatherCondition = "Partly Cloudy";
		else if (weathercode == 3L)
			weatherCondition = "Cloudy";
		else if ((weathercode >= 51L && weathercode <= 67L) || (weathercode >= 80L && weathercode <= 99L))
			weatherCondition = "Raining";
		else if (weathercode >= 71L && weathercode <= 77L)
			weatherCondition = "Snowing";
		else if (weathercode >= 95L && weathercode <= 99L)
			weatherCondition = "Thunderstorm";
		return weatherCondition;
	}
}
