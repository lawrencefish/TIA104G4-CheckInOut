package com.Lawrencefish.map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final String API_KEY = "AIzaSyB57iDNEzVhM7aKXRz3vjz6d1Y_Mg07G1E"; // 替換為你的 Google Maps API Key

    @GetMapping("/geocode")
    public Map<String, Object> getCoordinates(@RequestParam("address") String address) {
        String url = String.format(
                "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
                address,
                API_KEY
        );

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || !response.containsKey("results") || ((java.util.List<?>) response.get("results")).isEmpty()) {
            throw new RuntimeException("地址解析失敗，請確認輸入的地址是否正確！");
        }

        Map<String, Object> result = (Map<String, Object>) ((java.util.List<?>) response.get("results")).get(0);
        Map<String, Object> geometry = (Map<String, Object>) result.get("geometry");
        Map<String, Object> location = (Map<String, Object>) geometry.get("location");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("latitude", location.get("lat"));
        responseBody.put("longitude", location.get("lng"));

        return responseBody;
    }
}