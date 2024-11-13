package com.example.User.service;


import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Service
public class MapService {

    @Value("${MAP_API_KEY}")
    private String API_KEY;

    public double[] parseCoordinates(String jsonResponse) {
        JsonObject json = JsonParser.parseString(jsonResponse).getAsJsonObject();

        JsonArray results = json.getAsJsonArray("results");
        if (results.size() > 0) {
            JsonObject location = results.get(0).getAsJsonObject().getAsJsonObject("geometry").getAsJsonObject("location");
            double latitude = location.get("lat").getAsDouble();
            double longitude = location.get("lng").getAsDouble();
            return new double[]{latitude, longitude};
        } else {
            throw new CustomException(ErrorCode.CANNOT_FIND_POSITION);
        }
    }

    public double[] getCoordinates(String address){
        try {
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress + "&key=" + API_KEY;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                // Parse JSON response to get coordinates
                String jsonResponse = response.toString();
                double[] coordinates = parseCoordinates(jsonResponse);
                return coordinates;
            } else {
                throw new CustomException(ErrorCode.CANNOT_FIND_POSITION);
            }
        } catch (IOException e) {
            throw new CustomException(ErrorCode.SERVER_ERROR);
        }
    }
}
