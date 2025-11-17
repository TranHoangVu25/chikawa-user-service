package com.chikawa.user_service.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/line")
public class LineLoginController {

    @Value("${line.channel.id}")
    private String channelId;

    @Value("${line.channel.redirect-uri}")
    private String redirectUri;

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        String state = UUID.randomUUID().toString(); // dùng để chống CSRF
        String scope = "profile openid email";

        String url = "https://access.line.me/oauth2/v2.1/authorize"
                + "?response_type=code"
                + "&client_id=" + channelId
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&state=" + state
                + "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8);

        response.sendRedirect(url);
    }
}
