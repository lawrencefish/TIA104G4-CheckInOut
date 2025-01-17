package com.Lawrencefish.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/address")
public class AddressTestController {

    @Value("${google.api.key}")
    private String apiKey;

    @GetMapping("/test-geocode")
    public ResponseEntity<Map<String, Object>> getCoordinates(@RequestParam String address) {
        String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + apiKey;

        try {
            // 發送請求
//            System.out.println("Request URL: " + apiUrl);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(apiUrl, String.class);

            // 打印 API 回應
//            System.out.println("API Response: " + response);

            // 解析 JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonResponse = objectMapper.readValue(response, Map.class);

            // 檢查狀態
            String status = (String) jsonResponse.get("status");
            if (!"OK".equals(status)) {
                throw new RuntimeException("API 回應錯誤，狀態碼：" + status);
            }

            // 提取 location
            List<Map<String, Object>> results = (List<Map<String, Object>>) jsonResponse.get("results");
            Map<String, Object> geometry = (Map<String, Object>) results.get(0).get("geometry");
            Map<String, Object> location = (Map<String, Object>) geometry.get("location");

            // 返回經緯度
            Map<String, Object> result = new HashMap<>();
            result.put("latitude", location.get("lat"));
            result.put("longitude", location.get("lng"));
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "地址解析失敗，請確認輸入的地址是否正確！"));
        }
    }
}
