package com.chikawa.user_service.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth/line")
public class LineCallbackController {

    @Value("${line.channel.id}")
    private String channelId;

    @Value("${line.channel.secret}")
    private String channelSecret;

    @Value("${line.channel.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {

        // 1️⃣ Chuẩn bị params để đổi code lấy access_token
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        params.add("client_id", channelId);
        params.add("client_secret", channelSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 2️⃣ Gọi API lấy access_token
        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                "https://api.line.me/oauth2/v2.1/token",
                request,
                Map.class
        );

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // 3️⃣ Gọi API lấy profile user
        HttpHeaders profileHeaders = new HttpHeaders();
        profileHeaders.setBearerAuth(accessToken);
        HttpEntity<String> profileRequest = new HttpEntity<>(profileHeaders);

        ResponseEntity<Map> profileResponse = restTemplate.exchange(
                "https://api.line.me/v2/profile",
                HttpMethod.GET,
                profileRequest,
                Map.class
        );

        // 4️⃣ Trả về thông tin user
        return ResponseEntity.ok(profileResponse.getBody());
    }
}
