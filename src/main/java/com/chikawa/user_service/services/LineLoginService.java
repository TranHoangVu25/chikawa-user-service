package com.chikawa.user_service.services;

import com.chikawa.user_service.models.User;
import com.chikawa.user_service.repositories.UserRepository;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class LineLoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${line.channel.id}")
    private String channelId;

    @Value("${line.channel.redirect-uri}")
    private String redirectUri;

    @Value("${line.channel.secret}")
    private String channelSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Tạo URL redirect sang LINE OAuth
     */
    public String generateLoginUrl() {
        String state = UUID.randomUUID().toString();
        String scope = "profile openid email";

        return "https://access.line.me/oauth2/v2.1/authorize"
                + "?response_type=code"
                + "&client_id=" + channelId
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&state=" + state
                + "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8);
    }

    /**
     * Xử lý callback, đổi code thành access_token, lấy profile
     */
    public Map<String, Object> handleCallback(String code) throws ParseException {

        //Tạo params đổi code thành access_token
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        params.add("client_id", channelId);
        params.add("client_secret", channelSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // gọi API lấy access_token + id_token
        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                "https://api.line.me/oauth2/v2.1/token",
                request,
                Map.class
        );

        String accessToken = (String) tokenResponse.getBody().get("access_token");
        String idToken = (String) tokenResponse.getBody().get("id_token");

        // giải mã id_token để lấy email + name + sub
        SignedJWT signedJWT = (SignedJWT) JWTParser.parse(idToken);
        var claims = signedJWT.getJWTClaimsSet();

        String email = claims.getStringClaim("email");
        String name = claims.getStringClaim("name");
        String lineId = claims.getSubject(); // sub = user unique id

        String jit = UUID.randomUUID().toString();
        String password = passwordEncoder.encode(lineId);
        String role = "customer";

        if (email == null || email.isBlank()) {
            email = lineId + "@line.local";  // unique + không bị conflict
        }

        User user = userRepository.findByLineUserId(lineId).orElse(null);

        if (user == null) {
            user = User.builder()
                    .email(email)
                    .fullName(name)
                    .encryptedPassword(password)
                    .lineUserId(lineId)
                    .jti(jit)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .signInCount(1)
                    .role(role)
                    .lastSignInAt(LocalDateTime.now())
                    .build();

        } else {
            user.setFullName(name);
            user.setEmail(email);
            user.setUpdatedAt(LocalDateTime.now());
            user.setJti(jit);
            user.setSignInCount(user.getSignInCount() + 1);
            user.setLastSignInAt(LocalDateTime.now());
        }

        userRepository.save(user);

        return Map.of(
                "name", name,
                "lineUserId", lineId
        );
    }
}
