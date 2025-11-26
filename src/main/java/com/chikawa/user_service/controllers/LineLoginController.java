package com.chikawa.user_service.controllers;

import com.chikawa.user_service.dto.response.ApiResponse;
import com.chikawa.user_service.dto.response.UserLoginResponse;
import com.chikawa.user_service.models.User;
import com.chikawa.user_service.repositories.UserRepository;
import com.chikawa.user_service.services.AuthenticationService;
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
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        String url = lineLoginService.generateLoginUrl();
        response.sendRedirect(url);
    }

    @PostMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) throws ParseException {
//        try {
            // Lấy thông tin user sau callback LINE
            var userData = lineLoginService.handleCallback(code);

            String lineUserId = (String) userData.get("lineUserId");

            // Lấy user để tạo JWT
            User user = userRepository.findByLineUserId(lineUserId).orElseThrow();

            // Tạo JWT
            String jwt = authenticationService.generateToken(user);

            UserLoginResponse userLoginResponse = new UserLoginResponse().builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .lineId(lineUserId)
                    .build();
            return ResponseEntity
                    .status(200) // Redirect
                    .header("Authorization", "Bearer " + jwt)
                    .header("Access-Control-Expose-Headers", "Authorization") // FE đọc header được
//                    .header("Location", "http://localhost:5173/")
                    .body(userLoginResponse)
                    ;

//        } catch (Exception e) {
//            return
//                    ResponseEntity
//                    .status(302)
//                    .header("Location", "http://localhost:5173/login?error=line")
//                    .build();


//        }
    }
}
