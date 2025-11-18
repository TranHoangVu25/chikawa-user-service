package com.chikawa.user_service.controllers;

import com.chikawa.user_service.services.LineLoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/auth/line")
@RequiredArgsConstructor
public class LineLoginController {

    private final LineLoginService lineLoginService;

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        String url = lineLoginService.generateLoginUrl();
        response.sendRedirect(url);
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) throws ParseException {
        return ResponseEntity.ok(lineLoginService.handleCallback(code));
    }
}
