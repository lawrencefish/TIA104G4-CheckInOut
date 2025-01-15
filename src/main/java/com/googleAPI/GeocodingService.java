package com.googleAPI;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;


@Service
public class GeocodingService {

    private static final String GOOGLE_GEOCODING_API_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String GOOGLE_GEOCODING_PLACE_API_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json";

    @Value("${google.api.key}")
    private String apiKey;

    public Double[] getCoordinatesFromAddress(String address) {
        RestTemplate restTemplate = new RestTemplate();
        String url = GOOGLE_GEOCODING_API_URL + "?address=" + address + "&key=" + apiKey;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject jsonResponse = new JSONObject(response.getBody());
            if ("OK".equals(jsonResponse.getString("status"))) {
                JSONObject location = jsonResponse
                        .getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");

                return new Double[]{location.getDouble("lat"), location.getDouble("lng")};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // 無法解析地址時返回 null
    }
    
    public Double[] getCoordinatesFromPlace(String location) {
        RestTemplate restTemplate = new RestTemplate();
        String url = GOOGLE_GEOCODING_PLACE_API_URL + "?query=" + location + "&key=" + apiKey;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject jsonResponse = new JSONObject(response.getBody());
            if ("OK".equals(jsonResponse.getString("status"))) {
                JSONObject place = jsonResponse
                        .getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");

                return new Double[]{place.getDouble("lat"), place.getDouble("lng")};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // 無法解析地址時返回 null
    }

    
}
